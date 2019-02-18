package com.team2073.robot;

public abstract class AppConstants {

    public abstract class Context {
        public abstract class OperatorInterface {
            public static final String CONTROLLER = "controller";
            public static final String DRIVE_WHEEL = "wheel";
            public static final String DRIVE_STICK = "joystick";
        }

        public abstract class RobotMap {
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

    public abstract class Subsystems {
        public static final double DEFAULT_TIMESTEP = .01;
        public static final double MINIMUM_TIMESTEP = .005;

        public abstract class Hatch {
            public static final double HATCH_MARGIN_OF_ERROR = 6;
        }

        public abstract class Drivetrain {
            public static final double SENSE = .2;
            public static final double INVERSE = .3;
        }
    }

    public abstract class Defaults {
        public static final double SAFE_PERCENT = .25;
    }

    public abstract class Ports {

        //DS
        public static final int WHEEL_PORT = 0;
        public static final int JOYSTICK_PORT = 1;
        public static final int CONTROLLER_PORT = 2;
        // EXAMPLE
        public static final int EXAMPLE_ARM_SUBSYSTEM_TALON = -1;
        public static final int EXAMPLE_ARM_SUBSYSTEM_VICTOR = -1;
        public static final int EXAMPLE_ARM_HALL_EFFECT_SENSOR = -1;
    }

    public static class RobotPortsProperties {
        private int pigeonGyro;
        private int pdb;

        //DRIVE
        private int driveLeftTalonPort;
        private int driveRightTalonPort;
        private int leftDriveVictorPort;
        private int leftDriveVictor2Port;
        private int rightDriveVictorPort;
        private int rightDriveVictor2Port;
        //INTAKE
        private int intakeRollerVictorPort;
        private int intakeRollerVictor2Port;
        private int intakePivotTalonPort;
        private int intakePivotVictor;
        //SHOOTER
        private int leftShooterVictorPort;
        private int rightShooterVictorPort;
        //ELEVATOR
        private int elevatorTalonPort;
        private int elevatorVictorPort;
        private int elevatorVictor2Port;


        // PCM 2
        private int pcm2CanId;
        private int driveShiftLowPort;
        private int driveShiftHighPort;
        private int elevatorShiftLowPort;
        private int elevatorShiftHighPort;
        private int hatchUpSolenoidPort;
        private int hatchDownSolenoidPort;
        private int hatchHoldSolenoidPort;
        private int hatchReleaseSolenoidPort;

        // PCM 1
        private int pcm1CanId;
        private int forkReleaseSolenoidPort;
        private int forkHoldSolenoidPort;
        private int robotIntakeGrabSolenoidPort;
        private int robotIntakeOpenSolenoidPort;

        //DIO
        private int cargoBannerDioPort;
        private int hatchUltrasonicTriggerDioPort;
        private int hatchUltrasonicEchoDioPort;
        private int elevatorBottomLimitDioPort;
        private int elevatorTopLimitDioPort;

        public int getPigeonGyro() {
            return pigeonGyro;
        }

        public void setPigeonGyro(int pigeonGyro) {
            this.pigeonGyro = pigeonGyro;
        }

        public int getPdb() {
            return pdb;
        }

        public void setPdb(int pdb) {
            this.pdb = pdb;
        }

        public int getDriveLeftTalonPort() {
            return driveLeftTalonPort;
        }

        public void setDriveLeftTalonPort(int driveLeftTalonPort) {
            this.driveLeftTalonPort = driveLeftTalonPort;
        }

        public int getDriveRightTalonPort() {
            return driveRightTalonPort;
        }

        public void setDriveRightTalonPort(int driveRightTalonPort) {
            this.driveRightTalonPort = driveRightTalonPort;
        }

        public int getLeftDriveVictorPort() {
            return leftDriveVictorPort;
        }

        public void setLeftDriveVictorPort(int leftDriveVictorPort) {
            this.leftDriveVictorPort = leftDriveVictorPort;
        }

        public int getLeftDriveVictor2Port() {
            return leftDriveVictor2Port;
        }

        public void setLeftDriveVictor2Port(int leftDriveVictor2Port) {
            this.leftDriveVictor2Port = leftDriveVictor2Port;
        }

        public int getRightDriveVictorPort() {
            return rightDriveVictorPort;
        }

        public void setRightDriveVictorPort(int rightDriveVictorPort) {
            this.rightDriveVictorPort = rightDriveVictorPort;
        }

        public int getRightDriveVictor2Port() {
            return rightDriveVictor2Port;
        }

        public void setRightDriveVictor2Port(int rightDriveVictor2Port) {
            this.rightDriveVictor2Port = rightDriveVictor2Port;
        }

        public int getIntakeRollerVictorPort() {
            return intakeRollerVictorPort;
        }

        public void setIntakeRollerVictorPort(int intakeRollerVictorPort) {
            this.intakeRollerVictorPort = intakeRollerVictorPort;
        }

        public int getIntakeRollerVictor2Port() {
            return intakeRollerVictor2Port;
        }

        public void setIntakeRollerVictor2Port(int intakeRollerVictor2Port) {
            this.intakeRollerVictor2Port = intakeRollerVictor2Port;
        }

        public int getIntakePivotTalonPort() {
            return intakePivotTalonPort;
        }

        public void setIntakePivotTalonPort(int intakePivotTalonPort) {
            this.intakePivotTalonPort = intakePivotTalonPort;
        }

        public int getIntakePivotVictor() {
            return intakePivotVictor;
        }

        public void setIntakePivotVictor(int intakePivotVictor) {
            this.intakePivotVictor = intakePivotVictor;
        }

        public int getLeftShooterVictorPort() {
            return leftShooterVictorPort;
        }

        public void setLeftShooterVictorPort(int leftShooterVictorPort) {
            this.leftShooterVictorPort = leftShooterVictorPort;
        }

        public int getRightShooterVictorPort() {
            return rightShooterVictorPort;
        }

        public void setRightShooterVictorPort(int rightShooterVictorPort) {
            this.rightShooterVictorPort = rightShooterVictorPort;
        }

        public int getElevatorTalonPort() {
            return elevatorTalonPort;
        }

        public void setElevatorTalonPort(int elevatorTalonPort) {
            this.elevatorTalonPort = elevatorTalonPort;
        }

        public int getElevatorVictorPort() {
            return elevatorVictorPort;
        }

        public void setElevatorVictorPort(int elevatorVictorPort) {
            this.elevatorVictorPort = elevatorVictorPort;
        }

        public int getElevatorVictor2Port() {
            return elevatorVictor2Port;
        }

        public void setElevatorVictor2Port(int elevatorVictor2Port) {
            this.elevatorVictor2Port = elevatorVictor2Port;
        }

        public int getPcm2CanId() {
            return pcm2CanId;
        }

        public void setPcm2CanId(int pcm2CanId) {
            this.pcm2CanId = pcm2CanId;
        }

        public int getDriveShiftLowPort() {
            return driveShiftLowPort;
        }

        public void setDriveShiftLowPort(int driveShiftLowPort) {
            this.driveShiftLowPort = driveShiftLowPort;
        }

        public int getDriveShiftHighPort() {
            return driveShiftHighPort;
        }

        public void setDriveShiftHighPort(int driveShiftHighPort) {
            this.driveShiftHighPort = driveShiftHighPort;
        }

        public int getElevatorShiftLowPort() {
            return elevatorShiftLowPort;
        }

        public void setElevatorShiftLowPort(int elevatorShiftLowPort) {
            this.elevatorShiftLowPort = elevatorShiftLowPort;
        }

        public int getElevatorShiftHighPort() {
            return elevatorShiftHighPort;
        }

        public void setElevatorShiftHighPort(int elevatorShiftHighPort) {
            this.elevatorShiftHighPort = elevatorShiftHighPort;
        }

        public int getHatchUpSolenoidPort() {
            return hatchUpSolenoidPort;
        }

        public void setHatchUpSolenoidPort(int hatchUpSolenoidPort) {
            this.hatchUpSolenoidPort = hatchUpSolenoidPort;
        }

        public int getHatchDownSolenoidPort() {
            return hatchDownSolenoidPort;
        }

        public void setHatchDownSolenoidPort(int hatchDownSolenoidPort) {
            this.hatchDownSolenoidPort = hatchDownSolenoidPort;
        }

        public int getHatchHoldSolenoidPort() {
            return hatchHoldSolenoidPort;
        }

        public void setHatchHoldSolenoidPort(int hatchHoldSolenoidPort) {
            this.hatchHoldSolenoidPort = hatchHoldSolenoidPort;
        }

        public int getHatchReleaseSolenoidPort() {
            return hatchReleaseSolenoidPort;
        }

        public void setHatchReleaseSolenoidPort(int hatchReleaseSolenoidPort) {
            this.hatchReleaseSolenoidPort = hatchReleaseSolenoidPort;
        }

        public int getPcm1CanId() {
            return pcm1CanId;
        }

        public void setPcm1CanId(int pcm1CanId) {
            this.pcm1CanId = pcm1CanId;
        }

        public int getForkReleaseSolenoidPort() {
            return forkReleaseSolenoidPort;
        }

        public void setForkReleaseSolenoidPort(int forkReleaseSolenoidPort) {
            this.forkReleaseSolenoidPort = forkReleaseSolenoidPort;
        }

        public int getForkHoldSolenoidPort() {
            return forkHoldSolenoidPort;
        }

        public void setForkHoldSolenoidPort(int forkHoldSolenoidPort) {
            this.forkHoldSolenoidPort = forkHoldSolenoidPort;
        }

        public int getRobotIntakeGrabSolenoidPort() {
            return robotIntakeGrabSolenoidPort;
        }

        public void setRobotIntakeGrabSolenoidPort(int robotIntakeGrabSolenoidPort) {
            this.robotIntakeGrabSolenoidPort = robotIntakeGrabSolenoidPort;
        }

        public int getRobotIntakeOpenSolenoidPort() {
            return robotIntakeOpenSolenoidPort;
        }

        public void setRobotIntakeOpenSolenoidPort(int robotIntakeOpenSolenoidPort) {
            this.robotIntakeOpenSolenoidPort = robotIntakeOpenSolenoidPort;
        }

        public int getCargoBannerDioPort() {
            return cargoBannerDioPort;
        }

        public void setCargoBannerDioPort(int cargoBannerDioPort) {
            this.cargoBannerDioPort = cargoBannerDioPort;
        }

        public int getHatchUltrasonicTriggerDioPort() {
            return hatchUltrasonicTriggerDioPort;
        }

        public void setHatchUltrasonicTriggerDioPort(int hatchUltrasonicTriggerDioPort) {
            this.hatchUltrasonicTriggerDioPort = hatchUltrasonicTriggerDioPort;
        }

        public int getHatchUltrasonicEchoDioPort() {
            return hatchUltrasonicEchoDioPort;
        }

        public void setHatchUltrasonicEchoDioPort(int hatchUltrasonicEchoDioPort) {
            this.hatchUltrasonicEchoDioPort = hatchUltrasonicEchoDioPort;
        }

        public int getElevatorBottomLimitDioPort() {
            return elevatorBottomLimitDioPort;
        }

        public void setElevatorBottomLimitDioPort(int elevatorBottomLimitDioPort) {
            this.elevatorBottomLimitDioPort = elevatorBottomLimitDioPort;
        }

        public int getElevatorTopLimitDioPort() {
            return elevatorTopLimitDioPort;
        }

        public void setElevatorTopLimitDioPort(int elevatorTopLimitDioPort) {
            this.elevatorTopLimitDioPort = elevatorTopLimitDioPort;
        }

        public int getIntakePivotPotentiometerPort() {
            return intakePivotPotentiometerPort;
        }

        public void setIntakePivotPotentiometerPort(int intakePivotPotentiometerPort) {
            this.intakePivotPotentiometerPort = intakePivotPotentiometerPort;
        }

        //ANALOG
        private int intakePivotPotentiometerPort = 3;
    }


}
