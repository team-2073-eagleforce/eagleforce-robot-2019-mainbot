package com.team2073.robot.command.triggers;

import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class ElevatorStateTrigger extends Trigger {
    private ElevatorState state;
    private ApplicationContext appctx = ApplicationContext.getInstance();

    public ElevatorStateTrigger(ElevatorState state){
        this.state = state;
    }
    @Override
    public boolean get() {
        return appctx.getElevatorSubsystem().getCurrentState() == state;
    }
}
