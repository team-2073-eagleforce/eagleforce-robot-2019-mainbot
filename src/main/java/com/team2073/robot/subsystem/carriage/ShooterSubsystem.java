package com.team2073.robot.subsystem.carriage;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterState;

public class ShooterSubsystem implements PeriodicRunnable, StateSubsystem<ShooterState> {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorController shooterLeft = appCtx.getLeftShooter();
	private IMotorController shooterRight = appCtx.getRightShooter();
	private DigitalInput cargoSensor = appCtx.getCargoSensor();

	private ShooterState state = ShooterState.STOP;

	public ShooterSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {
		if(cargoSensor.get()){set(ShooterState.STALL);}

		switch (currentState()){
			case INTAKE:
				shooterLeft.set(ControlMode.PercentOutput, ShooterState.INTAKE.getPercent());
				shooterRight.set(ControlMode.PercentOutput, ShooterState.INTAKE.getPercent());
				break;
			case HIGH_SHOOT:
				shooterLeft.set(ControlMode.PercentOutput, ShooterState.HIGH_SHOOT.getPercent());
				shooterRight.set(ControlMode.PercentOutput, ShooterState.HIGH_SHOOT.getPercent());
				break;
			case STALL:
				shooterLeft.set(ControlMode.PercentOutput, ShooterState.STALL.getPercent());
				shooterRight.set(ControlMode.PercentOutput, ShooterState.STALL.getPercent());
				break;
			case STOP:
				shooterLeft.set(ControlMode.PercentOutput, ShooterState.STOP.getPercent());
				shooterRight.set(ControlMode.PercentOutput, ShooterState.STOP.getPercent());
				break;
			case DISABLED:
				shooterLeft.set(ControlMode.PercentOutput, 0d);
				shooterRight.set(ControlMode.PercentOutput, 0d);
				break;
			default:
				break;
		}

	}

	@Override
	public ShooterState currentState() {
		return state;
	}

	@Override
	public void set(ShooterState goalState) {
		state = goalState;
	}

	public enum ShooterState {
		HIGH_SHOOT(0.9),
		INTAKE(-0.9),
		STOP(0d),
		STALL(-0.09),
		DISABLED(null);

		private Double percent;

		ShooterState(Double percent){this.percent = percent;}

		public Double getPercent(){return percent;}
	}
}
