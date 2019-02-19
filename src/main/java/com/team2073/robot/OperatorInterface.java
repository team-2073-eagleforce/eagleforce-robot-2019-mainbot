package com.team2073.robot;

import com.team2073.robot.command.DriveShiftCommand;
import com.team2073.robot.command.IntakePivotCommand;
import com.team2073.robot.command.IntakeRollerCommand;
import com.team2073.robot.command.elevator.ElevatorShiftCommand;
import com.team2073.robot.command.elevator.ElevatorToPositionCommand;
import com.team2073.robot.command.elevator.ZeroElevatorCommand;
import com.team2073.robot.command.intakeRoller.IntakeStopCommand;
import com.team2073.robot.command.intakeRoller.OutakeCommand;
import com.team2073.robot.command.shooter.HighShootCommand;
import com.team2073.robot.command.shooter.ShooterIntakeCommand;
import com.team2073.robot.command.shooter.ShooterStopCommand;
import com.team2073.robot.ctx.ApplicationContext;
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

    public OperatorInterface() {
//        //DRIVE
//        stickFour.whenPressed(new DriveShiftCommand(DoubleSolenoid.Value.kForward));
//        stickFour.whenReleased(new DriveShiftCommand(DoubleSolenoid.Value.kReverse));
//
//        //Controller
//        dPadDown.whenPressed(new ElevatorToPositionCommand(0d));
//        dPadUp.whenPressed(new ElevatorToPositionCommand(70d));
//        dPadRight.whenPressed(new ElevatorToPositionCommand(35d));
//        controllerBack.whenPressed(new ZeroElevatorCommand());
//        a.whenPressed(new ShooterIntakeCommand());
//        a.whenPressed(new IntakeRollerCommand());
//        a.whenReleased(new ShooterStopCommand());
//        a.whenReleased(new IntakeStopCommand());
//        b.whenPressed(new HighShootCommand());
//        b.whenPressed(new OutakeCommand());
//        b.whenReleased(new ShooterStopCommand());
//        b.whenReleased(new ShooterStopCommand());
//        x.whenPressed(new IntakePivotCommand(130d));
//        y.whenPressed(new IntakePivotCommand(0d));
        a.whenPressed(new ElevatorToPositionCommand(40d));
        b.whenPressed(new ElevatorShiftCommand(DoubleSolenoid.Value.kReverse));
        b.whenReleased(new ElevatorShiftCommand(DoubleSolenoid.Value.kForward));
    }
}
