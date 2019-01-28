package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;

public class IntakeRollerCommand extends AbstractLoggingCommand {
	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private Mediator mediator = appCtx.getMediator();

	@Override
	protected void initializeDelegate() {
		mediator.intakeRollers(IntakeRollerSubsystem.IntakeRollerState.INTAKE_SPEED);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}
}
