package com.team2073.robot.command.shooter;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;

public class HighShootCommand extends AbstractLoggingCommand{
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator = appCtx.getMediator();

    @Override
    protected void initializeDelegate() {
        appCtx.getShooterSubsystem().set(ShooterSubsystem.ShooterState.HIGH_SHOOT);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}