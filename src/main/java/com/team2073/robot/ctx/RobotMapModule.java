package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.google.inject.AbstractModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import com.team2073.common.mediator.EagleSolenoid;
import com.team2073.common.periodic.PeriodicRunner;
import com.team2073.common.speedcontroller.EagleSPX;
import com.team2073.common.speedcontroller.EagleSRX;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.team2073.robot.AppConstants.Ports.*;

public class RobotMapModule extends AbstractModule {
	@Override
	protected void configure() {
		//ExampleArmSubsystem
		bindNamed(IMotorControllerEnhanced.class, "armMaster").toInstance(new EagleSRX(EXAMPLE_ARM_SUBSYSTEM_TALON, "armMaster", .5));
		bindNamed(IMotorController.class, "armSlave").toInstance(new EagleSPX(EXAMPLE_ARM_SUBSYSTEM_VICTOR, "armSlave", .5));
		bindNamed(DigitalInput.class, "armMagnetSensor").toInstance(new DigitalInput(EXAMPLE_ARM_HALL_EFFECT_SENSOR));

		//Drivetrain
		bindNamed(IMotorControllerEnhanced.class, "leftDrivetrainMotor").toInstance(new EagleSRX(DRIVE_LEFT_TALON, "leftDrivetrainMotor", .5));
		bindNamed(IMotorController.class, "slaveLeftDrivetrainMotor").toInstance(new EagleSPX(DRIVE_LEFT_VICTOR, "slaveLeftDrivetrainMotor", .5));
		bindNamed(IMotorControllerEnhanced.class, "rightDrivetrainMotor").toInstance(new EagleSRX(DRIVE_RIGHT_TALON, "rightDrivetrainMotor", .5));
		bindNamed(IMotorController.class, "slaveRightDrivetrainMotor").toInstance(new EagleSPX(DRIVE_RIGHT_VICTOR, "slaveRightDrivetrainMotor", .5));
		bind(PigeonIMU.class).toInstance(new PigeonIMU(PIGEON_GYRO));


		bind(PeriodicRunner.class).asEagerSingleton();
	}

	private <T> LinkedBindingBuilder<T> bindNamed(Class<T> clazz, String name) {
		return bind(clazz).annotatedWith(Names.named(name));
	}

}
