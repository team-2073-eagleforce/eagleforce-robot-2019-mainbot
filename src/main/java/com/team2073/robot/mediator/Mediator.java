package com.team2073.robot.mediator;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.CarriageSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;

public class Mediator implements PeriodicRunnable {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private final RobotContext robotCtx = RobotContext.getInstance();

	private IntakeRollerSubsystem intakeRoller = appCtx.getIntakeRollerSubsystem();
	private IntakePivotSubsystem intakePivot = appCtx.getIntakePivotSubsystem();
	private CarriageSubsystem shooter = appCtx.getCarriageSubsystem();
	//		private RobotIntakeSubsystem robotIntake = appCtx.getRobotIntakeSubsystem();
	private ElevatorSubsystem elevator = appCtx.getElevatorSubsystem();
	private DrivetrainSubsystem drivetrain = appCtx.getDrivetrainSubsystem();

	private static final double INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION = 110;
	private static final double MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT = 20;
	private static final double ELEVATOR_CLEARS_INTAKE = 25;/**/
	private static final double INTAKE_BELOW_CARRIAGE = 8;/**/
	private static final double ELEVATOR_SAFE_RANGE = 2;
	private static final double INTAKE_PIVOT_SAFE_RANGE = 5;


	private Double elevatorCachedSetpoint;
	private Double intakePivotCachedSetpoint;

	private Double elevatorReturnPosition;
	private Double intakeReturnPosition;
	private Double elevatorGoalPosition;
	private Double intakeGoalPosition;

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

	private boolean intakeInGoalBound(double goal) {
		if (goal > INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION) {
			return intakePivot.position() > INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION;
		} else if (goal < INTAKE_BELOW_CARRIAGE) {
			return intakePivot.position() < INTAKE_BELOW_CARRIAGE;
		} else {
			System.out.println("ERROR, INVALID SETPOINT OF INTAKE");
			return false;
		}
	}

	private void elevatorCheckPeriodic() {
		if (elevator.getSetpoint() != null && (elevatorGoalPosition != null || elevatorReturnPosition != null)) {

			if (!(intakePivot.position() < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
					&& intakePivot.position() > INTAKE_BELOW_CARRIAGE)) {

				if (elevatorReturnPosition != null && intakeInGoalBound(intakeGoalPosition)) {
					elevator.set(elevatorReturnPosition);
					elevatorReturnPosition = null;
					intakeGoalPosition = null;
				}
			}

		}
	}

	private void intakePivotCheckPeriodic() {

		if (intakePivot.getSetpoint() != null && (intakeGoalPosition != null || intakeReturnPosition != null)) {

			if (elevator.position() > ELEVATOR_CLEARS_INTAKE) {

				if (intakeReturnPosition != null) {
					intakePivot.set(intakeReturnPosition);
					intakeReturnPosition = null;
					elevatorGoalPosition = null;
				}
			}

		}

//			if (intakePivot.getSetpoint() != null) {
//
//				if (intakePivot.getSetpoint() < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
//						&& intakePivot.position() > INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
//						&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
//					if (intakePivotCachedSetpoint == null) {
//						intakePivotCachedSetpoint = intakePivot.getSetpoint();
//					}
//					intakePivot.set(INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION);
//					elevator.set(ELEVATOR_CLEARS_INTAKE);
//				} else if (intakePivot.getSetpoint() >= INTAKE_BELOW_CARRIAGE
//						&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
//						&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
//
//					if (intakePivotCachedSetpoint == null) {
//						intakePivotCachedSetpoint = intakePivot.getSetpoint();
//					}
//					intakePivot.set(intakePivot.position());
//					elevator.set(ELEVATOR_CLEARS_INTAKE);
//				} else if (intakePivotCachedSetpoint != null) {
//
//					intakePivot.set(intakePivotCachedSetpoint);
//					intakePivotCachedSetpoint = null;
//				} else {
//				}
//			} else if (intakePivotCachedSetpoint != null) {
//				intakePivot.set(intakePivotCachedSetpoint);
//				intakePivotCachedSetpoint = null;
//			}
	}


	private double checkElevatorSetpoint(double setpoint) {
		double adjustedSetpoint = setpoint;

		if (setpoint < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT
				&& intakePivot.position() < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_BELOW_CARRIAGE
				&& elevator.position() > MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

			adjustedSetpoint = ELEVATOR_CLEARS_INTAKE;
			if(intakePivot.getSetpoint() != null)
				intakeReturnPosition = intakePivot.getSetpoint();
			else
				intakeReturnPosition = intakePivot.position();
			elevatorGoalPosition = setpoint;
			intakePivot.set(closerBound(INTAKE_BELOW_CARRIAGE - INTAKE_PIVOT_SAFE_RANGE, INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION + INTAKE_PIVOT_SAFE_RANGE, intakePivot.position()));
		}
//		CONSIDER PULLING INTAKES IN IF ELEVATOR IS ABOVE MIN HEIGHT
//		else if(elevator.position() > 30 ){
//			intakePivot.set(.5);
//		}

		return adjustedSetpoint;
	}

	private double checkIntakePivotSetpoint(double setpoint) {
		double adjustedSetpoint = setpoint;

		if (setpoint < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

			adjustedSetpoint = INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION;
			intakeGoalPosition = setpoint;
			if(elevator.getSetpoint() != null)
				elevatorReturnPosition = elevator.getSetpoint();
			else
				elevatorReturnPosition = elevator.position();
			elevator.set(ELEVATOR_CLEARS_INTAKE);
		} else if (setpoint > INTAKE_BELOW_CARRIAGE
				&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
				&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {
			adjustedSetpoint = intakePivot.position();
			intakeGoalPosition = setpoint;
			if(elevator.getSetpoint() != null)
				elevatorReturnPosition = elevator.getSetpoint();
			else
				elevatorReturnPosition = elevator.position();
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
