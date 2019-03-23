package com.team2073.robot.command.triggers;

import edu.wpi.first.wpilibj.buttons.Trigger;

import java.util.concurrent.Callable;

public class CompareTrigger extends Trigger {
	private double val1;
	private Callable<Double> val2;
	private Comparitor comparitor;

	public enum Comparitor {
		EQUAL_TO,
		LESS_THAN,
		GREATER_THAN,
		LESS_THAN_OR_EQUAL_TO,
		GREATER_THAN_OR_EQUAL_TO
	}

	public CompareTrigger(double val1, Callable<Double> val2, Comparitor comparitor) {
		this.comparitor = comparitor;
		this.val1 = val1;
		this.val2 = val2;
	}

	@Override
	public boolean get() {
		try {
			if(val2.call() == null){
				return false;
			}
			switch (comparitor) {
				case EQUAL_TO:
					return val2.call() == val1;
				case LESS_THAN:
					return val2.call() < val1;
				case GREATER_THAN:
					return val2.call() > val1;
				case LESS_THAN_OR_EQUAL_TO:
					return val2.call() <= val1;
				case GREATER_THAN_OR_EQUAL_TO:
					return val2.call() >= val1;
				default:
					return false;
			}
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		return false;

	}
}
