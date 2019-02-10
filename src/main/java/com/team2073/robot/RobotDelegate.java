package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotDelegate extends AbstractRobotDelegate {

	ApplicationContext appCtx = ApplicationContext.getInstance();
	RobotContext robotCtx = RobotContext.getInstance();
	Mediator mediator = appCtx.getMediator();


	public RobotDelegate(double period) {
		super(period);
	}
	
	@Override
	public void robotInit() {
		robotCtx.getDataRecorder().disable();
	}

	@Override
	public void robotPeriodic() {
	}
}
