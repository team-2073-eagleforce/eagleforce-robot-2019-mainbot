package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ElevatorShiftCommand extends AbstractLoggingCommand {

    ApplicationContext appCtx = ApplicationContext.getInstance();
    private Value value;

    public ElevatorShiftCommand(Value value){
        this.value = value;
    }
    @Override
    protected void initializeDelegate() {
        appCtx.getElevatorSubsystem().setElevatorShifter(value);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
