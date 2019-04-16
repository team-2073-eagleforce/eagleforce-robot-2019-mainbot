package com.team2073.robot.command.climb;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.StiltSubsystem;

public class StiltCommand extends AbstractLoggingCommand {
	private ApplicationContext appCtx = ApplicationContext.getInstance();

	@Override
	protected void initializeDelegate() {
		appCtx.getStiltSubsystem().set(StiltSubsystem.StiltState.DEPLOY);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}

}
