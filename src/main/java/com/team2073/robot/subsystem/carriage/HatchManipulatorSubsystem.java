package com.team2073.robot.subsystem.carriage;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

public class HatchManipulatorSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private DoubleSolenoid hatchPosition = appCtx.getHatchPositionSolenoid();
	private DoubleSolenoid hatchPlace = appCtx.getHatchPlaceSolenoid();
	private Ultrasonic ultraSensor = appCtx.getHatchSensor();


	public HatchManipulatorSubsystem() {
		autoRegisterWithPeriodicRunner();
	}

	@Override
	public void onPeriodic() {

	}
}
