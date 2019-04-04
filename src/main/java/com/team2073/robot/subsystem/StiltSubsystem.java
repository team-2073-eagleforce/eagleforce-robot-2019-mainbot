package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static com.team2073.robot.subsystem.StiltSubsystem.StiltState;

public class StiltSubsystem implements StateSubsystem<StiltState> {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid stiltSolenoid = appCtx.getEngageStiltSolenoid();

	private StiltState state = StiltState.STORE;

	@Override
	public StiltState currentState() {
		return state;
	}

	@Override
	public void set(StiltState goalState) {
		switch (goalState) {
			case STORE:
				stiltSolenoid.set(Value.kForward);
				break;
			case DEPLOY:
				stiltSolenoid.set(Value.kReverse);
				break;
		}
	}

	public enum StiltState {
		DEPLOY,
		STORE;
	}
}

