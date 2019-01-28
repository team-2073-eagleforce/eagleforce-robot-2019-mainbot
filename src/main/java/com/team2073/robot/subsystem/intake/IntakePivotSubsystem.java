package com.team2073.robot.subsystem.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class IntakePivotSubsystem implements PeriodicRunnable {
	private static final double POT_MIN_VALUE = 0;
	private static final double POT_MAX_VALUE = 1;
	private static final double MIN_POSITION = 0;
	private static final double MAX_POSITION = 160;
	private static final double MAX_VELOCITY = 300;
	private static final double PERCENT_FOR_MAX_VELOCITY = .333;
	private static final double MAX_ACCELERATION = 150;
	private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
	private static final double TICS_PER_DEGREE = 4096 / 360d;

	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorControllerEnhanced intakeMaster = appCtx.getIntakePivotMaster();
	private AnalogPotentiometer pot = appCtx.getIntakePot();

	private Double setpoint;

	private PositionConverter converter = new IntakePositionConverter();
	private PidfControlLoop holdingPID = new PidfControlLoop(.05, 0.0001, 0, 0, 1);
	private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
	private MotionProfileControlloop controller = new MotionProfileControlloop(.001, 0,
			PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, .2 / MAX_ACCELERATION, 1);

	private TrapezoidalProfileManager profileManager = new TrapezoidalProfileManager(controller, profileConfig,
			this::position, holdingPID);

	public IntakePivotSubsystem() {
		autoRegisterWithPeriodicRunner();
		TalonUtil.resetTalon(intakeMaster, TalonUtil.ConfigurationType.SENSOR);
		zeroFromPot();
	}

	public void set(Double setPoint) {
		this.setpoint = setPoint;

		profileManager.setPoint(setpoint);
		profileManager.newOutput();

		intakeMaster.set(ControlMode.PercentOutput, profileManager.getOutput());

	}


	@Override
	public void onPeriodic() {
		if (setpoint == null) {
			return;
		}

		profileManager.setPoint(setpoint);
		profileManager.newOutput();

		intakeMaster.set(ControlMode.PercentOutput, profileManager.getOutput());
	}

	private void zeroFromPot() {
		if (velocity() < 1 && velocity() > -1) {
			intakeMaster.setSelectedSensorPosition(converter.asTics(potPosition()), 0, 10);
		}
	}

	private double potPosition() {
		return (pot.get() - POT_MIN_VALUE) * (MAX_POSITION - MIN_POSITION) / (POT_MAX_VALUE - POT_MIN_VALUE) + MIN_POSITION;
	}

	private double position() {
		return converter.asPosition(intakeMaster.getSelectedSensorPosition(0));
	}

	private double velocity() {
		return converter.asPosition(intakeMaster.getSelectedSensorVelocity(0) * 10);
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
