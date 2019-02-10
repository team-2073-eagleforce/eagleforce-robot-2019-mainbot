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
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.RobotState;

public class IntakePivotSubsystem implements PeriodicRunnable, PositionalSubsystem {
	private static final double POT_MIN_VALUE = .97;
	private static final double POT_MAX_VALUE = .35;
	private static final double MIN_POSITION = 0;
	private static final double MAX_POSITION = 158;
	private static final double MAX_VELOCITY = 350;
	private static final double PERCENT_FOR_MAX_VELOCITY = .4;
	private static final double MAX_ACCELERATION = 500;
	private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
	private static final double TICS_PER_DEGREE = 4096d / 360d;
	private static final double KA = .2 / MAX_ACCELERATION;

	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorControllerEnhanced intakeMaster = appCtx.getIntakePivotMaster();
	private AnalogPotentiometer pot = appCtx.getIntakePot();

	private Double setpoint = null;

	private PositionConverter converter = new IntakePositionConverter();
	private PidfControlLoop holdingPID = new PidfControlLoop(0.008, 0.0003, 0, 0, .5);
//		private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
//		private MotionProfileControlloop controller = new MotionProfileControlloop(.005, 0,
//				PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA, 1);
//
//		private TrapezoidalProfileManager profileManager = new TrapezoidalProfileManager(controller, profileConfig,
//				this::position, holdingPID);

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
		if (setpoint == null) {
			return;
		}

		if(RobotState.isEnabled()){
		    holdingPID.updateSetPoint(setpoint);
		    holdingPID.updatePID(AppConstants.Subsystems.DEFAULT_TIMESTEP);
		    intakeMaster.set(ControlMode.PercentOutput, holdingPID.getOutput());
        }

//		intakeMaster.set(ControlMode.PercentOutput, .3);

//		if(RobotState.isEnabled()){
//			profileManager.setPoint(setpoint);
//			profileManager.newOutput();
//			intakeMaster.set(ControlMode.PercentOutput, profileManager.getOutput());
//		}
        System.out.println("Output: " + holdingPID.getOutput() + " \t Position: " + position() + " \t Holding Err: "
                + holdingPID.getError());

	}

	private void zeroFromPot() {
		if (velocity() < 1 && velocity() > -1) {
			intakeMaster.setSelectedSensorPosition(converter.asTics(potPosition()), 0, 10);
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

}
