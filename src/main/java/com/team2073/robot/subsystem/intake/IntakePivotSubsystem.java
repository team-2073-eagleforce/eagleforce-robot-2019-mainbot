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
	private IntakePivotProperties intakePivotProperties = loader.registerPropContainer(IntakePivotProperties.class);

    private IMotorControllerEnhanced intakeMaster = appCtx.getIntakePivotMaster();
    private AnalogPotentiometer pot = appCtx.getIntakePot();
	//PID
	private final double P = intakePivotProperties.intake_P;
	private final double I = intakePivotProperties.intake_I;
	private final double D = intakePivotProperties.intake_D;
	private final double F = intakePivotProperties.intake_F;

	private final double holding_P = intakePivotProperties.intake_Holding_P;
	private final double holding_I = intakePivotProperties.intake_Holding_I;
	private final double holding_D = intakePivotProperties.intake_Holding_D;
	private final double holding_F = intakePivotProperties.intake_Holding_F;

    private Double setpoint = null;
    private boolean hasZeroed = false;

	private PositionConverter converter = new IntakePositionConverter();
	private PidfControlLoop holdingPID = new PidfControlLoop(holding_P, holding_I, holding_D, holding_F, 1);
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
        if(!hasZeroed)
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

		if(RobotState.isEnabled()){
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
		private double intake_P = .005;
		private double intake_I;
		private double intake_D = 0;
		private double intake_F;

		public double getIntake_P() {
			return intake_P;
		}

		public void setIntake_P(double intake_P) {
			this.intake_P = intake_P;
		}

		public double getIntake_I() {
			return intake_I;
		}

		public void setIntake_I(double intake_I) {
			this.intake_I = intake_I;
		}

		public double getIntake_D() {
			return intake_D;
		}

		public void setIntake_D(double intake_D) {
			this.intake_D = intake_D;
		}

		public double getIntake_F() {
			return intake_F;
		}

		public void setIntake_F(double intake_F) {
			this.intake_F = intake_F;
		}

		public double getIntake_Holding_P() {
			return intake_Holding_P;
		}

		public void setIntake_Holding_P(double intake_Holding_P) {
			this.intake_Holding_P = intake_Holding_P;
		}

		public double getIntake_Holding_I() {
			return intake_Holding_I;
		}

		public void setIntake_Holding_I(double intake_Holding_I) {
			this.intake_Holding_I = intake_Holding_I;
		}

		public double getIntake_Holding_D() {
			return intake_Holding_D;
		}

		public void setIntake_Holding_D(double intake_Holding_D) {
			this.intake_Holding_D = intake_Holding_D;
		}

		public double getIntake_Holding_F() {
			return intake_Holding_F;
		}

		public void setIntake_Holding_F(double intake_Holding_F) {
			this.intake_Holding_F = intake_Holding_F;
		}

		private double intake_Holding_P = .012;
		private double intake_Holding_I = .01;
		private double intake_Holding_D = 0;
		private double intake_Holding_F = 0;
	}

}
