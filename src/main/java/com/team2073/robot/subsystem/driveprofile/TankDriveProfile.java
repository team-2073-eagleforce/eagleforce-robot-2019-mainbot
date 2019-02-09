package com.team2073.robot.subsystem.driveprofile;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.Joystick;

public class TankDriveProfile implements DriveProfile {

    private IMotorControllerEnhanced leftMaster = ApplicationContext.getInstance().getLeftDriveMaster();
    private IMotorControllerEnhanced rightMaster = ApplicationContext.getInstance().getRightDriveMaster();

    public TankDriveProfile() {
        ApplicationContext.getInstance().getDriveProfileManager().registerProfile(this);
    }

    private Joystick controller = ApplicationContext.getInstance().getController();

    void move(double leftSide, double rightSide) {
        rightMaster.set(ControlMode.PercentOutput, -rightSide);
        leftMaster.set(ControlMode.PercentOutput, leftSide);
    }

    @Override
    public void setMotors() {
        move(controller.getRawAxis(1), controller.getRawAxis(5));

    }
}
