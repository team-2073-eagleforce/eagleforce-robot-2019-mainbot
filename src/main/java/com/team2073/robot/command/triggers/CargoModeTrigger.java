package com.team2073.robot.command.triggers;

import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class CargoModeTrigger extends Trigger {
    private ApplicationContext appCtx = ApplicationContext.getInstance();

    @Override
    public boolean get() {
        return appCtx.getCarriageSubsystem().isCargoMode();
    }
}
