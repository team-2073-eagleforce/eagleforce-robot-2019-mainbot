package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.periodic.PeriodicRunner;

public class DrivetrainSubsystem implements PeriodicRunnable {


	public DrivetrainSubsystem() {
		RobotContext.getInstance().getPeriodicRunner().register(this);
	}

	@Override
	public void onPeriodic() {

	}

}
