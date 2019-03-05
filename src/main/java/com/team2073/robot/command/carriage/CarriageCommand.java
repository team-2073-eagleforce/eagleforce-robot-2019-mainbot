package com.team2073.robot.command.carriage;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.CarriageSubsystem;
import com.team2073.robot.subsystem.CarriageSubsystem.CarriageState;

public class CarriageCommand extends AbstractLoggingCommand{
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private final CarriageState state;

    public CarriageCommand(CarriageState state){
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        appCtx.getCarriageSubsystem().set(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}