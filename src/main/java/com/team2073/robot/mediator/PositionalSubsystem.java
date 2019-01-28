package com.team2073.robot.mediator;

public interface PositionalSubsystem {

	double position();
	double velocity();
	void set(Double setpoint);
}
