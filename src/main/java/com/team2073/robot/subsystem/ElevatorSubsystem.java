package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.position.zeroer.Zeroer;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;

public class ElevatorSubsystem implements PeriodicRunnable, PositionalSubsystem {

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ElevatorProperties elevatorProperties = loader.registerPropContainer(ElevatorProperties.class);

    private static final double ENCODER_TICS_PER_INCH = 1000d;
    private static final double MAX_HEIGHT = 85d;
    private static final double MIN_HEIGHT = 0d;
    private static final double MAX_VELOCITY = 20d;
    private static final double PERCENT_FOR_MAX_VELOCITY = 0d;
    private static final double MAX_ACCELERATION = 10d;
    private static final double KA = .2 / MAX_ACCELERATION;
    private static final double TIME_STEP = 0d;

    private IMotorControllerEnhanced elevatorMaster = appCtx.getElevatorMaster();
    private IMotorController elevatorSlave1 = appCtx.getElevatorSlave();
    private IMotorController elevatorSlave2 = appCtx.getElevatorSlave2();

    private DigitalInput topLimit = appCtx.getElevatorTopLimit();
    private DigitalInput bottomLimit = appCtx.getElevatorBottomLimit();

    private ElevatorPositionConverter converter = new ElevatorPositionConverter();

    private Double setpoint;
    private Zeroer topZero = new Zeroer(topLimit, elevatorMaster);
    private Zeroer bottomZero = new Zeroer(bottomLimit, elevatorMaster);

    public ElevatorSubsystem() {
        autoRegisterWithPeriodicRunner();
        TalonUtil.resetTalon(elevatorMaster, TalonUtil.ConfigurationType.SENSOR);
        TalonUtil.resetVictor(elevatorSlave1, TalonUtil.ConfigurationType.SLAVE);
        TalonUtil.resetVictor(elevatorSlave2, TalonUtil.ConfigurationType.SLAVE);

        elevatorSlave1.follow(elevatorMaster);
        elevatorSlave2.follow(elevatorMaster);

        loader.loadProperties();
    }

    //PID
    private final double P = elevatorProperties.elevator_P;
    private final double I = elevatorProperties.elevator_I;
    private final double D = elevatorProperties.elevator_D;
    private final double F = elevatorProperties.elevator_Holding_F;

    private final double holding_P = elevatorProperties.elevator_Holding_P;
    private final double holding_I = elevatorProperties.elevator_Holding_I;
    private final double holding_D = elevatorProperties.elevator_Holding_D;
    private final double holding_F = elevatorProperties.elevator_Holding_F;

    private PidfControlLoop holdingPID = new PidfControlLoop(holding_P, holding_I, holding_D ,holding_F, 1);
    private MotionProfileControlloop controller = new MotionProfileControlloop(P, D,
            PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA, 1);
    private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
    private TrapezoidalProfileManager trapezoidalProfileManager = new TrapezoidalProfileManager(controller,
            profileConfig, this::position, holdingPID);

    @Override
    public void onPeriodic() {
        if (setpoint == null) {
            return;
        }

        bottomZero.onPeriodic();
        topZero.onPeriodic();

        if(!isPositionSafe(setpoint)){
            setpoint = findClosestBound(MIN_HEIGHT, setpoint, MAX_HEIGHT);
        }

        trapezoidalProfileManager.setPoint(setpoint);
        trapezoidalProfileManager.newOutput();
        elevatorMaster.set(ControlMode.PercentOutput, trapezoidalProfileManager.getOutput());
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

    @Override
    public double position() {
        return converter.asPosition(elevatorMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double velocity() {
        return converter.asPosition(elevatorMaster.getSelectedSensorVelocity(0))*10;
    }

    @Override
    public void set(Double setpoint) {
        this.setpoint = setpoint;
    }

    private class ElevatorPositionConverter implements PositionConverter {

        @Override
        public double asPosition(int tics) {
            return tics * ENCODER_TICS_PER_INCH;
        }

        @Override
        public int asTics(double position) {
            return (int) (position / ENCODER_TICS_PER_INCH);
        }

        @Override
        public String positionalUnit() {
            return Units.INCHES;
        }
    }

    @PropertyContainer
    public static class ElevatorProperties{
        private double elevator_P;
        private double elevator_I;
        private double elevator_D;
        private double elevator_F;

        private double elevator_Holding_P;
        private double elevator_Holding_I;
        private double elevator_Holding_D;
        private double elevator_Holding_F;

        public double getElevator_P() {
            return elevator_P;
        }

        public void setElevator_P(double elevator_P) {
            this.elevator_P = elevator_P;
        }

        public double getElevator_I() {
            return elevator_I;
        }

        public void setElevator_I(double elevator_I) {
            this.elevator_I = elevator_I;
        }

        public double getElevator_D() {
            return elevator_D;
        }

        public void setElevator_D(double elevator_D) {
            this.elevator_D = elevator_D;
        }

        public double getElevator_F() {
            return elevator_F;
        }

        public void setElevator_F(double elevator_F) {
            this.elevator_F = elevator_F;
        }

        public double getElevator_Holding_P() {
            return elevator_Holding_P;
        }

        public void setElevator_Holding_P(double elevator_Holding_P) {
            this.elevator_Holding_P = elevator_Holding_P;
        }

        public double getElevator_Holding_I() {
            return elevator_Holding_I;
        }

        public void setElevator_Holding_I(double elevator_Holding_I) {
            this.elevator_Holding_I = elevator_Holding_I;
        }

        public double getElevator_Holding_D() {
            return elevator_Holding_D;
        }

        public void setElevator_Holding_D(double elevator_Holding_D) {
            this.elevator_Holding_D = elevator_Holding_D;
        }

        public double getElevator_Holding_F() {
            return elevator_Holding_F;
        }

        public void setElevator_Holding_F(double elevator_Holding_F) {
            this.elevator_Holding_F = elevator_Holding_F;
        }
    }
}
