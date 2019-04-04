package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class IntakeOutForClimberCommand extends AbstractLoggingCommand {
    private ApplicationContext appctx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appctx.getMediator().elevatorGoal(ElevatorToPositionCommand.ElevatorHeight.CARGO_SHIP_BALL.getValue());
        appctx.getMediator().intakePivotGoal(IntakePivotCommand.IntakeSetpoint.VERTICAL.getValue());
    }

    @Override
    protected boolean isFinishedDelegate() {
        return (appctx.getIntakePivotSubsystem().position() > 100) && (appctx.getElevatorSubsystem().position() > 25);
    }
}
