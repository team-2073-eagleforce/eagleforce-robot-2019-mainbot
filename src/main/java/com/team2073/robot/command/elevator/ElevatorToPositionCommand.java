package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class ElevatorToPositionCommand extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();

    private double setpoint;

    public ElevatorToPositionCommand(double setpoint){
        this.setpoint = setpoint;
    }

    @Override
    protected void initializeDelegate() {
        appCtx.getElevatorSubsystem().set(setpoint);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
