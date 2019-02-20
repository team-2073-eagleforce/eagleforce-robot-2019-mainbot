package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;

public class ElevatorStateCommand extends AbstractLoggingCommand {

    private ApplicationContext appCtx = ApplicationContext.getInstance();

    private ElevatorState state;

    public ElevatorStateCommand(ElevatorState state){
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        appCtx.getElevatorSubsystem().setElevatorState(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
