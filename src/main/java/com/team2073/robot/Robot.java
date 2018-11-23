package com.team2073.robot;

import com.team2073.common.robot.AbstractRobotDelegator;

public class Robot extends AbstractRobotDelegator {

	public Robot() {
		super(new RobotDelegate());
	}

}
