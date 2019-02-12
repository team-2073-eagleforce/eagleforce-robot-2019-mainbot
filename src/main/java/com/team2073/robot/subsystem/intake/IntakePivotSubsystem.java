package com.team2073.robot.subsystem.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.RobotState;

public class IntakePivotSubsystem implements PeriodicRunnable, PositionalSubsystem {
    private static final double POT_MIN_VALUE = .889;
    private static final double POT_MAX_VALUE = .364;
    private static final double MIN_POSITION = 0;
    private static final double MAX_POSITION = 150;
    private static final double MAX_VELOCITY = 320;
    private static final double PERCENT_FOR_MAX_VELOCITY = .4;
    private static final double MAX_ACCELERATION = 500;
    private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
    private static final double TICS_PER_DEGREE = 4096d / 360d;
    private static final double KA = .2 / MAX_ACCELERATION;

    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationProperties applicationProperties = loader.registerPropContainer(ApplicationProperties.class);
    private IntakePivotProperties intakePivotProperties = applicationProperties.getIntakePivotProperties();

    private IMotorControllerEnhanced intakeMaster = appCtx.getIntakePivotMaster();
    private AnalogPotentiometer pot = appCtx.getIntakePot();
    //PID
    private final double P = intakePivotProperties.getIntakeP();
    private final double I = intakePivotProperties.getIntakeI();
    private final double D = intakePivotProperties.getIntakeD();
    private final double F = intakePivotProperties.getIntakeF();

    private final double HOLDING_P = intakePivotProperties.getIntakeHoldingP();
    private final double HOLDING_I = intakePivotProperties.getIntakeHoldingI();
    private final double HOLDING_D = intakePivotProperties.getIntakeHoldingD();
    private final double HOLDING_F = intakePivotProperties.getIntakeHoldingF();

    private Double setpoint = null;
    private boolean hasZeroed = false;

    private PositionConverter converter = new IntakePositionConverter();
    private PidfControlLoop holdingPID = new PidfControlLoop(HOLDING_P, HOLDING_I, HOLDING_D, HOLDING_F, 1);
    private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
    private MotionProfileControlloop controller = new MotionProfileControlloop(P, D,
            PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, .2 / MAX_ACCELERATION, 1);

    private TrapezoidalProfileManager profileManager = new TrapezoidalProfileManager(controller, profileConfig,
            this::position, holdingPID);

    public IntakePivotSubsystem() {
        autoRegisterWithPeriodicRunner();
        TalonUtil.resetTalon(intakeMaster, TalonUtil.ConfigurationType.SENSOR);
        intakeMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        zeroFromPot();
        holdingPID.setPositionSupplier(this::position);
        intakeMaster.configPeakOutputForward(.6, 10);
        intakeMaster.configPeakOutputReverse(-.6, 10);
    }

    @Override
    public void set(Double setPoint) {
        this.setpoint = setPoint;
    }

    @Override
    public double position() {
        return converter.asPosition(intakeMaster.getSelectedSensorPosition(0));
    }

    @Override
    public double velocity() {
        return converter.asPosition(intakeMaster.getSelectedSensorVelocity(0) * 10);
    }


    @Override
    public void onPeriodic() {
        if (!hasZeroed)
            zeroFromPot();

        if (setpoint == null) {
            return;
        }

//        if (RobotState.isEnabled()) {
//            holdingPID.updateSetPoint(setpoint);
//            holdingPID.updatePID(AppConstants.Subsystems.DEFAULT_TIMESTEP);
//            intakeMaster.set(ControlMode.PercentOutput, holdingPID.getOutput());
//        }

//		intakeMaster.set(ControlMode.PercentOutput, .3);

        if (RobotState.isEnabled()) {
            profileManager.setPoint(setpoint);
            profileManager.newOutput();
            intakeMaster.set(ControlMode.PercentOutput, profileManager.getOutput());
//            if(position() < 140){
//                intakeMaster.set(ControlMode.PercentOutput, .4);
//            }else{
//                intakeMaster.set(ControlMode.PercentOutput, 0);
//            }
        }
        System.out.println("Output: " + intakeMaster.getMotorOutputVoltage() + " \t Position: " + position() + " \t Holding Err: "
                + holdingPID.getError());
//        System.out.println("Potentiometer: " + pot.get() + " \t Encoder Position: " + position());

    }

    private void zeroFromPot() {
        if (velocity() < 1 && velocity() > -1) {
            intakeMaster.setSelectedSensorPosition(converter.asTics(potPosition()), 0, 10);
            hasZeroed = true;
        }
    }

    private double potPosition() {
        return (pot.get() - POT_MIN_VALUE) * (MAX_POSITION - MIN_POSITION) / (POT_MAX_VALUE - POT_MIN_VALUE) + MIN_POSITION;
    }

    private static class IntakePositionConverter implements PositionConverter {
        @Override
        public double asPosition(int tics) {
            return tics / TICS_PER_DEGREE;
        }

        @Override
        public int asTics(double position) {
            return (int) (position * TICS_PER_DEGREE);
        }

        @Override
        public String positionalUnit() {
            return Units.DEGREES;
        }
    }

    public static class IntakePivotProperties {
        private double intakeP = .005;
        private double intakeI;
        private double intakeD = 0;
        private double intakeF;

        private double intakeHoldingP = .012;
        private double intakeHoldingI = .01;
        private double intakeHoldingD = 0;
        private double intakeHoldingF = 0;

        public double getIntakeP() {
            return intakeP;
        }

        public void setIntakeP(double intakeP) {
            this.intakeP = intakeP;
        }

        public double getIntakeI() {
            return intakeI;
        }

        public void setIntakeI(double intakeI) {
            this.intakeI = intakeI;
        }

        public double getIntakeD() {
            return intakeD;
        }

        public void setIntakeD(double intakeD) {
            this.intakeD = intakeD;
        }

        public double getIntakeF() {
            return intakeF;
        }

        public void setIntakeF(double intakeF) {
            this.intakeF = intakeF;
        }

        public double getIntakeHoldingP() {
            return intakeHoldingP;
        }

        public void setIntakeHoldingP(double intakeHoldingP) {
            this.intakeHoldingP = intakeHoldingP;
        }

        public double getIntakeHoldingI() {
            return intakeHoldingI;
        }

        public void setIntakeHoldingI(double intakeHoldingI) {
            this.intakeHoldingI = intakeHoldingI;
        }

        public double getIntakeHoldingD() {
            return intakeHoldingD;
        }

        public void setIntakeHoldingD(double intakeHoldingD) {
            this.intakeHoldingD = intakeHoldingD;
        }

        public double getIntakeHoldingF() {
            return intakeHoldingF;
        }

        public void setIntakeHoldingF(double intakeHoldingF) {
            this.intakeHoldingF = intakeHoldingF;
        }
    }

}
