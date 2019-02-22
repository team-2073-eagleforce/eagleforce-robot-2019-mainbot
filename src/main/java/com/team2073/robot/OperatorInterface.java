package com.team2073.robot;

import com.team2073.common.trigger.ControllerTriggerTrigger;
import com.team2073.common.trigger.MultiTrigger;
import com.team2073.robot.command.*;
import com.team2073.robot.command.elevator.ElevatorShiftCommand;
import com.team2073.robot.command.elevator.ElevatorStateCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.command.elevator.ZeroElevatorCommand;
import com.team2073.robot.command.intakeRoller.IntakeStopCommand;
import com.team2073.robot.command.intakeRoller.OutakeCommand;
import com.team2073.robot.command.shooter.HighShootCommand;
import com.team2073.robot.command.shooter.ShooterIntakeCommand;
import com.team2073.robot.command.shooter.ShooterStopCommand;
import com.team2073.robot.command.triggers.ElevatorStateTrigger;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorState;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.HatchState;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;

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
    private MultiTrigger downDpadAndClimb = new MultiTrigger( isClimbMode, dPadDown);
    private MultiTrigger upDpadAndClimb = new MultiTrigger( isClimbMode, dPadUp);

    public OperatorInterface() {
        //DRIVE
        stickFour.whenPressed(new DriveShiftCommand(DoubleSolenoid.Value.kForward));
        stickFour.whenReleased(new DriveShiftCommand(DoubleSolenoid.Value.kReverse));
        stickThree.whenPressed(new HighShootCommand());
        stickThree.whenReleased(new ShooterStopCommand());

        //Controller
        dPadDown.whenPressed(new ElevatorToPositionCommand(.5d));
        downDpadAndClimb.whenActive(new RobotGrabberCommand(RobotIntakeState.CLAMP));
        upDpadAndClimb.whenActive(new RobotGrabberCommand(RobotIntakeState.OPEN_INTAKE));
        dPadUp.whenPressed(new ElevatorToPositionCommand(70d));
        dPadRight.whenPressed(new ElevatorToPositionCommand(31.25d));
        dPadLeft.whenPressed(new ElevatorToPositionCommand(5d));
        controllerBack.whenPressed(new ZeroElevatorCommand());
        controllerStart.whenPressed(new IntakePivotCommand(10d));
        rightTrigger.whenActive(new ShooterIntakeCommand());
        rightTrigger.whenActive(new IntakeRollerCommand());
        rightTrigger.whenInactive(new ShooterStopCommand());
        rightTrigger.whenInactive(new IntakeStopCommand());
        leftTrigger.whenActive(new OutakeCommand());
        leftTrigger.whenInactive(new IntakeStopCommand());
        rb.whenPressed(new IntakePivotCommand(140d));
        lb.whenPressed(new IntakePivotCommand(100d));
        x.whenPressed(new HatchManipulatorCommand(HatchState.READY_TO_INTAKE));
        a.whenPressed(new HatchManipulatorCommand(HatchState.GRABED_HATCH));
        y.whenPressed(new HatchManipulatorCommand(HatchState.STARTING_CONFIG));

        climbMode.whenActive(new RobotGrabberCommand(RobotIntakeSubsystem.RobotIntakeState.DEPLOY_FORKS));
        climbMode.whenActive(new RobotGrabberCommand(RobotIntakeSubsystem.RobotIntakeState.CLAMP));
        climbMode.whenActive(new ElevatorStateCommand(ElevatorState.CLIMBING));
//        rightPaddle.whenPressed(new ElevatorShiftCommand(DoubleSolenoid.Value.kReverse));
//        rightPaddle.whenReleased(new ElevatorShiftCommand(DoubleSolenoid.Value.kForward));
    }
}
