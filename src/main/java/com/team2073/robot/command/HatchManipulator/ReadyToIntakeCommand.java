package com.team2073.robot.command.HatchManipulator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;

public class ReadyToIntakeCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator = appCtx.getMediator();

    @Override
    protected void initializeDelegate() {
        mediator.hatchManipulator(HatchManipulatorSubsystem.HatchState.READY_TO_INTAKE);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
