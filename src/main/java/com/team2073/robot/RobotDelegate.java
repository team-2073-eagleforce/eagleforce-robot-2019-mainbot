package com.team2073.robot;

import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotDelegate extends AbstractRobotDelegate {

	ApplicationContext appCtx = ApplicationContext.getInstance();
	Mediator mediator = appCtx.getMediator();


	public RobotDelegate(double period) {
		super(period);
	}
	
	@Override
	public void robotInit() {
	}

	@Override
	public void robotPeriodic() {
	}
}
