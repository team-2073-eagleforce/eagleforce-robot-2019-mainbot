package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class ZeroElevatorCommand extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getElevatorSubsystem().zeroElevator();
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
