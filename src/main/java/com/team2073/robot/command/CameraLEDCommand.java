package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class CameraLEDCommand extends AbstractLoggingCommand {

    private ApplicationContext applicationContext = ApplicationContext.getInstance();

    @Override
    protected void initializeDelegate() {
        applicationContext.getCameraLED().set(false);
    }

    @Override
    protected void endDelegate() {
        applicationContext.getCameraLED().set(true);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
