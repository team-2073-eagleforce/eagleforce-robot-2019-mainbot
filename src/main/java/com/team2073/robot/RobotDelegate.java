package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;

public class RobotDelegate extends AbstractRobotDelegate {

    private RobotContext robotCtx = RobotContext.getInstance();
    ApplicationContext appCtx = ApplicationContext.getInstance();
	private PropertyLoader loader = robotCtx.getPropertyLoader();
	Mediator mediator = appCtx.getMediator();


	public RobotDelegate(double period) {
		super(period);
	}
	
	@Override
	public void robotInit() {
		loader.autoRegisterAllPropContainers(getClass().getPackage().getName());
		robotCtx.getDataRecorder().disable();
	}

	@Override
	public void robotPeriodic() {
	}
}
