package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicAware;
import com.team2073.common.periodic.PeriodicRunner;

public class DrivetrainSubsystem implements PeriodicAware {


	public DrivetrainSubsystem() {
		RobotContext.getInstance().getPeriodicRunner().registerAsync(this, 10);
	}

	@Override
	public void onPeriodic() {

	}

}
