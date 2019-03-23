package com.team2073.robot.command;

import com.team2073.robot.command.FootDropCommand;
import com.team2073.robot.command.elevator.ElevatorStateCommand;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ClimbModeCommandGroup extends CommandGroup{
	public ClimbModeCommandGroup(){

		addSequential(new IntakeOutForClimberCommand());
//		addSequential(new FootDropCommand());
		addSequential(new ElevatorStateCommand(ElevatorSubsystem.ElevatorState.CLIMBING));
		addSequential(new WaitCommand(.1));
		addSequential(new RobotGrabberCommand(RobotIntakeSubsystem.RobotIntakeState.DEPLOY_FORKS));
	}
}
