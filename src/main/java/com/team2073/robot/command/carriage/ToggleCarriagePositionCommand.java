package com.team2073.robot.command.carriage;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class ToggleCarriagePositionCommand extends AbstractLoggingCommand {

	ApplicationContext appCtx = ApplicationContext.getInstance();

	@Override
	protected void initializeDelegate() {
		appCtx.getElevatorSubsystem().setCarriagePosition(DoubleSolenoid.Value.kForward);
	}

	@Override
	protected void executeDelegate() {
		if(appCtx.getElevatorSubsystem().position() < 4){
			appCtx.getElevatorSubsystem().setCarriagePosition(DoubleSolenoid.Value.kReverse);
		}
	}

	@Override
	protected void endDelegate() {
		appCtx.getElevatorSubsystem().setCarriagePosition(DoubleSolenoid.Value.kReverse);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return false;
	}
}
