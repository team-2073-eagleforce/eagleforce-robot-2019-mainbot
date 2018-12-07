package com.team2073.robot;

public abstract class AppConstants {
	public abstract class Subsystems{
		public static final double DEFAULT_TIMESTEP = .01;
		public static final double MINIMUM_TIMESTEP = .005;

	}
	public abstract class Ports {
		public static final int EXAMPLE_ARM_SUBSYSTEM_TALON = 0;
		public static final int EXAMPLE_ARM_SUBSYSTEM_VICTOR = 0;
		public static final int EXAMPLE_ARM_HALL_EFFECT_SENSOR = 0;

		public static final int DRIVE_LEFT_TALON = 1;
		public static final int DRIVE_RIGHT_TALON = 2;
		public static final int DRIVE_LEFT_VICTOR = 1;
		public static final int DRIVE_RIGHT_VICTOR = 2;

		public static final int PIGEON_GYRO = 0;
	}


}
