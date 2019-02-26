package com.team2073.robot.command.elevator;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class ElevatorToPositionCommand extends AbstractLoggingCommand {

	private ApplicationContext appCtx = ApplicationContext.getInstance();

	private ElevatorHeight height;

	public enum ElevatorHeight {
		BOTTOM(.5),
		DRIVE(31.25),
		LOW_BALL(13.5),
		LOW_HATCH(5d),
		LOW_DETERMINE(null),
		MID_BALL(41.5),
		MID_HATCH(33d),
		MID_DETERMINE(null),
		HIGH_BALL(70.5),
		HIGH_HATCH(61d),
		HIGH_DETERMINE(null);
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
				if(appCtx.getCargoSensor().get()){
					setpoint = ElevatorHeight.LOW_BALL.getValue();
				}else{
					setpoint = ElevatorHeight.LOW_HATCH.getValue();
				}
				break;
			case MID_DETERMINE:
				if(appCtx.getCargoSensor().get()){
					setpoint = ElevatorHeight.MID_BALL.getValue();
				}else{
					setpoint = ElevatorHeight.MID_HATCH.getValue();
				}
				break;
			case HIGH_DETERMINE:
				if(appCtx.getCargoSensor().get()){
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
