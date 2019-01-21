package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.periodic.PeriodicRunner;
import com.team2073.robot.ctx.ApplicationContext;

public class DrivetrainSubsystem implements PeriodicRunnable {


	private IMotorControllerEnhanced leftMaster = ApplicationContext.getInstance().getLeftDriveMaster();
	private IMotorController leftSlave = ApplicationContext.getInstance().getLeftDriveSlave();
	private IMotorController leftSlave2 = ApplicationContext.getInstance().getLeftDriveSlave2();
	private IMotorControllerEnhanced rightMaster = ApplicationContext.getInstance().getRightDriveMaster();
	private IMotorController rightSlave = ApplicationContext.getInstance().getRightDriveSlave();
	private IMotorController rightSlave2 = ApplicationContext.getInstance().getRightDriveSlave2();

	public DrivetrainSubsystem() {
		RobotContext.getInstance().getPeriodicRunner().register(this);

	}

	@Override
	public void onPeriodic() {

	}

}