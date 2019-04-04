package com.team2073.robot.command.climb;

import com.team2073.robot.command.DriveShiftCommand;
import com.team2073.robot.command.IntakeOutForClimberCommand;
import com.team2073.robot.command.IntakePivotCommand;
import com.team2073.robot.command.elevator.ElevatorStateCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class FinishClimbCommandGroup extends CommandGroup {

	public FinishClimbCommandGroup() {
		addSequential(new ElevatorStateCommand(ElevatorSubsystem.ElevatorState.NORMAL_OPERATION));
		addSequential(new IntakePivotCommand(IntakePivotCommand.IntakeSetpoint.STORE));

	}

}
