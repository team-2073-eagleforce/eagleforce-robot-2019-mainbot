package com.team2073.robot;

import com.team2073.common.robot.RobotApplication;

public class Main {
	public static void main(String[] args) {
		RobotApplication.start(() -> new RobotDelegate(AppConstants.Subsystems.DEFAULT_TIMESTEP));
	}
}
