package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.dev.GraphCSV;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotState;

public class ElevatorSubsystem implements PeriodicRunnable, PositionalSubsystem {

    private static final double ENCODER_TICS_PER_INCH = 697d;
    private static final double MAX_HEIGHT = 70.5d;
    private static final double MIN_HEIGHT = 0d;
    private static final double MAX_VELOCITY = 80.0d;
    private static final double PERCENT_FOR_MAX_VELOCITY = .85d;
    private static final double MAX_ACCELERATION = 300;
    private static final double KA = .1 / MAX_ACCELERATION;
    private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
    private static final double ACCEPTABLE_VARIATION = .125d;
    private static final double MAX_CLIMBING_HEIGHT = 48d;

    //PID
    private static final double P = 0.05;
    private static final double I = 0;
    private static final double D = 0.001;
    private static final double F = 0;

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private IMotorControllerEnhanced elevatorMaster = appCtx.getElevatorMaster();
    private IMotorController elevatorSlave1 = appCtx.getElevatorSlave();
    private IMotorController elevatorSlave2 = appCtx.getElevatorSlave2();
    private DoubleSolenoid elevatorShifter = appCtx.getElevatorShiftSolenoid();

//    private DigitalInput topLimit = appCtx.getElevatorTopLimit();
//    private DigitalInput bottomLimit = appCtx.getElevatorBottomLimit();

    private ElevatorPositionConverter converter = new ElevatorPositionConverter();

    private PidfControlLoop holdingClimbingPID = new PidfControlLoop(0, 0, 0, 0, 1);
    private PidfControlLoop holdingPID = new PidfControlLoop(0.03, 0, 0, 0, 1);
    private MotionProfileControlloop motionController = new MotionProfileControlloop(P, D,
            PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA, 1);
    private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
    private TrapezoidalProfileManager trapezoidalProfileManager = new TrapezoidalProfileManager(motionController,
            profileConfig, this::position, holdingPID);

    private Double setpoint;
    private Value shifterValue = elevatorShifter.get();

    private GraphCSV graph = new GraphCSV("ElevatorProfile", "time", "Position", "Velocity",
            "setpoint", "output voltage", "profile position", "profile velocity", "profile acceleration", "integral position");
    private double time = 0;

//    private Zeroer topZero = new Zeroer(topLimit, elevatorMaster, converter.asTics(MAX_HEIGHT), 0, false);
//    private Zeroer bottomZero = new Zeroer(bottomLimit, elevatorMaster, converter.asTics(MIN_HEIGHT), 0, false);

    private boolean isClimbing = false;

    public ElevatorSubsystem() {
        autoRegisterWithPeriodicRunner();

        TalonUtil.resetTalon(elevatorMaster, TalonUtil.ConfigurationType.SENSOR);
        TalonUtil.resetVictor(elevatorSlave1, TalonUtil.ConfigurationType.SLAVE);
        TalonUtil.resetVictor(elevatorSlave2, TalonUtil.ConfigurationType.SLAVE);

        elevatorMaster.setSensorPhase(true);

        elevatorMaster.setInverted(true);
        elevatorSlave1.setInverted(true);
        elevatorSlave2.setInverted(true);
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

//        holdingClimbingPID.setPositionSupplier(this::position);
    }

    public double holdingStrategy() {
        elevatorShifter.set(Value.kReverse);
        return 0;
    }

    @Override
    public void onPeriodic() {
        if (appCtx.getController().getRawButton(1)) {
            graph.writeToFile();
        }
//        if (setpoint == null) {
//            return;
//        }
//
//        bottomZero.onPeriodic();
//        topZero.onPeriodic();

        if (appCtx.getController().getRawButton(6)) {
            elevatorMaster.setSelectedSensorPosition(0, 0, 10);
        }

        if (appCtx.getController().getRawButton(2)) {
            elevatorShifter.set(Value.kForward);
        }
//
//        if (((position() > MAX_HEIGHT - 2) && -appCtx.getController().getRawAxis(1) < 0) || ((position() < MIN_HEIGHT + 5) && -appCtx.getController().getRawAxis(1) > 0)) {
//            elevatorMaster.set(ControlMode.PercentOutput, -appCtx.getController().getRawAxis(1));
//        } else if (position() < MAX_HEIGHT - 2 && position() > MIN_HEIGHT + 5) {
//            elevatorMaster.set(ControlMode.PercentOutput, -appCtx.getController().getRawAxis(1));
//        } else {
//            elevatorMaster.set(ControlMode.PercentOutput, 0);
//        }

        if (!appCtx.getController().getRawButton(4)) {
            if (position() > MAX_HEIGHT - 15) {
                elevatorMaster.set(ControlMode.PercentOutput, 0);
            } else {
                normalOperation(50d);
            }
        } else {
            setpoint = null;
            elevatorMaster.set(ControlMode.PercentOutput, -appCtx.getController().getRawAxis(1));
        }


        System.out.println("Elevator position (tics): " + elevatorMaster.getSelectedSensorPosition(0) + "\t Position: " + position() + "\t voltage: " + elevatorMaster.getMotorOutputVoltage()
                + "\t gear(frwd high) " + elevatorShifter.get());
//        System.out.println(elevatorMaster.getMotorOutputVoltage() + "\t" + elevatorSlave1.getMotorOutputVoltage() + "\t" + elevatorSlave2.getMotorOutputVoltage());
    }

    private boolean isGoingUp = true;

    private void runInElevator() {
        if (isGoingUp) {
            if (position() < MAX_HEIGHT - 10) {
                elevatorMaster.set(ControlMode.PercentOutput, .3);
            } else {
                isGoingUp = false;
            }
        } else {
            if (position() > MIN_HEIGHT + 10) {
                elevatorMaster.set(ControlMode.PercentOutput, -.3);
            } else {
                isGoingUp = true;
            }
        }
    }
    private double lastIntegralPosition;
    private void normalOperation(double setpoint) {
        if (!isPositionSafe(setpoint)) {
            setpoint = findClosestBound(MIN_HEIGHT, setpoint, MAX_HEIGHT);
        }
        if (RobotState.isEnabled()) {

            if (isAtSetpoint(setpoint)) {
                holdingStrategy();
            }

            trapezoidalProfileManager.setPoint(setpoint);
            trapezoidalProfileManager.newOutput();

            lastIntegralPosition += trapezoidalProfileManager.getProfile().getCurrentVelocity() * .01;
            elevatorMaster.set(ControlMode.PercentOutput, trapezoidalProfileManager.getOutput());
            graph.updateMainFile(time, position(), velocity(), setpoint, elevatorMaster.getMotorOutputVoltage(),
                    trapezoidalProfileManager.getProfile().getCurrentPosition(),
                    trapezoidalProfileManager.getProfile().getCurrentVelocity(),
                    trapezoidalProfileManager.getProfile().getCurrentAcceleration(),
                    lastIntegralPosition);
            time += .01;
        }
    }

    private double climbHeight;

    private void climbingOperation() {
        if (isAnalogControlReleased()) {
            elevatorMaster.set(ControlMode.PercentOutput, holdingClimbingPID.getOutput());
        } else {
            if (position() < MAX_CLIMBING_HEIGHT) {
                elevatorMaster.set(ControlMode.PercentOutput, appCtx.getController().getRawAxis(5));
            } else {

            }
        }
        if (position() < MAX_CLIMBING_HEIGHT) {
            if (isAnalogControlReleased()) {
            } else {
                elevatorMaster.set(ControlMode.PercentOutput, appCtx.getController().getRawAxis(5));
            }

        } else {
            holdingClimbingPID.updateSetPoint(MAX_CLIMBING_HEIGHT);
            elevatorMaster.set(ControlMode.PercentOutput, MAX_CLIMBING_HEIGHT);
        }
    }

    private boolean isAnalogControlReleased() {
        //TODO fill out
        return false;
    }

    public void setElevatorShifter(Value value) {
        elevatorShifter.set(value);
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
}
