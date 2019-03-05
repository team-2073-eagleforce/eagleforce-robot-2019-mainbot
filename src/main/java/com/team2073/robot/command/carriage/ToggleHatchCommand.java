package com.team2073.robot.command.carriage;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.CarriageSubsystem.CarriageState;

public class ToggleHatchCommand extends AbstractLoggingCommand{
    private ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        appCtx.getCarriageSubsystem().set(CarriageState.HATCH_MODE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }

    @Override
    protected void endDelegate() {
        appCtx.getCarriageSubsystem().set(CarriageState.CARGO_MODE);
    }
}