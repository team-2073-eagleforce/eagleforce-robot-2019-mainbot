package com.team2073.robot;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.team2073.common.controller.EagleController;
import com.team2073.common.controller.EagleJoystick;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import javax.annotation.PostConstruct;

import static com.team2073.robot.AppConstants.Context.OperatorInterface.*;

public class OperatorInterface {

	@Inject @Named(CONTROLLER)
	private Joystick controller;

	@Inject @Named(DRIVE_WHEEL)
	private Joystick drivewheel;

	@Inject @Named(DRIVE_STICK)
	private Joystick driveStick;

	@PostConstruct
	public void init() {
		JoystickButton a = new JoystickButton(controller, 1);
	}
}
