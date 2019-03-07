package com.team2073.robot.command.triggers;

import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class ElevatorHeightTrigger extends Trigger {
    private double position;
    private ApplicationContext appctx = ApplicationContext.getInstance();

    public ElevatorHeightTrigger(double position){
        this.position = position;
    }

    @Override
    public boolean get() {
        return appctx.getElevatorSubsystem().position() > position;
    }
}
