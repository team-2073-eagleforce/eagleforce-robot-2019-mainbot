package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem;

public class IntakePivotCommand extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private IntakeSetpoint setpoint;


    public enum IntakeSetpoint {
        STORE(2.5d),
        INTAKE(169d),
        VERTICAL(140d);

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

        if(appCtx.getElevatorSubsystem().getCurrentState() != ElevatorSubsystem.ElevatorState.CLIMBING && setpoint == IntakeSetpoint.STORE){
            appCtx.getMediator().intakePivotGoal(setpoint.getValue());
        }else{
            appCtx.getIntakePivotSubsystem().set(IntakeSetpoint.VERTICAL.getValue());
        }
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
