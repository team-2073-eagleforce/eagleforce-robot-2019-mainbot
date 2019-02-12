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
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;

public class ElevatorSubsystem implements PeriodicRunnable, PositionalSubsystem {

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationProperties applicationProperties = loader.registerPropContainer(ApplicationProperties.class);
    private ElevatorProperties elevatorProperties = applicationProperties.getElevatorProperties();

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
    }

    //PID
    private final double P = elevatorProperties.getElevatorP();
    private final double I = elevatorProperties.getElevatorI();
    private final double D = elevatorProperties.getElevatorD();
    private final double F = elevatorProperties.getElevatorF();

    private final double holding_P = elevatorProperties.getElevatorHoldingP();
    private final double holding_I = elevatorProperties.getElevatorHoldingI();
    private final double holding_D = elevatorProperties.getElevatorHoldingD();
    private final double holding_F = elevatorProperties.getElevatorHoldingF();

    private PidfControlLoop holdingPID = new PidfControlLoop(holding_P, holding_I, holding_D, holding_F, 1);
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

        if (!isPositionSafe(setpoint)) {
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
        return converter.asPosition(elevatorMaster.getSelectedSensorVelocity(0)) * 10;
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

    public static class ElevatorProperties {
        private double elevatorP;
        private double elevatorI;
        private double elevatorD;
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
