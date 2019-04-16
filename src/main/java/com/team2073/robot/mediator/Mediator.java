package com.team2073.robot.mediator;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.command.IntakePivotCommand.IntakeSetpoint;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.CarriageSubsystem;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.StiltSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;

import java.io.File;

public class Mediator implements PeriodicRunnable {
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private final RobotContext robotCtx = RobotContext.getInstance();

	private IntakeRollerSubsystem intakeRoller = appCtx.getIntakeRollerSubsystem();
	private IntakePivotSubsystem intakePivot = appCtx.getIntakePivotSubsystem();
	private CarriageSubsystem shooter = appCtx.getCarriageSubsystem();
	private StiltSubsystem stilt = appCtx.getStiltSubsystem();
	private ElevatorSubsystem elevator = appCtx.getElevatorSubsystem();
	private DrivetrainSubsystem drivetrain = appCtx.getDrivetrainSubsystem();

	private static final double INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION = 130;
	private static final double MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT = 20;
	private static final double ELEVATOR_CLEARS_INTAKE = 23.5;/**/
	private static final double INTAKE_BELOW_CARRIAGE = 15;/**/
	private static final double ELEVATOR_BOTTOM_SAFE_RANGE = 5;
	private static final double INTAKE_PIVOT_SAFE_RANGE = 5;

	private Double elevatorReturnPosition;
	private Double intakeReturnPosition;
	private Double elevatorGoalPosition;
	private Double intakeGoalPosition;

	private Double elevatorSetpoint;
	private Double intakeSetpoint;

	public Mediator() {
		autoRegisterWithPeriodicRunner();
	}

	//ELEVATOR
	public void elevatorGoal(double setpoint) {
		double adjustedSetpoint = checkElevatorSetpoint(setpoint);
		elevatorSetpoint = adjustedSetpoint;
		elevator.set(adjustedSetpoint);
	}

	public void intakePivotGoal(double setpoint) {
		double adjustedSetpoint = checkIntakePivotSetpoint(setpoint);
		intakeSetpoint = adjustedSetpoint;
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
					if (elevatorReturnPosition < ELEVATOR_BOTTOM_SAFE_RANGE) {

						elevatorSetpoint = ELEVATOR_BOTTOM_SAFE_RANGE;
						elevator.set(ELEVATOR_BOTTOM_SAFE_RANGE);
					} else {
						elevatorSetpoint = elevatorReturnPosition;
						elevator.set(elevatorReturnPosition);
					}
					elevatorReturnPosition = null;
					intakeGoalPosition = null;
				} else if (elevatorGoalPosition != null && !(elevatorGoalPosition < ELEVATOR_BOTTOM_SAFE_RANGE
						&& intakePivot.position() < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION)) {

					elevatorSetpoint = elevatorGoalPosition;
					elevator.set(elevatorGoalPosition);
				}
			}

		}
	}

	private void intakePivotCheckPeriodic() {

		if (intakePivot.getSetpoint() != null && (intakeGoalPosition != null || intakeReturnPosition != null)) {

			if (elevator.position() > MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

				if (intakeReturnPosition != null && (elevator.getSetpoint().equals(elevatorGoalPosition))) {
					intakePivot.set(intakeReturnPosition);
					intakeReturnPosition = null;
					elevatorGoalPosition = null;
				} else if (intakeGoalPosition != null) {
					intakePivot.set(intakeGoalPosition);
				}
			}

		}

	}


	private double checkElevatorSetpoint(double setpoint) {
		double adjustedSetpoint = setpoint;

		if (setpoint < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT
				&& intakePivot.position() < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_BELOW_CARRIAGE
				&& elevator.position() > MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

			adjustedSetpoint = ELEVATOR_CLEARS_INTAKE;
			if (intakePivot.getSetpoint() != null)
				intakeReturnPosition = intakePivot.getSetpoint();
			else
				intakeReturnPosition = intakePivot.position();
			elevatorGoalPosition = setpoint;
			intakePivot.set(closerBound(INTAKE_BELOW_CARRIAGE - INTAKE_PIVOT_SAFE_RANGE, INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION + INTAKE_PIVOT_SAFE_RANGE, intakePivot.position()));
		} else if (setpoint < ELEVATOR_BOTTOM_SAFE_RANGE
				&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
				&& elevator.position() > ELEVATOR_BOTTOM_SAFE_RANGE) {
			adjustedSetpoint = ELEVATOR_CLEARS_INTAKE;
			intakeReturnPosition = IntakeSetpoint.INTAKE.getValue();

			elevatorGoalPosition = setpoint;
			intakePivot.set(IntakeSetpoint.INTAKE.getValue());
		}

		return adjustedSetpoint;
	}

	private double checkIntakePivotSetpoint(double setpoint) {
		double adjustedSetpoint = setpoint;

		if (setpoint < INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& intakePivot.position() > INTAKE_MINIMUM_OUTSIDE_CLEARING_POSITION
				&& elevator.position() < MINIMUM_ELEVATOR_HEIGHT_TO_PIVOT) {

			adjustedSetpoint = intakePivot.position();
			intakeGoalPosition = setpoint;
			if (elevator.getSetpoint() != null)
				elevatorReturnPosition = elevator.getSetpoint();
			else{
//				elevatorReturnPosition = elevator.position();
				elevatorReturnPosition = null;
			}

			if(elevator.getSetpoint() < ELEVATOR_CLEARS_INTAKE ){
				elevatorSetpoint = ELEVATOR_CLEARS_INTAKE;
				elevator.set(ELEVATOR_CLEARS_INTAKE);
			}
		} else if (setpoint > INTAKE_BELOW_CARRIAGE
				&& intakePivot.position() < INTAKE_BELOW_CARRIAGE
				&& elevator.position() < ELEVATOR_CLEARS_INTAKE) {
			adjustedSetpoint = intakePivot.position();
			intakeGoalPosition = setpoint;
			if (elevator.getSetpoint() != null) {
				elevatorReturnPosition = elevator.getSetpoint();
			} else {
				elevatorReturnPosition = elevator.position();
			}
			elevatorSetpoint = ELEVATOR_CLEARS_INTAKE;
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

	public Double getElevatorReturnPosition() {
		return elevatorReturnPosition;
	}

	public Double getIntakeReturnPosition() {
		return intakeReturnPosition;
	}

	public Double getElevatorGoalPosition() {
		return elevatorGoalPosition;
	}

	public Double getIntakeGoalPosition() {
		return intakeGoalPosition;
	}
}
