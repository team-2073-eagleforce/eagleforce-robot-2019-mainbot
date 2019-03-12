package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class IntakePivotCommand extends AbstractLoggingCommand {
    private ApplicationContext appctx = ApplicationContext.getInstance();
    private IntakeSetpoint setpoint;


    public enum IntakeSetpoint {
        STORE(2.5d),
        INTAKE(140d),
        VERTICAL(111d);

        private Double position;

        IntakeSetpoint(Double position) {
            this.position = position;
        }

        public Double getValue() {
            return position;
        }
    }
    public IntakePivotCommand(IntakeSetpoint setpoint){
        this.setpoint = setpoint;
    }

    @Override
    protected void initializeDelegate() {
        appctx.getMediator().intakePivotGoal(setpoint.getValue());
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
