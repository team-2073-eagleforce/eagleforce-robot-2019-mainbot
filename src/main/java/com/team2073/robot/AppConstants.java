package com.team2073.robot;

public abstract class AppConstants {

	public abstract class Context{
		public abstract class OperatorInterface{
			public static final String CONTROLLER = "controller";
			public static final String DRIVE_WHEEL = "wheel";
			public static final String DRIVE_STICK = "joystick";
		}

		public abstract class RobotMap{
			public static final String ARM_MASTER = "armMaster";
			public static final String ARM_SLAVE = "armSlave";
			public static final String ARM_MAGNET_SENSOR = "armMagnetSensor";

			public static final String LEFT_DRIVETRAIN_MOTOR_NAME = "leftDrivetrainMotor";
			public static final String LEFT_DRIVETRAIN_SLAVE_NAME = "slaveLeftDrivetrainMotor";
			public static final String LEFT_DRIVETRAIN_SLAVE_2_NAME = "slaveLeftDrivetrainMotor2";
			public static final String RIGHT_DRIVETRAIN_MOTOR_NAME = "rightDrivetrainMotor";
			public static final String RIGHT_DRIVETRAIN_SLAVE_NAME = "slaveRightDrivetrainMotor";
			public static final String RIGHT_DRIVETRAIN_SLAVE_2_NAME = "slaveRightDrivetrainMotor2";

			public static final String ELEVATOR_MASTER_NAME = "elevatorMotor";
			public static final String ELEVATOR_SLAVE_NAME = "slaveElevatorMotor";
			public static final String ELEVATOR_SLAVE_2_NAME = "slaveElevatorMotor2";

			public static final String INTAKE_PIVOT_MASTER_NAME = "intakePivotMotor";
			public static final String INTAKE_PIVOT_SLAVE = "slaveIntakePivotMotor";

			public static final String INTAKE_ROLLER_NAME = "intakeRollerMotor";
			public static final String INTAKE_ROLLER_2_NAME = "intakeRollerMotor2";

			public static final String LEFT_SHOOTER_NAME = "leftShooterMotor";
			public static final String RIGHT_SHOOTER_NAME = "rightShooterMotor";




		}
	}
	public abstract class Subsystems{
		public static final double DEFAULT_TIMESTEP = .01;
		public static final double MINIMUM_TIMESTEP = .005;

	}

	public abstract class Defaults{
		public static final double SAFE_PERCENT = .25;
	}
	public abstract class Ports {
		public static final int PIGEON_GYRO = 0;
		public static final int PDB = 0;

		public static final int WHEEL_PORT = 0;
		public static final int JOYSTICK_PORT = 1;
		public static final int CONTROLLER_PORT = 2;

		public static final int EXAMPLE_ARM_SUBSYSTEM_TALON = 0;
		public static final int EXAMPLE_ARM_SUBSYSTEM_VICTOR = 0;
		public static final int EXAMPLE_ARM_HALL_EFFECT_SENSOR = 0;

		public static final int DRIVE_LEFT_TALON_PORT = 1;
		public static final int DRIVE_RIGHT_TALON_PORT = 2;
		public static final int LEFT_DRIVE_VICTOR_PORT = 1;
		public static final int LEFT_DRIVE_VICTOR_2_PORT = 3;
		public static final int RIGHT_DRIVE_VICTOR_PORT = 2;
		public static final int RIGHT_DRIVE_VICTOR_2_PORT = 4;

		public static final int INTAKE_ROLLER_VICTOR_PORT = 5;
		public static final int INTAKE_ROLLER_VICTOR_2_PORT = 6;
		public static final int INTAKE_PIVOT_TALON_PORT = 3;
		public static final int INTAKE_PIVOT_VICTOR = 7;

		public static final int LEFT_SHOOTER_VICTOR_PORT = 8;
		public static final int RIGHT_SHOOTER_VICTOR_PORT = 9;

		public static final int ELEVATOR_TALON_PORT = 3;
		public static final int ELEVATOR_VICTOR_PORT = 10;
		public static final int ELEVATOR_VICTOR_2_PORT = 11;

		public static final int DRIVE_SHIFT_PORT = 0;
		public static final int ELEVATOR_SHIFT_PORT = 1;
		public static final int HATCH_POSITION_SOLENOID_PORT = 2;
		public static final int HATCH_GRAB_SOLENOID_PORT = 3;
		public static final int CLIMBER_RELEASE_SOLENIOD_PORT = 4;
		public static final int ROBOT_GRAB_SOLENOID_PORT = 5;




	}


}
