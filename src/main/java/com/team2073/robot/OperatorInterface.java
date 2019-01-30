package com.team2073.robot;

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
	}
}
