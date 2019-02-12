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
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ElevatorSubsystem implements PeriodicRunnable, PositionalSubsystem {

    private static final double ENCODER_TICS_PER_INCH = 4096 / ((1.75 + .125) * Math.PI);
    private static final double MAX_HEIGHT = 65d;
    private static final double MIN_HEIGHT = 0d;
    private static final double MAX_VELOCITY = 30d;
    private static final double PERCENT_FOR_MAX_VELOCITY = .3d;
    private static final double MAX_ACCELERATION = 10d;
    private static final double KA = .2 / MAX_ACCELERATION;
    private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
    private static final double ACCEPTABLE_VARIATION = .5d;
    private static final double MAX_CLIMBING_HEIGHT = 48d;

    //PID
    private static final double P = 0;
    private static final double I = 0;
    private static final double D = 0;
    private static final double F = 0;

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private IMotorControllerEnhanced elevatorMaster = appCtx.getElevatorMaster();
    private IMotorController elevatorSlave1 = appCtx.getElevatorSlave();
    private IMotorController elevatorSlave2 = appCtx.getElevatorSlave2();
    private DoubleSolenoid elevatorShifter = appCtx.getElevatorShiftSolenoid();

    private DigitalInput topLimit = appCtx.getElevatorTopLimit();
    private DigitalInput bottomLimit = appCtx.getElevatorBottomLimit();

    private ElevatorPositionConverter converter = new ElevatorPositionConverter();

    private PidfControlLoop holdingClimbingPID = new PidfControlLoop(0, 0, 0, 0, 1);
    private PidfControlLoop holdingPID = new PidfControlLoop(0, 0, 0, 0, 1);
    private MotionProfileControlloop motionController = new MotionProfileControlloop(P, D,
            PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA / MAX_ACCELERATION, 1);
    private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
    private TrapezoidalProfileManager trapezoidalProfileManager = new TrapezoidalProfileManager(motionController,
            profileConfig, this::position, holdingPID);

    private Double setpoint;
    private Value shifterValue = elevatorShifter.get();

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

        elevatorMaster.configPeakOutputForward(.5, 10);
        elevatorSlave1.configPeakOutputForward(.5, 10);
        elevatorSlave2.configPeakOutputForward(.5, 10);
        elevatorMaster.configPeakOutputReverse(-.5, 10);
        elevatorSlave1.configPeakOutputReverse(-.5, 10);
        elevatorSlave2.configPeakOutputReverse(-.5, 10);

        elevatorSlave1.follow(elevatorMaster);
        elevatorSlave2.follow(elevatorMaster);

//        holdingClimbingPID.setPositionSupplier(this::position);
    }

    @Override
    public void onPeriodic() {
//        if (setpoint == null) {
//            return;
//        }
//
//        bottomZero.onPeriodic();
//        topZero.onPeriodic();
//
//        if (!isClimbing) {
//            normalOperation(setpoint);
//        } else if (isClimbing) {
//            climbingOperation();
//        }

        if (appCtx.getController().getRawButton(2)) {
            elevatorMaster.setSelectedSensorPosition(0, 0, 10);
        }
//        if (appCtx.getController().getRawButton(1)) {
//            runInElevator();
//        } else {
//        if (position() < MIN_HEIGHT + 3) {
//            elevatorMaster.set(ControlMode.PercentOutput, .3);
//        } else if (position() > MAX_HEIGHT - 3) {
//            elevatorMaster.set(ControlMode.PercentOutput, -.3);
//        } else {
            elevatorMaster.set(ControlMode.PercentOutput, -appCtx.getController().getRawAxis(1));
//        }
        System.out.println("limit switch: " + topLimit.get() + "Elevator position: " + position());
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

    private void normalOperation(double setpoint) {
        if (!isPositionSafe(setpoint)) {
            setpoint = findClosestBound(MIN_HEIGHT, setpoint, MAX_HEIGHT);
        }

        trapezoidalProfileManager.setPoint(setpoint);
        trapezoidalProfileManager.newOutput();

        if (!isAtSetpoint()) {
            if (shifterValue == Value.kReverse) {
                elevatorShifter.set(Value.kForward);
            }
            elevatorMaster.set(ControlMode.PercentOutput, trapezoidalProfileManager.getOutput());
        } else {
            elevatorShifter.set(Value.kReverse);
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

    private boolean isAtSetpoint() {
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
