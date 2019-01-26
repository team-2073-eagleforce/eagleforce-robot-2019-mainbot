package com.team2073.robot.subsystem.driveprofile;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.robot.AppConstants;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.Joystick;

public class EagleDriveProfile implements DriveProfile {

    private IMotorControllerEnhanced leftMaster = ApplicationContext.getInstance().getLeftDriveMaster();
    private IMotorControllerEnhanced rightMaster = ApplicationContext.getInstance().getRightDriveMaster();

    private Joystick wheel = ApplicationContext.getInstance().getWheel();
    private Joystick joystick = ApplicationContext.getInstance().getJoystick();

    public EagleDriveProfile() {
        ApplicationContext.getInstance().getDriveProfileManager().registerProfile(this);
    }

    private double adjustTurn(double turn) {
        return (turn * 160d) / 90;
    }

    private double turnSense(double ptart) {
        return AppConstants.Drivetrain.SENSE * Math.pow(ptart, 3) + ptart * (1 - AppConstants.Drivetrain.SENSE);
    }

    private double inverse(double start) {
        return (start) * AppConstants.Drivetrain.INVERSE + start;
    }

    private void move(double speed, double turn) {
        double leftSide = -(inverse(speed) - (inverse(speed) * turnSense(-turn)));
        double rightSide = inverse(speed) + inverse(speed) * turnSense(-turn);

        rightMaster.set(ControlMode.PercentOutput, -rightSide);
        leftMaster.set(ControlMode.PercentOutput, -leftSide);
    }

    private void pointTurn(double turn) {
        rightMaster.set(ControlMode.PercentOutput, turn);
        leftMaster.set(ControlMode.PercentOutput, turn);
    }

    @Override
    public void setMotors() {
        if (wheel.getRawButton(1)) {
            pointTurn(adjustTurn(wheel.getRawAxis(0)));
        } else {
            move(joystick.getRawAxis(1), adjustTurn(wheel.getRawAxis(0)));
        }

    }
}
