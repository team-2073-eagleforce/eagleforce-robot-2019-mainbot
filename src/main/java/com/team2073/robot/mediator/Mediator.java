package com.team2073.robot.mediator;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState;

import static com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.HatchState;
import static com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterState;
import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;

public class Mediator implements PeriodicRunnable {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private final RobotContext robotCtx = RobotContext.getInstance();

//	private IntakeRollerSubsystem intakeRoller = appCtx.getIntakeRollerSubsystem();
//	private IntakePivotSubsystem intakePivot = appCtx.getIntakePivotSubsystem();
	private HatchManipulatorSubsystem hatch = appCtx.getHatchManipulatorSubsystem();
//	private ShooterSubsystem shooter = appCtx.getShooterSubsystem();
//	private RobotIntakeSubsystem robotIntake = appCtx.getRobotIntakeSubsystem();
//	private ElevatorSubsystem elevator = appCtx.getElevatorSubsystem();
//	private DrivetrainSubsystem drivetrain = appCtx.getDrivetrainSubsystem();

	public Mediator() {
		autoRegisterWithPeriodicRunner();
	}

	// INTAKE ROLLERS
	public void intakeRollers(IntakeRollerState state) {

//		intakeRoller.set(state);
		checkIntakeRollersPeriodic();
	}

	private void checkIntakeRollersPeriodic() {
//		if (intakePivot.position() < 70) {
//			intakeRoller.set(IntakeRollerState.STOP);
//		}

	}

	// HATCH
	public void hatchManipulator(HatchState state) {
//		hatch.set(state);
	}

	//SHOOTER
	public void shooterSubsystem(ShooterState state) {
//		shooter.set(state);
	}

	//	ROBOT INTAKE
	public void robotIntake(RobotIntakeState state) {
//		robotIntake.set(state);
	}

	//ELEVATOR
	public void elevator(double setpoint) {
//		elevator.set(setpoint);
	}

	boolean set = false;
	@Override
	public void onPeriodic() {
//		checkIntakeRollersPeriodic();
		if(!set){
//			intakePivot.set(147.);
			set = true;
		}
	}
}
