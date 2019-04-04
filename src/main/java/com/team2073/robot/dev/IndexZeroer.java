package com.team2073.robot.dev;

import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.position.converter.PositionConverter;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.concurrent.Callable;

public class IndexZeroer {
	private double[] positions;
	private Callable<Double> potPosition;
	private int lastCounter;
	private boolean zeroed = false;
	private Counter counter;
	private PositionConverter converter;
	private IMotorControllerEnhanced motorController;

	public IndexZeroer(IMotorControllerEnhanced motorController, PositionConverter converter, DigitalInput index, Callable<Double> potPosition, double... positions) {
		this.positions = positions;
		this.potPosition = potPosition;
		this.motorController = motorController;
		this.converter = converter;
		counter = new Counter(index);
	}

	public void attemptZero() {
		if (lastCounter < counter.get()) {
			try {
				double indexPos = findNearestPin();
				motorController.setSelectedSensorPosition(converter.asTics(indexPos), 0, 10);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			lastCounter = counter.get();
			zeroed = true;
		}

	}

	public boolean isZeroed() {
		return zeroed;
	}

	private double findNearestPin() throws Exception {
		double nearest = Integer.MAX_VALUE;
		double potPos = potPosition.call();
		for (double position : positions) {
			if (Math.abs(position - potPos) < nearest) {
				nearest = position;
			}

		}
		if (nearest == Integer.MAX_VALUE) {
			throw new Exception("NO NEAREST INDEX PIN VALUE");
		}
		return nearest;
	}


}
