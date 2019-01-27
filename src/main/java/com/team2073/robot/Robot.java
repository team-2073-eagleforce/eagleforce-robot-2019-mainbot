package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunner;
import com.team2073.common.robot.AbstractRobotDelegator;
import com.team2073.robot.ctx.ApplicationContext;

public class Robot extends AbstractRobotDelegator {

	private static ApplicationContext appCtx = ApplicationContext.getInstance();
	private static RobotContext robotCtx = RobotContext.getInstance();

	public Robot() {
		super(new RobotDelegate(), AppConstants.Subsystems.DEFAULT_TIMESTEP);
	}

}
