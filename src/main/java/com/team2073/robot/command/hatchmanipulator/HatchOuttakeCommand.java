package com.team2073.robot.command.hatchmanipulator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.HatchState;


public class HatchOuttakeCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator = appCtx.getMediator();
    //my password is !Programmer

    @Override
    protected void initializeDelegate() {
        mediator.hatchManipulator(HatchState.RELEASE_HATCH);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
