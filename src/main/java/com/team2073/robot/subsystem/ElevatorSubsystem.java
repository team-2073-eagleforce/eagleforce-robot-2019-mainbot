package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.*;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.position.zeroer.Zeroer;
import com.team2073.common.util.ConversionUtil;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotState;

public class ElevatorSubsystem implements PeriodicRunnable, PositionalSubsystem {

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private ElevatorProperties elevatorProperties = applicationProperties.getElevatorProperties();
    private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();

    private static final double ENCODER_TICS_PER_INCH = 697d;
    private static final double MAX_HEIGHT = 70.5d;
    private static final double MIN_HEIGHT = 0d;
    private static final double MAX_VELOCITY = 100.0d;
    private static final double PERCENT_FOR_MAX_VELOCITY = .85d;
    private static final double MAX_ACCELERATION = 300;
    private static final double KA = .1 / MAX_ACCELERATION;
    private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
    private static final double ACCEPTABLE_VARIATION = .125d;
    private static final double MAX_CLIMBING_HEIGHT = 55d;

    //PID
    private double P = .05;
    private double D = elevatorProperties.getElevatorD();


    private IMotorControllerEnhanced elevatorMaster = appCtx.getElevatorMaster();
    private IMotorController elevatorSlave1 = appCtx.getElevatorSlave();
    private IMotorController elevatorSlave2 = appCtx.getElevatorSlave2();
    private DoubleSolenoid elevatorShifter = appCtx.getElevatorShiftSolenoid();

    private DigitalInput topLimit = appCtx.getElevatorTopLimit();
    private DigitalInput bottomLimit = appCtx.getElevatorBottomLimit();

    private ElevatorPositionConverter converter = new ElevatorPositionConverter();

    private PidfControlLoop holdingClimbingPID = new PidfControlLoop(0.05, 0, 0, 0, 1);
    private PidfControlLoop holdingPID = new PidfControlLoop(0.15, 0.08, 0, 0, 1);
    private MotionProfileControlloop motionController = new MotionProfileControlloop(P, D,
            PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA, 1);
    private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
    private TrapezoidalProfileManager trapezoidalProfileManager = new TrapezoidalProfileManager(motionController,
            profileConfig, this::position, holdingPID);

    private Double setpoint;
    private Value shifterValue = elevatorShifter.get();

//    private Zeroer topZero = new Zeroer(topLimit, elevatorMaster, converter.asTics(MAX_HEIGHT), 0, true);
//    private Zeroer bottomZero = new Zeroer(bottomLimit, elevatorMaster, converter.asTics(MIN_HEIGHT), 0, true);

    private ElevatorState currentState = ElevatorState.NORMAL_OPERATION;
    private ElevatorState lastState;

    public ElevatorSubsystem() {
        autoRegisterWithPeriodicRunner();

        TalonUtil.resetTalon(elevatorMaster, TalonUtil.ConfigurationType.SENSOR);
        TalonUtil.resetVictor(elevatorSlave1, TalonUtil.ConfigurationType.SLAVE);
        TalonUtil.resetVictor(elevatorSlave2, TalonUtil.ConfigurationType.SLAVE);

        elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        elevatorMaster.setSensorPhase(true);

        elevatorMaster.setInverted(directionalityProperties.isElevatorMaster());
        elevatorSlave1.setInverted(directionalityProperties.isElevatorSlave1());
        elevatorSlave2.setInverted(directionalityProperties.isElevatorSlave2());
        holdingPID.setPositionSupplier(this::position);

        elevatorMaster.setNeutralMode(NeutralMode.Brake);
        elevatorSlave1.setNeutralMode(NeutralMode.Brake);
        elevatorSlave2.setNeutralMode(NeutralMode.Brake);

        elevatorMaster.configPeakOutputForward(1, 10);
        elevatorSlave1.configPeakOutputForward(1, 10);
        elevatorSlave2.configPeakOutputForward(1, 10);
        elevatorMaster.configPeakOutputReverse(-1, 10);
        elevatorSlave1.configPeakOutputReverse(-1, 10);
        elevatorSlave2.configPeakOutputReverse(-1, 10);

        elevatorSlave1.follow(elevatorMaster);
        elevatorSlave2.follow(elevatorMaster);
        elevatorShifter.set(Value.kForward);
//        holdingClimbingPID.setPositionSupplier(this::position);
    }

    public enum ElevatorState {
        NORMAL_OPERATION,
        CLIMBING,
        HOLD_CLIMB;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.currentState = elevatorState;
    }

    public void shiftHighGear() {
        setElevatorShifter(Value.kForward);
    }

    public void shiftLowGear() {
        setElevatorShifter(Value.kReverse);
    }

    public ElevatorState getCurrentState() {
        return currentState;
    }

    @Override
    public void onPeriodic() {
//        bottomZero.onPeriodic();
//        topZero.onPeriodic();

        if (isAtBottom()) {
            elevatorMaster.setSelectedSensorPosition(converter.asTics(MIN_HEIGHT), 0, 10);
        }

        if (isAtTop()) {
            elevatorMaster.setSelectedSensorPosition(converter.asTics(MAX_HEIGHT), 0, 10);
        }

//        System.out.println("Ele voltage: " + elevatorMaster.getMotorOutputVoltage() + "elevator Position: " + position());
        if (setpoint == null) {
            return;
        }


        switch (currentState) {
            case NORMAL_OPERATION:
                normalOperation();
                break;
            case CLIMBING:
                climbingOperation();
                break;
            case HOLD_CLIMB:
                if(lastState != currentState){
                    holdingClimbingPID.updateSetPoint(position());
                }
                holdingClimbingPID.updatePID();
                elevatorMaster.set(ControlMode.PercentOutput, holdingClimbingPID.getOutput());
                break;

        }
        lastState = currentState;

//        if(appCtx.getWheel().getRawButton(3)){
//            climbingOperation();
//        }else {
//            normalOperation(setpoint);
//        }
    }

    private void holdElevator(){
        shiftHighGear();
        elevatorMaster.set(ControlMode.PercentOutput, .05);
    }

    private void normalOperation() {
        if (!isPositionSafe(setpoint)) {
            setpoint = findClosestBound(MIN_HEIGHT, setpoint, MAX_HEIGHT);
        }
        if(elevatorShifter.get() != Value.kForward){
            shiftHighGear();
        }
//        if (isAtSetpoint(setpoint)) {
//            shiftLowGear();
//        }

        trapezoidalProfileManager.setPoint(setpoint);
        trapezoidalProfileManager.newOutput();
        elevatorMaster.set(ControlMode.PercentOutput, trapezoidalProfileManager.getOutput());

    }

    private void climbingOperation() {
        double defaultOutput = -appCtx.getController().getRawAxis(5);
        double adjustedOutput = defaultOutput * ((MAX_CLIMBING_HEIGHT + 5 - position()) / (MAX_CLIMBING_HEIGHT + 5));
        double motorOutput;
        if(elevatorShifter.get() != Value.kReverse){
            shiftLowGear();
            motorOutput = 0;
        }else if(MAX_CLIMBING_HEIGHT - 5 < position()){
            motorOutput =  adjustedOutput;
        }else{
            motorOutput = defaultOutput;
        }

        if(isAtBottom() && defaultOutput < 0 ){
            motorOutput = 0;
        }

        elevatorMaster.set(ControlMode.PercentOutput, motorOutput);
    }

    private void setElevatorShifter(Value value) {
        elevatorShifter.set(value);
    }

    public void zeroElevator() {
        elevatorMaster.setSelectedSensorPosition(0, 0, 10);
    }

    private boolean isAtBottom(){
        return !bottomLimit.get();
    }

    private boolean isAtTop(){
        return !topLimit.get();
    }

    private boolean isPositionSafe(double position) {
        return position < MAX_HEIGHT || position > MIN_HEIGHT;
    }

    private double findClosestBound(double lowerBound, double position, double upperBound) {
        if (position - lowerBound > position - upperBound) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private boolean isAtSetpoint(double setpoint) {
        return setpoint - position() < ACCEPTABLE_VARIATION;
    }

    @Override
    public double position() {
        return converter.asPosition(elevatorMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double velocity() {
        return converter.asPosition(elevatorMaster.getSelectedSensorVelocity(0)) * 10;
    }

    @Override
    public void set(Double setpoint) {
        this.setpoint = setpoint;
    }

    private class ElevatorPositionConverter implements PositionConverter {

        @Override
        public double asPosition(int tics) {
            return tics / ENCODER_TICS_PER_INCH;
        }

        @Override
        public int asTics(double position) {
            return (int) (position * ENCODER_TICS_PER_INCH);
        }

        @Override
        public String positionalUnit() {
            return Units.INCHES;
        }
    }

    public static class ElevatorProperties {
        private double elevatorP = 0.05;
        private double elevatorI;
        private double elevatorD = 0.001;
        private double elevatorF;

        private double elevatorHoldingP;
        private double elevatorHoldingI;
        private double elevatorHoldingD;
        private double elevatorHoldingF;

        public double getElevatorP() {
            return elevatorP;
        }

        public void setElevatorP(double elevatorP) {
            this.elevatorP = elevatorP;
        }

        public double getElevatorI() {
            return elevatorI;
        }

        public void setElevatorI(double elevatorI) {
            this.elevatorI = elevatorI;
        }

        public double getElevatorD() {
            return elevatorD;
        }

        public void setElevatorD(double elevatorD) {
            this.elevatorD = elevatorD;
        }

        public double getElevatorF() {
            return elevatorF;
        }

        public void setElevatorF(double elevatorF) {
            this.elevatorF = elevatorF;
        }

        public double getElevatorHoldingP() {
            return elevatorHoldingP;
        }

        public void setElevatorHoldingP(double elevatorHoldingP) {
            this.elevatorHoldingP = elevatorHoldingP;
        }

        public double getElevatorHoldingI() {
            return elevatorHoldingI;
        }

        public void setElevatorHoldingI(double elevatorHoldingI) {
            this.elevatorHoldingI = elevatorHoldingI;
        }

        public double getElevatorHoldingD() {
            return elevatorHoldingD;
        }

        public void setElevatorHoldingD(double elevatorHoldingD) {
            this.elevatorHoldingD = elevatorHoldingD;
        }

        public double getElevatorHoldingF() {
            return elevatorHoldingF;
        }

        public void setElevatorHoldingF(double elevatorHoldingF) {
            this.elevatorHoldingF = elevatorHoldingF;
        }
    }

}
