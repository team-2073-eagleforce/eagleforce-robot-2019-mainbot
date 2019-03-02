package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import static com.team2073.robot.subsystem.CarriageSubsystem.CarriageState;

public class CarriageSubsystem implements PeriodicRunnable, StateSubsystem<CarriageState> {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
	private ShooterProperties shooterProperties = applicationProperties.getShooterProperties();
	private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();

	private IMotorController shooterLeft = appCtx.getLeftShooter();
	private IMotorController shooterRight = appCtx.getRightShooter();
	private DigitalInput cargoSensor = appCtx.getCargoSensor();
	private DoubleSolenoid shooterPosition = appCtx.getShooterClampSolenoid();

	private CarriageState state = CarriageState.STOP;

	public CarriageSubsystem() {
		autoRegisterWithPeriodicRunner();
		shooterLeft.setInverted(directionalityProperties.isShooterLeft());
		shooterRight.setInverted(directionalityProperties.isShooterRight());
		shooterPosition.set(DoubleSolenoid.Value.kForward);
	}

	private void setPower(Double percent) {
		shooterLeft.set(ControlMode.PercentOutput, percent);
		shooterRight.set(ControlMode.PercentOutput, percent);
	}


	@Override
	public void onPeriodic() {
//		System.out.println("shooter position : " + shooterPosition.get() + "\t cargo sensor: " + cargoSensor.get());
		switch (state){
			case CARGO_MODE:
				shooterPosition.set(DoubleSolenoid.Value.kReverse);
				break;
			case HATCH_MODE:
				shooterPosition.set(DoubleSolenoid.Value.kForward);
				break;
		}

		if (!cargoSensor.get() &&
				!(state == CarriageState.CARGO_INTAKE || state == CarriageState.CARGO_OUTTAKE)
				&& !(state == CarriageState.HATCH_OUTTAKE || state == CarriageState.HATCH_INTAKE)) {

			if (shooterPosition.get() == DoubleSolenoid.Value.kForward) {
				set(CarriageState.HATCH_STALL);
			} else {
				set(CarriageState.CARGO_STALL);
			}
		}

		setPower(state.getPercent());
	}

	@Override
	public CarriageState currentState() {
		return state;
	}

	@Override
	public void set(CarriageState goalState) {
		state = goalState;
	}


	public enum CarriageState {
		CARGO_INTAKE(-.666),
		CARGO_OUTTAKE(.7),
		CARGO_STALL(-.15),
		HATCH_INTAKE(.9),
		HATCH_OUTTAKE(-.9),
		HATCH_STALL(.15),
		STOP(0d),
		HATCH_MODE(0d),
		CARGO_MODE(0d),
		DISABLED(0d);

		private Double percent;

		CarriageState(Double percent) {
			this.percent = percent;
		}

		public Double getPercent() {
			return percent;
		}
	}

	public static class ShooterProperties {
		private double highShootPercent;
		private double intakePercent;
		private double stallPercent;

		public double getHighShootPercent() {
			return highShootPercent;
		}

		public void setHighShootPercent(double highShootPercent) {
			this.highShootPercent = highShootPercent;
		}

		public double getIntakePercent() {
			return intakePercent;
		}

		public void setIntakePercent(double intakePercent) {
			this.intakePercent = intakePercent;
		}

		public double getStallPercent() {
			return stallPercent;
		}

		public void setStallPercent(double stallPercent) {
			this.stallPercent = stallPercent;
		}
	}

	public boolean isCargoMode(){
		return (shooterPosition.get() == DoubleSolenoid.Value.kReverse);
	}
}