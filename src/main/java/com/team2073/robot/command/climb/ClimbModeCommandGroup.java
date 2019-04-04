package com.team2073.robot.command.climb;

import com.team2073.robot.command.DriveShiftCommand;
import com.team2073.robot.command.IntakeOutForClimberCommand;
import com.team2073.robot.command.elevator.ElevatorStateCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ClimbModeCommandGroup extends CommandGroup{
	public ClimbModeCommandGroup(){

		addSequential(new IntakeOutForClimberCommand());
		addSequential(new DriveShiftCommand(DoubleSolenoid.Value.kForward));
		addSequential(new WaitCommand(.25));
		addSequential(new ElevatorToPositionCommand(20d));
		addSequential(new ElevatorStateCommand(ElevatorSubsystem.ElevatorState.CLIMBING));
	}
}
