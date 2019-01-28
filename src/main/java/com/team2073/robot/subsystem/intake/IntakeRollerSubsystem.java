package com.team2073.robot.subsystem.intake;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.SpeedController;

public class IntakeRollerSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private SpeedController shooterLeft = appCtx.getIntakeRoller();
	private SpeedController shooterRight = appCtx.getIntakeRoller2();

	public IntakeRollerSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}
}
