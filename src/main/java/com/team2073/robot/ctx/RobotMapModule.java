package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.google.inject.AbstractModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import com.team2073.common.periodic.PeriodicRunner;
import com.team2073.common.speedcontroller.EagleSPX;
import com.team2073.common.speedcontroller.EagleSRX;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.team2073.robot.AppConstants.Ports.*;
import static com.team2073.robot.ctx.RobotMapModule.DeviceNames.*;

public class RobotMapModule extends AbstractModule {
	public abstract class DeviceNames{
		public static final String ARM_MASTER = "armMaster";
		public static final String ARM_SLAVE = "armSlave";
		public static final String ARM_MAGNET_SENSOR = "armMagnetSensor";

		public static final String LEFT_DRIVETRAIN_MOTOR = "leftDrivetrainMotor";
		public static final String LEFT_DRIVETRAIN_SLAVE = "slaveLeftDrivetrainMotor";
		public static final String RIGHT_DRIVETRAIN_MOTOR = "rightDrivetrainMotor";
		public static final String RIGHT_DRIVETRAIN_SLAVE = "slaveRightDrivetrainMotor";

	}

	@Override
	protected void configure() {
		//ExampleArmSubsystem
		bindNamed(IMotorControllerEnhanced.class, ARM_MASTER).toInstance(new EagleSRX(EXAMPLE_ARM_SUBSYSTEM_TALON, "armMaster", .5));
		bindNamed(IMotorController.class,         ARM_SLAVE).toInstance(new EagleSPX(EXAMPLE_ARM_SUBSYSTEM_VICTOR, "armSlave", .5));
		bindNamed(DigitalInput.class,             ARM_MAGNET_SENSOR).toInstance(new DigitalInput(EXAMPLE_ARM_HALL_EFFECT_SENSOR));

		//Drivetrain
		bindNamed(IMotorControllerEnhanced.class, LEFT_DRIVETRAIN_MOTOR).toInstance(new EagleSRX(DRIVE_LEFT_TALON, "leftDrivetrainMotor", .5));
		bindNamed(IMotorController.class,         LEFT_DRIVETRAIN_SLAVE).toInstance(new EagleSPX(DRIVE_LEFT_VICTOR, "slaveLeftDrivetrainMotor", .5));
		bindNamed(IMotorControllerEnhanced.class, RIGHT_DRIVETRAIN_MOTOR).toInstance(new EagleSRX(DRIVE_RIGHT_TALON, "rightDrivetrainMotor", .5));
		bindNamed(IMotorController.class,         RIGHT_DRIVETRAIN_SLAVE).toInstance(new EagleSPX(DRIVE_RIGHT_VICTOR, "slaveRightDrivetrainMotor", .5));
		bind(PigeonIMU.class).toInstance(new PigeonIMU(PIGEON_GYRO));


		bind(PeriodicRunner.class).asEagerSingleton();
	}

	private <T> LinkedBindingBuilder<T> bindNamed(Class<T> clazz, String name) {
		return bind(clazz).annotatedWith(Names.named(name));
	}

}
