package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotDelegate extends AbstractRobotDelegate {

    private RobotContext robotCtx = RobotContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator;
    private OperatorInterface oi;


    public RobotDelegate(double period) {
        super(period);
    }


    @Override
    public void robotInit() {
        loader.autoRegisterAllPropContainers(getClass().getPackage().getName());
        loader.loadProperties();
        robotCtx.getDataRecorder().disable();
        oi = new OperatorInterface();
        mediator = appCtx.getMediator();
    }

    @Override
    public void robotPeriodic() {
    }
}
