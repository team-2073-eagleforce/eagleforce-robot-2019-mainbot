package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.driveprofile.*;
import edu.wpi.first.wpilibj.Joystick;

public class DrivetrainSubsystem implements PeriodicRunnable {
	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();

	private IMotorControllerEnhanced leftMaster = appCtx.getLeftDriveMaster();
	private IMotorController leftSlave = appCtx.getLeftDriveSlave();
	private IMotorController leftSlave2 = appCtx.getLeftDriveSlave2();
	private IMotorControllerEnhanced rightMaster = appCtx.getRightDriveMaster();
	private IMotorController rightSlave = appCtx.getRightDriveSlave();
	private IMotorController rightSlave2 = appCtx.getRightDriveSlave2();
    private Joystick controller = ApplicationContext.getInstance().getController();

    private CheesyDriveProfile cheesyDriveProfile = new CheesyDriveProfile();
    private TankDriveProfile tankDriveProfile = new TankDriveProfile();
    private EagleDriveProfile eagleDriveProfile = new EagleDriveProfile();
    private DriveProfile currentDriveProfile = tankDriveProfile;

	public DrivetrainSubsystem() {
		autoRegisterWithPeriodicRunner();
		TalonUtil.resetTalon(leftMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetTalon(rightMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(leftSlave, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(leftSlave2, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(rightSlave, TalonUtil.ConfigurationType.SLAVE);
		TalonUtil.resetVictor(rightSlave2, TalonUtil.ConfigurationType.SLAVE);

		configMotors();

		driveProfileManager.registerProfile(cheesyDriveProfile);
		driveProfileManager.registerProfile(tankDriveProfile);
		driveProfileManager.registerProfile(eagleDriveProfile);
    }

	private void configMotors() {
		leftMaster.setInverted(false);
		rightMaster.setInverted(true);
//        rightSlave.follow(rightMaster);
//        leftSlave.follow(leftMaster);
		leftMaster.configPeakOutputForward(.5, 10);
		leftMaster.configPeakOutputReverse(-.5, 10);
		rightMaster.configPeakOutputForward(.5, 10);
		rightMaster.configPeakOutputReverse(-.5, 10);
	}

    private DriveProfileManager driveProfileManager = ApplicationContext.getInstance().getDriveProfileManager();

    @Override
    public void onPeriodic() {
        System.out.println(currentDriveProfile);
        for (int i = 0; i < driveProfileManager.getDriveProfiles().size(); i++) {
            if (controller.getRawButton(i + 1)) {
                currentDriveProfile = driveProfileManager.getDriveProfile(i);
            }
        }
        currentDriveProfile.setMotors();
    }
}