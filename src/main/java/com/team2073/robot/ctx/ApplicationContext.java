package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ExampleArmSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {
	private static ApplicationContext instance;

	private Logger log = LoggerFactory.getLogger(getClass());

	/* TALONS */
	private IMotorControllerEnhanced leftDriveMaster;
	private IMotorControllerEnhanced rightDriveMaster;
	private IMotorControllerEnhanced elevatorMaster;
	private IMotorControllerEnhanced intakePivotMaster;
	//	====================================================================================================================
	/* VICTOR SPXs */
	private IMotorController leftDriveSlave;
	private IMotorController leftDriveSlave2;
	private IMotorController rightDriveSlave;
	private IMotorController rightDriveSlave2;
	private IMotorController elevatorSlave;
	private IMotorController elevatorSlave2;
	private IMotorController leftShooter;
	private IMotorController rightShooter;
	private IMotorController intakeRoller;
	private IMotorController intakeRoller2;
	//	====================================================================================================================
	/*SOLENOIDS*/
	private Solenoid driveShiftSolenoid;
	private Solenoid elevatorShiftSolenoid;
	private Solenoid hatchPositionSolenoid;
	private Solenoid hatchPlaceSolenoid;
	private Solenoid climbDeploySolenoid;
	private Solenoid robotGrabSolenoid;
	//	====================================================================================================================
	/*JOYSTICKS*/
	private Joystick controller;
	private Joystick wheel;
	private Joystick joystick;
	//	====================================================================================================================
	/*SUBSYSTEMS*/
	private DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
	private ExampleArmSubsystem exampleArmSubsystem = new ExampleArmSubsystem();
	//	====================================================================================================================
	/*GETTERS*/

	public static ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}

	public DrivetrainSubsystem getDrivetrainSubsystem() {
		return drivetrainSubsystem;
	}

	public ExampleArmSubsystem getExampleArmSubsystem() {
		return exampleArmSubsystem;
	}

	public Joystick getController() {
		if (controller == null) {
			controller = new Joystick(CONTROLLER_PORT);
		}
		return controller;
	}

	public Joystick getWheel() {
		if (wheel == null) {
			wheel = new Joystick(WHEEL_PORT);
		}
		return wheel;
	}

	public Joystick getJoystick() {
		if (joystick == null) {
			joystick = new Joystick(JOYSTICK_PORT);
		}
		return joystick;
	}

	public IMotorControllerEnhanced getLeftDriveMaster() {
		if (leftDriveMaster == null) {
			leftDriveMaster = new TalonSRX(DRIVE_LEFT_TALON_PORT/*, LEFT_DRIVETRAIN_MOTOR_NAME, SAFE_PERCENT*/);
		}
		return leftDriveMaster;
	}

	public IMotorControllerEnhanced getRightDriveMaster() {
		if (rightDriveMaster == null) {
			rightDriveMaster = new TalonSRX(DRIVE_RIGHT_TALON_PORT/*, RIGHT_DRIVETRAIN_MOTOR_NAME, SAFE_PERCENT*/);
		}
		return rightDriveMaster;
	}

	public IMotorControllerEnhanced getElevatorMaster() {
		if (elevatorMaster == null) {
			elevatorMaster = new TalonSRX(ELEVATOR_TALON_PORT/*, ELEVATOR_MASTER_NAME, SAFE_PERCENT*/);
		}
		return elevatorMaster;
	}

	public IMotorControllerEnhanced getIntakePivotMaster() {
		if (intakePivotMaster == null) {
			intakePivotMaster = new TalonSRX(INTAKE_PIVOT_TALON_PORT/*, INTAKE_PIVOT_MASTER_NAME, SAFE_PERCENT*/);
		}
		return intakePivotMaster;
	}

	public IMotorController getLeftDriveSlave() {
		if (leftDriveSlave == null) {
			leftDriveSlave = new TalonSRX(LEFT_DRIVE_VICTOR_PORT/*, LEFT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
		}
		return leftDriveSlave;
	}

	public IMotorController getLeftDriveSlave2() {
		if (leftDriveSlave2 == null) {
			leftDriveSlave2 = new TalonSRX(LEFT_DRIVE_VICTOR_2_PORT/*, LEFT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
		}
		return leftDriveSlave2;
	}

	public IMotorController getRightDriveSlave() {
		if (rightDriveSlave == null) {
			rightDriveSlave = new TalonSRX(RIGHT_DRIVE_VICTOR_PORT/*, RIGHT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
		}
		return rightDriveSlave;
	}

	public IMotorController getRightDriveSlave2() {
		if (rightDriveSlave2 == null) {
			rightDriveSlave2 = new TalonSRX(RIGHT_DRIVE_VICTOR_2_PORT/*, RIGHT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
		}
		return rightDriveSlave2;
	}

	public IMotorController getElevatorSlave() {
		if (elevatorSlave == null) {
			elevatorSlave = new TalonSRX(ELEVATOR_VICTOR_PORT/*, ELEVATOR_SLAVE_NAME, SAFE_PERCENT*/);
		}
		return elevatorSlave;
	}

	public IMotorController getElevatorSlave2() {
		if (elevatorSlave2 == null) {
			elevatorSlave2 = new TalonSRX(ELEVATOR_VICTOR_2_PORT/*, ELEVATOR_SLAVE_2_NAME, SAFE_PERCENT*/);
		}
		return elevatorSlave2;
	}

	public IMotorController getLeftShooter() {
		if (leftShooter == null) {
			leftShooter = new VictorSPX(LEFT_SHOOTER_VICTOR_PORT/*, LEFT_SHOOTER_NAME, SAFE_PERCENT*/);
		}
		return leftShooter;
	}

	public IMotorController getRightShooter() {
		if (rightShooter == null) {
			rightShooter = new VictorSPX(RIGHT_SHOOTER_VICTOR_PORT/*, RIGHT_SHOOTER_NAME, SAFE_PERCENT*/);
		}
		return rightShooter;
	}

	public IMotorController getIntakeRoller() {
		if (intakeRoller == null) {
			intakeRoller = new VictorSPX(INTAKE_ROLLER_VICTOR_PORT/*, INTAKE_ROLLER_NAME, SAFE_PERCENT*/);
		}
		return intakeRoller;
	}

	public IMotorController getIntakeRoller2() {
		if (intakeRoller2 == null) {
			intakeRoller2 = new VictorSPX(INTAKE_ROLLER_VICTOR_2_PORT/*, INTAKE_ROLLER_2_NAME, SAFE_PERCENT*/);
		}
		return intakeRoller2;
	}

	public Solenoid getDriveShiftSolenoid() {
		if (driveShiftSolenoid == null) {
			driveShiftSolenoid = new Solenoid(DRIVE_SHIFT_PORT);
		}
		return driveShiftSolenoid;
	}

	public Solenoid getElevatorShiftSolenoid() {
		if (elevatorShiftSolenoid == null) {
			elevatorShiftSolenoid = new Solenoid(ELEVATOR_SHIFT_PORT);
		}
		return elevatorShiftSolenoid;
	}

	public Solenoid getHatchPositionSolenoid() {
		if (hatchPositionSolenoid == null) {
			hatchPositionSolenoid = new Solenoid(HATCH_POSITION_SOLENOID_PORT);
		}
		return hatchPositionSolenoid;
	}

	public Solenoid getHatchPlaceSolenoid() {
		if (hatchPlaceSolenoid == null) {
			hatchPlaceSolenoid = new Solenoid(HATCH_GRAB_SOLENOID_PORT);
		}
		return hatchPlaceSolenoid;
	}

	public Solenoid getClimbDeploySolenoid() {
		if (climbDeploySolenoid == null) {
			climbDeploySolenoid = new Solenoid(CLIMBER_RELEASE_SOLENIOD_PORT);
		}
		return climbDeploySolenoid;
	}

	public Solenoid getRobotGrabSolenoid() {
		if (robotGrabSolenoid == null) {
			robotGrabSolenoid = new Solenoid(ROBOT_GRAB_SOLENOID_PORT);
		}
		return robotGrabSolenoid;
	}
}
