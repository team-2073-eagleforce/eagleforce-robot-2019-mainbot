package com.team2073.robot.subsystem.carriage;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorController shooterLeft = appCtx.getLeftShooter();
	private IMotorController shooterRight = appCtx.getRightShooter();
	private DigitalInput cargoSensor = appCtx.getCargoSensor();


	public ShooterSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}
}
