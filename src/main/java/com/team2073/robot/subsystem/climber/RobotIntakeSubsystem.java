package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;

public class RobotIntakeSubsystem implements PeriodicRunnable, StateSubsystem<RobotIntakeState> {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
	private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();

	private RobotIntakeState state = RobotIntakeState.STORE;

	public RobotIntakeSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public RobotIntakeState currentState() {
		return state;
	}

	@Override
	public void set(RobotIntakeState goalState) {
		state = goalState;
	}

	@Override
	public void onPeriodic() {

	}

	public enum RobotIntakeState {
		STORE,
		DEPLOY_FORKS,
		OPEN_INTAKE,
		CLAMP;
	}
}
