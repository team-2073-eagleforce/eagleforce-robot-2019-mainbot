package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotDelegate extends AbstractRobotDelegate {

    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private RobotContext robotCtx = RobotContext.getInstance();
    private Mediator mediator = appCtx.getMediator();
    private OperatorInterface oi;


    public RobotDelegate(double period) {
        super(period);
    }


    @Override
    public void robotInit() {
        robotCtx.getDataRecorder().disable();
        oi = new OperatorInterface();
    }

    @Override
    public void robotPeriodic() {
    }
}
