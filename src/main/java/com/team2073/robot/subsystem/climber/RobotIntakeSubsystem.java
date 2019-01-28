package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotIntakeSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
	private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();


	public RobotIntakeSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}
}
