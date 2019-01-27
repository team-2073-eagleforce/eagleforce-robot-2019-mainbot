package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ctx.ApplicationContext;

public class DrivetrainSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorControllerEnhanced leftMaster = appCtx.getLeftDriveMaster();
	private IMotorController leftSlave = appCtx.getLeftDriveSlave();
	private IMotorController leftSlave2 = appCtx.getLeftDriveSlave2();
	private IMotorControllerEnhanced rightMaster = appCtx.getRightDriveMaster();
	private IMotorController rightSlave = appCtx.getRightDriveSlave();
	private IMotorController rightSlave2 = appCtx.getRightDriveSlave2();


	public DrivetrainSubsystem() {
		autoRegisterWithPeriodicRunner();
		TalonUtil.resetTalon(leftMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetTalon(rightMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(leftSlave, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(leftSlave2, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(rightSlave, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(rightSlave2, TalonUtil.ConfigurationType.SLAVE);

	}

	@Override
	public void onPeriodic() {

	}

}