package com.team2073.robot.subsystem;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static com.team2073.robot.subsystem.StiltSubsystem.StiltState;
import static com.team2073.robot.subsystem.StiltSubsystem.StiltState.STORE;

public class StiltSubsystem implements StateSubsystem<StiltState>, PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid stiltSolenoid = appCtx.getEngageStiltSolenoid();

	private StiltState state = STORE;


	public StiltSubsystem(){
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public StiltState currentState() {
		return state;
	}

	@Override
	public void set(StiltState goalState) {
		this.state = goalState;
	}

	@Override
	public void onPeriodic() {
		switch (state) {
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

