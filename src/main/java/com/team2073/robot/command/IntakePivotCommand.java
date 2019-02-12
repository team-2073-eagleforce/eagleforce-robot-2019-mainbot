package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import sun.awt.AppContext;

public class IntakePivotCommand extends AbstractLoggingCommand {
    private ApplicationContext appctx = ApplicationContext.getInstance();
    private double setpoint;

    public IntakePivotCommand(double setpoint){
        this.setpoint = setpoint;
    }

    @Override
    protected void initializeDelegate() {
        appctx.getIntakePivotSubsystem().set(setpoint);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
