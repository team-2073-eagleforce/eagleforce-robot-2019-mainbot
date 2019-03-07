package com.team2073.robot.subsystem.driveprofile;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.util.drive.CheesyDriveHelper;
import com.team2073.robot.util.drive.DriveSignal;
import edu.wpi.first.wpilibj.Joystick;

public class CheesyDriveProfile implements DriveProfile {

	private IMotorControllerEnhanced leftMaster = ApplicationContext.getInstance().getLeftDriveMaster();
	private IMotorControllerEnhanced rightMaster = ApplicationContext.getInstance().getRightDriveMaster();

	private Joystick wheel = ApplicationContext.getInstance().getWheel();
	private Joystick joystick = ApplicationContext.getInstance().getJoystick();

	public CheesyDriveProfile() {
		ApplicationContext.getInstance().getDriveProfileManager().registerProfile(this);
	}

	private CheesyDriveHelper cheesyDriveHelper = new CheesyDriveHelper();

	@Override
	public void setMotors() {
		DriveSignal driveSignal = cheesyDriveHelper.cheesyDrive(-joystick.getRawAxis(1), adjustWheel(wheel.getRawAxis(0)), wheel.getRawButton(1), joystick.getRawButton(3));

		leftMaster.set(ControlMode.PercentOutput, driveSignal.getLeft());
		rightMaster.set(ControlMode.PercentOutput, driveSignal.getRight());
	}

	private double adjustWheel(double rawJoystick) {
		if (rawJoystick < 0) {
			return Math.max(-1d, (rawJoystick * 140d) / 90d);
		} else {
			return Math.min(1d, (rawJoystick * 140d) / 90d);
		}

	}
}

