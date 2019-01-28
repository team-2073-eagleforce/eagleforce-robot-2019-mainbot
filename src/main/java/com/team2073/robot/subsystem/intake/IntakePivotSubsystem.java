package com.team2073.robot.subsystem.intake;

import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class IntakePivotSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();


	private IMotorControllerEnhanced armMaster = appCtx.getIntakePivotMaster();
	private AnalogPotentiometer pot = appCtx.getIntakePot();

	public IntakePivotSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}

	private double potToPosition(double potValue){
		return 0;
	}
}
