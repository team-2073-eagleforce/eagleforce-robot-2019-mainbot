package com.team2073.robot;

import com.team2073.common.trigger.ControllerTriggerTrigger;
import com.team2073.common.trigger.MultiTrigger;
import com.team2073.robot.command.*;
import com.team2073.robot.command.carriage.ToggleCarriagePositionCommand;
import com.team2073.robot.command.carriage.ToggleHatchCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand.ElevatorHeight;
import com.team2073.robot.command.elevator.ZeroElevatorCommand;
import com.team2073.robot.command.carriage.CarriageCommand;
import com.team2073.robot.command.CameraLEDCommand;
import com.team2073.robot.command.intakeRoller.IntakeCommand;
import com.team2073.robot.command.intakeRoller.IntakeDisabledCommand;
import com.team2073.robot.command.intakeRoller.IntakeStopCommand;
import com.team2073.robot.command.triggers.CargoModeTrigger;
import com.team2073.robot.command.triggers.ElevatorStateTrigger;
import com.team2073.robot.command.triggers.InverseTrigger;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.CarriageSubsystem;
import com.team2073.robot.subsystem.CarriageSubsystem.CarriageState;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.Command;

public class OperatorInterface {

	private static ApplicationContext appCtx = ApplicationContext.getInstance();

	private Joystick controller = appCtx.getController();
	private Joystick driveWheel = appCtx.getWheel();
	private Joystick driveStick = appCtx.getJoystick();

	private JoystickButton a = new JoystickButton(controller, 1);
	private JoystickButton b = new JoystickButton(controller, 2);
	private JoystickButton x = new JoystickButton(controller, 3);
	private JoystickButton y = new JoystickButton(controller, 4);
	private JoystickButton lb = new JoystickButton(controller, 5);
	private JoystickButton rb = new JoystickButton(controller, 6);
	private JoystickButton controllerBack = new JoystickButton(controller, 7);
	private JoystickButton controllerStart = new JoystickButton(controller, 8);
	private POVButton dPadUp = new POVButton(controller, 0);
	private POVButton dPadRight = new POVButton(controller, 90);
	private POVButton dPadDown = new POVButton(controller, 180);
	private POVButton dPadLeft = new POVButton(controller, 270);
	private JoystickButton backTrigger = new JoystickButton(driveStick, 1);
	private JoystickButton stickTwo = new JoystickButton(driveStick, 2);
	private JoystickButton stickThree = new JoystickButton(driveStick, 3);
	private JoystickButton stickFour = new JoystickButton(driveStick, 4);
	private JoystickButton stickFive = new JoystickButton(driveStick, 5);

	private JoystickButton leftPaddle = new JoystickButton(driveWheel, 1);
	private JoystickButton wheelCircle = new JoystickButton(driveWheel, 2);
	private JoystickButton rightPaddle = new JoystickButton(driveWheel, 3);
	private JoystickButton wheelTriangle = new JoystickButton(driveWheel, 4);
	private ControllerTriggerTrigger leftTrigger = new ControllerTriggerTrigger(controller, 2);
	private ControllerTriggerTrigger rightTrigger = new ControllerTriggerTrigger(controller, 3);
	private MultiTrigger climbMode = new MultiTrigger(leftPaddle, rightPaddle);
	private ElevatorStateTrigger isClimbMode = new ElevatorStateTrigger(ElevatorState.CLIMBING);
	private MultiTrigger downDpadAndClimb = new MultiTrigger(isClimbMode, dPadDown);
	private MultiTrigger upDpadAndClimb = new MultiTrigger(isClimbMode, dPadUp);
	private CargoModeTrigger cargoModeTrigger = new CargoModeTrigger();
	private MultiTrigger cargoShoot = new MultiTrigger(cargoModeTrigger, stickThree);
	private MultiTrigger hatchShoot = new MultiTrigger(new InverseTrigger(cargoModeTrigger), stickThree);
	private MultiTrigger cargoIntake = new MultiTrigger(cargoModeTrigger, a);
	private MultiTrigger hatchIntake = new MultiTrigger(new InverseTrigger(cargoModeTrigger), a);


	public OperatorInterface() {
		//DRIVE
		stickFour.whenPressed(new DriveShiftCommand(DoubleSolenoid.Value.kForward));
		stickFour.whenReleased(new DriveShiftCommand(DoubleSolenoid.Value.kReverse));

//		Stick 3
		cargoShoot.whenActive(new CarriageCommand(CarriageState.CARGO_OUTTAKE));
		cargoShoot.whenInactive(new CarriageCommand(CarriageState.STOP));
		hatchShoot.whenActive(new CarriageCommand(CarriageState.HATCH_OUTTAKE));
		hatchShoot.whenInactive(new CarriageCommand(CarriageState.STOP));

		stickTwo.toggleWhenPressed(new CameraLEDCommand());

		//Controller
		dPadDown.whenPressed(new ElevatorToPositionCommand(ElevatorHeight.BOTTOM));
		dPadUp.whenPressed(new ElevatorToPositionCommand(ElevatorHeight.HIGH_DETERMINE));
		dPadRight.whenPressed(new ElevatorToPositionCommand(ElevatorHeight.MID_DETERMINE));
		dPadLeft.whenPressed(new ElevatorToPositionCommand(ElevatorHeight.LOW_DETERMINE));
//        downDpadAndClimb.whenActive(new RobotGrabberCommand(RobotIntakeState.CLAMP));
//        upDpadAndClimb.whenActive(new RobotGrabberCommand(RobotIntakeState.OPEN_INTAKE));
		controllerBack.whenPressed(new ZeroElevatorCommand());

		cargoIntake.whenActive(new CarriageCommand(CarriageState.CARGO_INTAKE));
		cargoIntake.whenInactive(new CarriageCommand(CarriageState.STOP));
		hatchIntake.whenActive(new CarriageCommand(CarriageState.HATCH_INTAKE));
		hatchIntake.whenInactive(new CarriageCommand(CarriageState.STOP));
		a.whenPressed(new IntakeCommand());
		a.whenReleased(new IntakeStopCommand());
		rightTrigger.toggleWhenActive(new ToggleHatchCommand());
		x.toggleWhenPressed(new ToggleCarriagePositionCommand());

        rb.whenPressed(new IntakePivotCommand(140d));
        lb.whenPressed(new IntakePivotCommand(105d));
		controllerStart.whenPressed(new IntakePivotCommand(5d));

//        climbMode.whenActive(new RobotGrabberCommand(RobotIntakeSubsystem.RobotIntakeState.DEPLOY_FORKS));
//        climbMode.whenActive(new RobotGrabberCommand(RobotIntakeSubsystem.RobotIntakeState.CLAMP));
//        climbMode.whenActive(new ElevatorStateCommand(ElevatorState.CLIMBING));
	}

}
