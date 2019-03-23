package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class BumpElevatorCommand extends AbstractLoggingCommand {

	private ApplicationContext appCtx = ApplicationContext.getInstance();

	private double bumpHeight;

	public BumpElevatorCommand(double bumpHeight) {
		this.bumpHeight = bumpHeight;
	}

	@Override
	protected void initializeDelegate() {
		appCtx.getMediator().elevatorGoal(appCtx.getElevatorSubsystem().position() + bumpHeight);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}
}
