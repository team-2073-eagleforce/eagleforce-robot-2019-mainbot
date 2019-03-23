package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class FootDropCommand extends AbstractLoggingCommand {

	private ApplicationContext appCtx = ApplicationContext.getInstance();
	@Override
	protected void initializeDelegate() {
//		appCtx.getRobotIntakeSubsystem().dropFoot();
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}
}
