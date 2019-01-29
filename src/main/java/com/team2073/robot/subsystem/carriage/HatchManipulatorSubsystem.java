package com.team2073.robot.subsystem.carriage;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

import static com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.*;

public class HatchManipulatorSubsystem implements PeriodicRunnable, StateSubsystem<HatchState> {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid hatchPosition = appCtx.getHatchPositionSolenoid();
	private DoubleSolenoid hatchPlace = appCtx.getHatchPlaceSolenoid();
	private Ultrasonic ultraSensor = appCtx.getHatchSensor();

	private HatchState state = HatchState.STARTING_CONFIG;

	@Override
	public HatchState currentState() {
		return state;
	}

	@Override
	public void set(HatchState goalState) {
		this.state = goalState;
	}

	public enum HatchState{
		STARTING_CONFIG,
		READY_TO_INTAKE,
		GRAB_HATCH;
	}


	public HatchManipulatorSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}
}
