package com.team2073.robot.command.triggers;

import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.buttons.Trigger;

public class CarriageAmpedTrigger extends Trigger {
    private double amperage;
    private ApplicationContext appctx = ApplicationContext.getInstance();

    public CarriageAmpedTrigger(double amperage){
        this.amperage = amperage;
    }

    @Override
    public boolean get() {
        return appctx.getCarriageSubsystem().getAmperage() > amperage;
    }
}
