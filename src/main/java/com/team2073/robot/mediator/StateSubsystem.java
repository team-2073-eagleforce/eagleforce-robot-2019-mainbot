package com.team2073.robot.mediator;

public interface StateSubsystem<T extends Enum<T>> {

	T currentState();

	void set(T goalState);
}
