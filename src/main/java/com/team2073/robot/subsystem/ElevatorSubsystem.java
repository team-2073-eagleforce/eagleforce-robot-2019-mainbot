package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DigitalInput;

public class ElevatorSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorControllerEnhanced elevatorMaster = appCtx.getElevatorMaster();
	private IMotorController elevatorSlave1 = appCtx.getElevatorSlave();
	private IMotorController elevatorSlave2 = appCtx.getElevatorSlave2();

	private DigitalInput topLimit = appCtx.getElevatorTopLimit();
	private DigitalInput bottomLimit = appCtx.getElevatorBottomLimit();


	public ElevatorSubsystem() {
		autoRegisterWithPeriodicRunner();
		TalonUtil.resetTalon(elevatorMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(elevatorSlave1, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(elevatorSlave2, TalonUtil.ConfigurationType.SLAVE);
	}

	@Override
	public void onPeriodic() {

	}
}
