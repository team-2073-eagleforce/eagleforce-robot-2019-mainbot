package com.team2073.robot.command.climb;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;

public class ClimbCommand extends AbstractLoggingCommand {
	private ApplicationContext appCtx = ApplicationContext.getInstance();
	private Mediator mediator = appCtx.getMediator();
	private IntakePivotSubsystem intake = appCtx.getIntakePivotSubsystem();
	private ElevatorSubsystem elevator = appCtx.getElevatorSubsystem();
	private IntakeRollerSubsystem intakeRoller = appCtx.getIntakeRollerSubsystem();

	@Override
	protected void initializeDelegate() {

	}

	@Override
	protected void executeDelegate() {
		intake.setIntakeToClimbHeight(appCtx.getClimbHeight(), elevator.position());
		intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.CLIMB_ROLL);
		elevator.setClimbPercent(-.65);
	}

	@Override
	protected void endDelegate() {
		elevator.setClimbPercent(0);
		intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.STOP);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return false;
	}
}
