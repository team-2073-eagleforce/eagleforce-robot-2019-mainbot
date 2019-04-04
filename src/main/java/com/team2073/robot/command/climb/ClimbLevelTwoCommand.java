package com.team2073.robot.command.climb;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class ClimbLevelTwoCommand extends AbstractLoggingCommand {
	private ApplicationContext appCtx = ApplicationContext.getInstance();

	@Override
	protected void initializeDelegate() {
		appCtx.setClimbHeight(13d);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}
}
