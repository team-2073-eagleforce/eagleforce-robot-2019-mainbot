package com.team2073.robot.command.triggers;

import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class InverseTrigger extends Trigger {
    private Trigger trigger;
    private ApplicationContext appctx = ApplicationContext.getInstance();

    public InverseTrigger(Trigger trigger){
        this.trigger = trigger;
    }
    @Override
    public boolean get() {
        return !trigger.get();
    }
}
