package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.HatchState;

public class HatchManipulatorCommand extends AbstractLoggingCommand {

    private ApplicationContext appctx = ApplicationContext.getInstance();
    private HatchManipulatorSubsystem hatch = appctx.getHatchManipulatorSubsystem();
    private final HatchState state;

    public HatchManipulatorCommand(HatchState state) {
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        hatch.set(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
