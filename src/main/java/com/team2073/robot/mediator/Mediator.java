package com.team2073.robot.mediator;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;

public class Mediator implements PeriodicRunnable {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private final RobotContext robotCtx = RobotContext.getInstance();

	//	private IntakeRollerSubsystem intakeRoller = appCtx.getIntakeRollerSubsystem();
	private IntakePivotSubsystem intakePivot = appCtx.getIntakePivotSubsystem();
	private HatchManipulatorSubsystem hatch = appCtx.getHatchManipulatorSubsystem();
	private ShooterSubsystem shooter = appCtx.getShooterSubsystem();
	//	private RobotIntakeSubsystem robotIntake = appCtx.getRobotIntakeSubsystem();
	private ElevatorSubsystem elevator = appCtx.getElevatorSubsystem();
//	private DrivetrainSubsystem drivetrain = appCtx.getDrivetrainSubsystem();

	private static final double INTAKE_MINIMUM_CLEARING_POSITION = 100;
	private static final double MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT = 16.5;
	private static final double ELEVATOR_CLEARS_INTAKE = 20;/**/
	private static final double INTAKE_BELOW_CARRIAGE = 8;/**/
	private static final double ELEVATOR_SAFE_RANGE = 2;
	private static final double INTAKE_PIVOT_SAFE_RANGE = 5;


	private Double elevatorCachedSetpoint;
	private Double intakePivotCachedSetpoint;

	public Mediator() {
		autoRegisterWithPeriodicRunner();
	}

	//ELEVATOR
	public void elevatorGoal(double setpoint) {
		double adjustedSetpoint = checkElevatorSetpoint(setpoint);
		elevator.set(adjustedSetpoint);
	}

	public void intakePivotGoal(double setpoint) {
		double adjustedSetpoint = checkIntakePivotSetpoint(setpoint);
		intakePivot.set(adjustedSetpoint);
	}


	private void elevatorCheckPeriodic() {
		System.out.println("elevatorCached = " + elevatorCachedSetpoint);
		if (elevator.getSetpoint() != null) {
			if (intakePivot.position() < INTAKE_MINIMUM_CLEARING_POSITION
					&& intakePivot.position() > INTAKE_BELOW_CARRIAGE
					&& elevator.getSetpoint() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT
					&& elevator.position() > elevator.getSetpoint()
					&& elevatorCachedSetpoint == null) {
				elevatorCachedSetpoint = elevator.getSetpoint();
				elevator.set(MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT);
				intakePivot.set(closerBound(0, INTAKE_MINIMUM_CLEARING_POSITION, intakePivot.position()));
			} else if (elevatorCachedSetpoint != null) {
				elevator.set(elevatorCachedSetpoint);
				elevatorCachedSetpoint = null;
			}
		} else if (elevatorCachedSetpoint != null) {
			elevator.set(elevatorCachedSetpoint);
			elevatorCachedSetpoint = null;
		}
	}

	private void intakePivotCheckPeriodic() {
		if (intakePivot.getSetpoint() != null) {

			if (intakePivot.getSetpoint() < INTAKE_MINIMUM_CLEARING_POSITION
					&& intakePivot.position() > INTAKE_MINIMUM_CLEARING_POSITION
					&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
				if (intakePivotCachedSetpoint == null) {
					intakePivotCachedSetpoint = intakePivot.getSetpoint();
				}
				intakePivot.set(INTAKE_MINIMUM_CLEARING_POSITION);
				elevator.set(ELEVATOR_CLEARS_INTAKE);
			} else if (intakePivot.getSetpoint() >= INTAKE_BELOW_CARRIAGE
					&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
					&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

				if (intakePivotCachedSetpoint == null) {
					intakePivotCachedSetpoint = intakePivot.getSetpoint();
				}
				intakePivot.set(intakePivot.position());
				elevator.set(ELEVATOR_CLEARS_INTAKE);
			} else if (intakePivotCachedSetpoint != null) {

				intakePivot.set(intakePivotCachedSetpoint);
				intakePivotCachedSetpoint = null;
			}else{
			}
		} else if (intakePivotCachedSetpoint != null) {
			intakePivot.set(intakePivotCachedSetpoint);
			intakePivotCachedSetpoint = null;
		}
	}


	private double checkElevatorSetpoint(double setpoint) {
		double adjustedSetpoint = setpoint;
		if (intakePivot.position() < INTAKE_MINIMUM_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_BELOW_CARRIAGE
				&& setpoint < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT
				&& elevator.position() > MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
			adjustedSetpoint = ELEVATOR_CLEARS_INTAKE;
			elevatorCachedSetpoint = setpoint;
			intakePivot.set(closerBound(0, INTAKE_MINIMUM_CLEARING_POSITION, intakePivot.position()));
		}
//		CONSIDER PULLING INTAKES IN IF ELEVATOR IS ABOVE MIN HEIGHT
//		else if(elevator.position() > 30 ){
//			intakePivot.set(.5);
//		}

		return adjustedSetpoint;
	}

	private double checkIntakePivotSetpoint(double setpoint) {

		double adjustedSetpoint = setpoint;
		if (setpoint < INTAKE_MINIMUM_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_MINIMUM_CLEARING_POSITION
				&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

			adjustedSetpoint = INTAKE_MINIMUM_CLEARING_POSITION;
			intakePivotCachedSetpoint = setpoint;
			elevator.set(ELEVATOR_CLEARS_INTAKE);
		} else if (setpoint > INTAKE_BELOW_CARRIAGE
				&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
				&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
			adjustedSetpoint = intakePivot.position();
			intakePivotCachedSetpoint = setpoint;
			elevator.set(ELEVATOR_CLEARS_INTAKE);
		}

		return adjustedSetpoint;
	}

	@Override
	public void onPeriodic() {
		elevatorCheckPeriodic();
		intakePivotCheckPeriodic();
	}

	private double closerBound(double min, double max, double value) {
		if (Math.abs(min - value) <= Math.abs(max - value)) {
			return min;
		} else {
			return max;
		}

	}
}
