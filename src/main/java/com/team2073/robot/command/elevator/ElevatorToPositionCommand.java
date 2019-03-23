package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class ElevatorToPositionCommand extends AbstractLoggingCommand {

	private ApplicationContext appCtx = ApplicationContext.getInstance();

	private ElevatorHeight height;

	public enum ElevatorHeight {
		BOTTOM(.5),
		DRIVE(31.25),
		LOW_BALL(16d),
		LOW_HATCH(8d),
		LOW_DETERMINE(null),
		MID_BALL(45.5),
		MID_HATCH(37d),
		MID_DETERMINE(null),
		HIGH_BALL(71.5),
		HIGH_HATCH(65d),
		HIGH_DETERMINE(null),
		CARGO_SHIP_BALL(27.5);
		private Double height;

		ElevatorHeight(Double height) {
			this.height = height;
		}

		public Double getValue() {
			return height;
		}
	}

	public ElevatorToPositionCommand(ElevatorHeight height) {
		this.height = height;
	}

	@Override
	protected void initializeDelegate() {
		double setpoint;
		switch (height) {
			case LOW_DETERMINE:
				if(appCtx.getCarriageSubsystem().isCargoMode()){
					setpoint = ElevatorHeight.LOW_BALL.getValue();
				}else{
					setpoint = ElevatorHeight.LOW_HATCH.getValue();
				}
				break;
			case MID_DETERMINE:
				if(appCtx.getCarriageSubsystem().isCargoMode()){
					setpoint = ElevatorHeight.MID_BALL.getValue();
				}else{
					setpoint = ElevatorHeight.MID_HATCH.getValue();
				}
				break;
			case HIGH_DETERMINE:
				if(appCtx.getCarriageSubsystem().isCargoMode()){
					setpoint = ElevatorHeight.HIGH_BALL.getValue();
				}else{
					setpoint = ElevatorHeight.HIGH_HATCH.getValue();
				}
				break;
			default:
				setpoint = height.getValue();
				break;
		}
		appCtx.getMediator().elevatorGoal(setpoint);
	}

	@Override
	protected boolean isFinishedDelegate() {
		return true;
	}
}
