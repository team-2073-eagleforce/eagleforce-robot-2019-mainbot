package com.team2073.robot.command.shooter;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;

public class ShooterIntakeCommand extends AbstractLoggingCommand{
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator = appCtx.getMediator();

    @Override
    protected void initializeDelegate() {
        appCtx.getShooterSubsystem().set(ShooterSubsystem.ShooterState.INTAKE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}