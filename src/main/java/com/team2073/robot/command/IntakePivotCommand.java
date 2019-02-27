package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.common.mediator.condition.PositionBasedCondition;
import com.team2073.common.mediator.request.Request;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import org.apache.commons.lang3.Range;

public class IntakePivotCommand extends AbstractLoggingCommand {
    private ApplicationContext appctx = ApplicationContext.getInstance();
    private double setpoint;

    public IntakePivotCommand(double setpoint){
        this.setpoint = setpoint;
    }

    @Override
    protected void initializeDelegate() {
//        appctx.getMediator().intakePivotGoal(setpoint);
        appctx.getCommonMediator().add(new Request(IntakePivotSubsystem.class,
                new PositionBasedCondition(setpoint, Range.between(setpoint - 1, setpoint + 1))));
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
