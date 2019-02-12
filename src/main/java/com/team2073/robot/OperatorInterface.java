package com.team2073.robot;

import com.team2073.robot.command.IntakePivotCommand;
import com.team2073.robot.command.intakeRoller.IntakeCommand;
import com.team2073.robot.command.intakeRoller.IntakeDisabledCommand;
import com.team2073.robot.command.intakeRoller.IntakeStopCommand;
import com.team2073.robot.command.intakeRoller.OutakeCommand;
import com.team2073.robot.command.shooter.HighShootCommand;
import com.team2073.robot.command.shooter.ShooterIntakeCommand;
import com.team2073.robot.command.shooter.ShooterStopCommand;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OperatorInterface {

	private static ApplicationContext appCtx = ApplicationContext.getInstance();

	private Joystick controller = appCtx.getController();
	private Joystick drivewheel = appCtx.getWheel();
	private Joystick driveStick = appCtx.getJoystick();


	public OperatorInterface() {
		JoystickButton a = new JoystickButton(controller, 1);
		JoystickButton b = new JoystickButton(controller, 2);
		JoystickButton lb = new JoystickButton(controller, 5);
		a.whenPressed(new IntakeCommand());
		a.whenPressed(new ShooterIntakeCommand());
		a.whenReleased(new IntakeStopCommand());
		a.whenReleased(new ShooterStopCommand());

		b.whenPressed(new HighShootCommand());
		b.whenReleased(new ShooterStopCommand());
		b.whenPressed(new OutakeCommand());
		b.whenReleased(new IntakeStopCommand());

		lb.whenPressed(new IntakePivotCommand(135d));



	}
}
