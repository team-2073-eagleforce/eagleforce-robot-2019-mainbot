package com.team2073.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.driveprofile.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;

public class DrivetrainSubsystem implements PeriodicRunnable {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();


    private IMotorControllerEnhanced leftMaster = appCtx.getLeftDriveMaster();
    private IMotorController leftSlave = appCtx.getLeftDriveSlave();
    private IMotorController leftSlave2 = appCtx.getLeftDriveSlave2();
    private IMotorControllerEnhanced rightMaster = appCtx.getRightDriveMaster();
    private IMotorController rightSlave = appCtx.getRightDriveSlave();
    private IMotorController rightSlave2 = appCtx.getRightDriveSlave2();

    private DoubleSolenoid shifter = appCtx.getDriveShiftSolenoid();

    private Joystick wheel = ApplicationContext.getInstance().getWheel();

    private CheesyDriveProfile cheesyDriveProfile = new CheesyDriveProfile();
    private EagleDriveProfile eagleDriveProfile = new EagleDriveProfile();
    private DriveProfile currentDriveProfile = cheesyDriveProfile;

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
//        driveProfileManager.registerProfile(tankDriveProfile);
//        driveProfileManager.registerProfile(eagleDriveProfile);
    }

    private void configMotors() {
        leftMaster.setInverted(directionalityProperties.isLeftDrivetrainMaster());
        leftSlave.setInverted(directionalityProperties.isLeftDrivetrainSlave());
        leftSlave2.setInverted(directionalityProperties.isLeftDrivetrainSlave2());
        leftSlave.follow(leftMaster);
        leftSlave2.follow(leftMaster);

        rightMaster.setInverted(directionalityProperties.isRightDrivetrainMaster());
        rightSlave.setInverted(directionalityProperties.isRightDrivetrainSlave());
        rightSlave2.setInverted(directionalityProperties.isRightDrivetrainSlave2());
        rightSlave.follow(rightMaster);
        rightSlave2.follow(rightMaster);

        leftMaster.configPeakOutputForward(1, 10);
        leftMaster.configPeakOutputReverse(-1, 10);
        rightMaster.configPeakOutputForward(1, 10);
        rightMaster.configPeakOutputReverse(-1, 10);

        rightMaster.setSensorPhase(true);
        leftMaster.setSensorPhase(true);

    }

    private DriveProfileManager driveProfileManager = ApplicationContext.getInstance().getDriveProfileManager();

    public void shiftDrivetrain(DoubleSolenoid.Value value){
        shifter.set(value);
    }

    @Override
    public void onPeriodic() {
//        System.out.println(currentDriveProfile);
        for (int i = 0; i < driveProfileManager.getDriveProfiles().size(); i++) {
            if (wheel.getRawButton(i + 2)) {
                currentDriveProfile = driveProfileManager.getDriveProfile(i);
            }
        }
        currentDriveProfile.setMotors();
    }

}