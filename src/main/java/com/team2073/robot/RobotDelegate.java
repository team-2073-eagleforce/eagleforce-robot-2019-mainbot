package com.team2073.robot;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mycila.guice.ext.closeable.CloseableModule;
import com.mycila.guice.ext.jsr250.Jsr250Module;
import com.team2073.common.periodic.PeriodicRunner;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.RobotMapModule;

public class RobotDelegate extends AbstractRobotDelegate {

    @Inject
    private PeriodicRunner periodicRunner;

    @Override
    public void robotInit() {
        Injector injector = Guice.createInjector(new RobotMapModule(), new CloseableModule(), new Jsr250Module());
        injector.getInstance(OperatorInterface.class);
        injector.injectMembers(this);
    }

    @Override
    public void robotPeriodic() {

    }
}
