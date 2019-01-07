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
import com.team2073.robot.AppConstants.Subsystems;
import com.team2073.robot.ctx.RobotMapModule.DeviceNames;
import edu.wpi.first.wpilibj.DigitalInput;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

public class ExampleArmSubsystem implements PeriodicRunnable {
	// in degrees per second and degrees per second^2 respectively
	private static final double MAX_VELOCITY = 90;
	private static final double PERCENT_FOR_MAX_VELOCITY = .8;
	private static final double MAX_ACCELERATION = 180;
	private static final double TIME_STEP = Subsystems.DEFAULT_TIMESTEP;
	private static final double TICS_PER_DEGREE = 2048 / 360d;

	@Inject
	@Named(DeviceNames.ARM_MASTER)
	private IMotorControllerEnhanced armMaster;

	@Inject
	@Named(DeviceNames.ARM_SLAVE)
	private IMotorController armSlave;

	@Inject
	@Named(DeviceNames.ARM_MAGNET_SENSOR)
	private DigitalInput armMagnetSensor;

	private Double setpoint;

	private PositionConverter converter = new ArmPositionConverter();
	private PidfControlLoop holdingPID = new PidfControlLoop(.05, 0.0001, 0, 0, 1);
	private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, .01);
	private MotionProfileControlloop controller = new MotionProfileControlloop(.001, 0,
			PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, .2 / MAX_ACCELERATION, 1);
	private TrapezoidalProfileManager profileManager = new TrapezoidalProfileManager(controller, profileConfig,
			this::position, holdingPID);

	private Zeroer zeroer = new Zeroer(armMagnetSensor, armMaster, converter, "armZeroer");

	public ExampleArmSubsystem() {
		RobotContext.getInstance().getPeriodicRunner().register(this);

	}

	@PostConstruct
	public void initArmSubsystem() {
		TalonUtil.resetTalon(armMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(armSlave, TalonUtil.ConfigurationType.SLAVE);

		armSlave.follow(armMaster);
	}

	public void set(double angle) {
		this.setpoint = angle;
	}


	@Override
	public void onPeriodic() {
		zeroer.onPeriodic();
		if (setpoint == null) {
			return;
		}
		profileManager.setPoint(setpoint);
		profileManager.newOutput();

		armMaster.set(ControlMode.PercentOutput, profileManager.getOutput());

	}

	private double position() {
		return converter.asPosition(armMaster.getSelectedSensorPosition(0));
	}

	private static class ArmPositionConverter implements PositionConverter {
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
