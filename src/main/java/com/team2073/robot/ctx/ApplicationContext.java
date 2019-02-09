package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.ExampleArmSubsystem;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;
import com.team2073.robot.subsystem.climber.WheelieBarSubsystem;
import com.team2073.robot.subsystem.driveprofile.DriveProfileManager;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import edu.wpi.first.wpilibj.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private static ApplicationContext instance;

    private Logger log = LoggerFactory.getLogger(getClass());

    /*UTIL */
    private DriveProfileManager driveProfileManager /*= new DriveProfileManager()*/;
    //	====================================================================================================================
    private Mediator mediator;
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
    //	====================================================================================================================
    /* VICTOR SPs */
    private SpeedController intakeRoller;
    private SpeedController intakeRoller2;
    //	====================================================================================================================
    /*SUBSYSTEMS*/
    private DrivetrainSubsystem drivetrainSubsystem;
    private ExampleArmSubsystem exampleArmSubsystem;
    private IntakeRollerSubsystem intakeRollerSubsystem;
    private IntakePivotSubsystem intakePivotSubsystem;
    private HatchManipulatorSubsystem hatchManipulatorSubsystem;
    private ShooterSubsystem shooterSubsystem;
    private RobotIntakeSubsystem robotIntakeSubsystem;
    private WheelieBarSubsystem wheelieBarSubsystem;
    private ElevatorSubsystem elevatorSubsystem;
    //	====================================================================================================================
    /*SOLENOIDS*/
    private DoubleSolenoid driveShiftSolenoid;
    private DoubleSolenoid elevatorShiftSolenoid;
    private DoubleSolenoid hatchPositionSolenoid;
    private DoubleSolenoid hatchPlaceSolenoid;
    private DoubleSolenoid forkDeploySolenoid;
    private DoubleSolenoid robotGrabSolenoid;
    //	====================================================================================================================
    /*JOYSTICKS*/
    private Joystick controller;
    private Joystick wheel;
    private Joystick joystick;
    //	====================================================================================================================
    /*SENSORS*/
    private DigitalInput cargoSensor;
    private DigitalInput elevatorTopLimit;
    private DigitalInput elevatorBottomLimit;
    private AnalogPotentiometer intakePot;
    private Ultrasonic hatchSensor;
    //	====================================================================================================================
    /*GETTERS*/

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Mediator getMediator() {
        if (mediator == null) {
            mediator = new Mediator();
        }
        return mediator;
    }

    public DriveProfileManager getDriveProfileManager() {
        if(driveProfileManager == null){
            driveProfileManager = new DriveProfileManager();
        }
        return driveProfileManager;
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

    public DrivetrainSubsystem getDrivetrainSubsystem() {
        if (drivetrainSubsystem == null) {
            drivetrainSubsystem = new DrivetrainSubsystem();
        }
        return drivetrainSubsystem;
    }

    public ExampleArmSubsystem getExampleArmSubsystem() {
        if (exampleArmSubsystem == null) {
            exampleArmSubsystem = new ExampleArmSubsystem();
        }
        return exampleArmSubsystem;
    }

    public IntakeRollerSubsystem getIntakeRollerSubsystem() {
        if (intakeRollerSubsystem == null) {
            intakeRollerSubsystem = new IntakeRollerSubsystem();
        }
        return intakeRollerSubsystem;
    }

    public IntakePivotSubsystem getIntakePivotSubsystem() {
        if (intakePivotSubsystem == null) {
            intakePivotSubsystem = new IntakePivotSubsystem();
        }
        return intakePivotSubsystem;
    }

    public HatchManipulatorSubsystem getHatchManipulatorSubsystem() {
        if (hatchManipulatorSubsystem == null) {
            hatchManipulatorSubsystem = new HatchManipulatorSubsystem();
        }
        return hatchManipulatorSubsystem;
    }

    public ShooterSubsystem getShooterSubsystem() {
        if (shooterSubsystem == null) {
            shooterSubsystem = new ShooterSubsystem();
        }
        return shooterSubsystem;
    }

    public RobotIntakeSubsystem getRobotIntakeSubsystem() {
        if (robotIntakeSubsystem == null) {
            robotIntakeSubsystem = new RobotIntakeSubsystem();
        }
        return robotIntakeSubsystem;
    }

    public WheelieBarSubsystem getWheelieBarSubsystem() {
        if (wheelieBarSubsystem == null) {
            wheelieBarSubsystem = new WheelieBarSubsystem();
        }
        return wheelieBarSubsystem;
    }

    public ElevatorSubsystem getElevatorSubsystem() {
        if (elevatorSubsystem == null) {
            intakePivotSubsystem = new IntakePivotSubsystem();
        }
        return elevatorSubsystem;
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
            leftDriveSlave = new VictorSPX(LEFT_DRIVE_VICTOR_PORT/*, LEFT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return leftDriveSlave;
    }

    public IMotorController getLeftDriveSlave2() {
        if (leftDriveSlave2 == null) {
            leftDriveSlave2 = new VictorSPX(LEFT_DRIVE_VICTOR_2_PORT/*, LEFT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
        }
        return leftDriveSlave2;
    }

    public IMotorController getRightDriveSlave() {
        if (rightDriveSlave == null) {
            rightDriveSlave = new VictorSPX(RIGHT_DRIVE_VICTOR_PORT/*, RIGHT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return rightDriveSlave;
    }

    public IMotorController getRightDriveSlave2() {
        if (rightDriveSlave2 == null) {
            rightDriveSlave2 = new VictorSPX(RIGHT_DRIVE_VICTOR_2_PORT/*, RIGHT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
        }
        return rightDriveSlave2;
    }

    public IMotorController getElevatorSlave() {
        if (elevatorSlave == null) {
            elevatorSlave = new VictorSPX(ELEVATOR_VICTOR_PORT/*, ELEVATOR_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return elevatorSlave;
    }

    public IMotorController getElevatorSlave2() {
        if (elevatorSlave2 == null) {
            elevatorSlave2 = new VictorSPX(ELEVATOR_VICTOR_2_PORT/*, ELEVATOR_SLAVE_2_NAME, SAFE_PERCENT*/);
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
            rightShooter = new TalonSRX(RIGHT_SHOOTER_VICTOR_PORT/*, RIGHT_SHOOTER_NAME, SAFE_PERCENT*/);
        }
        return rightShooter;
    }

    public SpeedController getIntakeRoller() {
        if (intakeRoller == null) {
            intakeRoller = new VictorSP(INTAKE_ROLLER_VICTOR_PORT/*, INTAKE_ROLLER_NAME, SAFE_PERCENT*/);
        }
        return intakeRoller;
    }

    public SpeedController getIntakeRoller2() {
        if (intakeRoller2 == null) {
            intakeRoller2 = new VictorSP(INTAKE_ROLLER_VICTOR_2_PORT/*, INTAKE_ROLLER_2_NAME, SAFE_PERCENT*/);
        }
        return intakeRoller2;
    }

    public DoubleSolenoid getDriveShiftSolenoid() {
        if (driveShiftSolenoid == null) {
            driveShiftSolenoid = new DoubleSolenoid(PCM_2_CAN_ID, DRIVE_SHIFT_LOW_PORT, DRIVE_SHIFT_HIGH_PORT);
        }
        return driveShiftSolenoid;
    }

    public DoubleSolenoid getElevatorShiftSolenoid() {
        if (elevatorShiftSolenoid == null) {
            elevatorShiftSolenoid = new DoubleSolenoid(PCM_2_CAN_ID, ELEVATOR_SHIFT_LOW_PORT, ELEVATOR_SHIFT_HIGH_PORT);
        }
        return elevatorShiftSolenoid;
    }

    public DoubleSolenoid getHatchPositionSolenoid() {
        if (hatchPositionSolenoid == null) {
            hatchPositionSolenoid = new DoubleSolenoid(PCM_2_CAN_ID, HATCH_UP_SOLENOID_PORT, HATCH_DOWN_SOLENOID_PORT);
        }
        return hatchPositionSolenoid;
    }

    public DoubleSolenoid getHatchPlaceSolenoid() {
        if (hatchPlaceSolenoid == null) {
            hatchPlaceSolenoid = new DoubleSolenoid(PCM_2_CAN_ID, HATCH_HOLD_SOLENOID_PORT, HATCH_RELEASE_SOLENOID_PORT);
        }
        return hatchPlaceSolenoid;
    }

    public DoubleSolenoid getForkDeploySolenoid() {
        if (forkDeploySolenoid == null) {
            forkDeploySolenoid = new DoubleSolenoid(PCM_1_CAN_ID, FORK_HOLD_SOLENOID_PORT, FORK_RELEASE_SOLENOID_PORT);
        }
        return forkDeploySolenoid;
    }

    public DoubleSolenoid getRobotGrabSolenoid() {
        if (robotGrabSolenoid == null) {
            robotGrabSolenoid = new DoubleSolenoid(PCM_1_CAN_ID, ROBOT_INTAKE_GRAB_SOLENOID_PORT, ROBOT_INTAKE_OPEN_SOLENOID_PORT);
        }
        return robotGrabSolenoid;
    }

    public DigitalInput getCargoSensor() {
        if (cargoSensor == null) {
            cargoSensor = new DigitalInput(CARGO_BANNER_DIO_PORT);
        }
        return cargoSensor;
    }

    public DigitalInput getElevatorTopLimit() {
        if (elevatorTopLimit == null) {
            elevatorTopLimit = new DigitalInput(ELEVATOR_TOP_LIMIT_DIO_PORT);
        }
        return elevatorTopLimit;
    }

    public DigitalInput getElevatorBottomLimit() {
        if (elevatorBottomLimit == null) {
            elevatorBottomLimit = new DigitalInput(ELEVATOR_BOTTOM_LIMIT_DIO_PORT);
        }
        return elevatorBottomLimit;
    }

    public AnalogPotentiometer getIntakePot() {
        if (intakePot == null) {
            intakePot = new AnalogPotentiometer(INTAKE_PIVOT_POTENTIOMETER_PORT);
        }
        return intakePot;
    }

    public Ultrasonic getHatchSensor() {
        if (hatchSensor == null) {
            hatchSensor = new Ultrasonic(HATCH_ULTRASONIC_TRIGGER_DIO_PORT, HATCH_ULTRASONIC_ECHO_DIO_PORT, Ultrasonic.Unit.kInches);
        }
        return hatchSensor;
    }
}
