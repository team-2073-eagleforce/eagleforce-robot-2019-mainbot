package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration;
import com.team2073.common.ctx.RobotContext;
import com.team2073.robot.AppConstants;
import com.team2073.robot.conf.ApplicationProperties;
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

import java.util.ArrayList;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

    private ApplicationProperties applicationProperties = RobotContext.getInstance().getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private AppConstants.RobotPortsProperties portProps = applicationProperties.getPortsProperties();

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
    private IMotorController intakePivotSlave;
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
            elevatorSubsystem = new ElevatorSubsystem();
        }
        return elevatorSubsystem;
    }

    public IMotorControllerEnhanced getLeftDriveMaster() {
        if (leftDriveMaster == null) {
            leftDriveMaster = new TalonSRX(portProps.getDriveLeftTalonPort()/*, LEFT_DRIVETRAIN_MOTOR_NAME, SAFE_PERCENT*/);
        }
        return leftDriveMaster;
    }

    public IMotorControllerEnhanced getRightDriveMaster() {
        if (rightDriveMaster == null) {
            rightDriveMaster = new TalonSRX(portProps.getDriveRightTalonPort()/*, RIGHT_DRIVETRAIN_MOTOR_NAME, SAFE_PERCENT*/);
        }
        return rightDriveMaster;
    }

    public IMotorControllerEnhanced getElevatorMaster() {
        if (elevatorMaster == null) {
            elevatorMaster = new TalonSRX(portProps.getElevatorTalonPort()/*, ELEVATOR_MASTER_NAME, SAFE_PERCENT*/);
        }
        return elevatorMaster;
    }

    public IMotorControllerEnhanced getIntakePivotMaster() {
        if (intakePivotMaster == null) {
            intakePivotMaster = new TalonSRX(portProps.getIntakePivotTalonPort()/*, INTAKE_PIVOT_MASTER_NAME, SAFE_PERCENT*/);
        }
        return intakePivotMaster;
    }

    public IMotorController getIntakePivotSlave(){
        if(intakePivotSlave == null){
            intakePivotSlave = new VictorSPX(portProps.getIntakePivotVictor());
        }

        return intakePivotSlave;
    }

    public IMotorController getLeftDriveSlave() {
        if (leftDriveSlave == null) {
            leftDriveSlave = new VictorSPX(portProps.getLeftDriveVictorPort()/*, LEFT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return leftDriveSlave;
    }

    public IMotorController getLeftDriveSlave2() {
        if (leftDriveSlave2 == null) {
            leftDriveSlave2 = new VictorSPX(portProps.getLeftDriveVictor2Port()/*, LEFT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
        }
        return leftDriveSlave2;
    }

    public IMotorController getRightDriveSlave() {
        if (rightDriveSlave == null) {
            rightDriveSlave = new VictorSPX(portProps.getRightDriveVictorPort()/*, RIGHT_DRIVETRAIN_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return rightDriveSlave;
    }

    public IMotorController getRightDriveSlave2() {
        if (rightDriveSlave2 == null) {
            rightDriveSlave2 = new VictorSPX(portProps.getRightDriveVictor2Port()/*, RIGHT_DRIVETRAIN_SLAVE_2_NAME, SAFE_PERCENT*/);
        }
        return rightDriveSlave2;
    }

    public IMotorController getElevatorSlave() {
        if (elevatorSlave == null) {
            elevatorSlave = new VictorSPX(portProps.getElevatorVictorPort()/*, ELEVATOR_SLAVE_NAME, SAFE_PERCENT*/);
        }
        return elevatorSlave;
    }

    public IMotorController getElevatorSlave2() {
        if (elevatorSlave2 == null) {
            elevatorSlave2 = new VictorSPX(portProps.getElevatorVictor2Port()/*, ELEVATOR_SLAVE_2_NAME, SAFE_PERCENT*/);
        }
        return elevatorSlave2;
    }

    public IMotorController getLeftShooter() {
        if (leftShooter == null) {
            leftShooter = new VictorSPX(portProps.getLeftShooterVictorPort()/*, LEFT_SHOOTER_NAME, SAFE_PERCENT*/);
        }
        return leftShooter;
    }

    public IMotorController getRightShooter() {
        if (rightShooter == null) {
            rightShooter = new TalonSRX(portProps.getRightShooterVictorPort()/*, RIGHT_SHOOTER_NAME, SAFE_PERCENT*/);
        }
        return rightShooter;
    }

    public SpeedController getIntakeRoller() {
        if (intakeRoller == null) {
            intakeRoller = new VictorSP(portProps.getIntakeRollerVictorPort()/*, INTAKE_ROLLER_NAME, SAFE_PERCENT*/);
        }
        return intakeRoller;
    }

    public SpeedController getIntakeRoller2() {
        if (intakeRoller2 == null) {
            intakeRoller2 = new VictorSP(portProps.getIntakeRollerVictor2Port()/*, INTAKE_ROLLER_2_NAME, SAFE_PERCENT*/);
        }
        return intakeRoller2;
    }

    public DoubleSolenoid getDriveShiftSolenoid() {
        if (driveShiftSolenoid == null) {
            driveShiftSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getDriveShiftLowPort(), portProps.getDriveShiftHighPort());
        }
        return driveShiftSolenoid;
    }

    public DoubleSolenoid getElevatorShiftSolenoid() {
        if (elevatorShiftSolenoid == null) {
            elevatorShiftSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getElevatorShiftLowPort(), portProps.getElevatorShiftHighPort());
        }
        return elevatorShiftSolenoid;
    }

    public DoubleSolenoid getHatchPositionSolenoid() {
        if (hatchPositionSolenoid == null) {
            hatchPositionSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getHatchUpSolenoidPort(), portProps.getHatchDownSolenoidPort());
        }
        return hatchPositionSolenoid;
    }

    public DoubleSolenoid getHatchPlaceSolenoid() {
        if (hatchPlaceSolenoid == null) {
            hatchPlaceSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getHatchHoldSolenoidPort(), portProps.getHatchReleaseSolenoidPort());
        }
        return hatchPlaceSolenoid;
    }

    public DoubleSolenoid getForkDeploySolenoid() {
        if (forkDeploySolenoid == null) {
            forkDeploySolenoid = new DoubleSolenoid(portProps.getPcm1CanId(), portProps.getForkHoldSolenoidPort(), portProps.getForkReleaseSolenoidPort());
        }
        return forkDeploySolenoid;
    }

    public DoubleSolenoid getRobotGrabSolenoid() {
        if (robotGrabSolenoid == null) {
            robotGrabSolenoid = new DoubleSolenoid(portProps.getPcm1CanId(), portProps.getRobotIntakeGrabSolenoidPort(), portProps.getRobotIntakeOpenSolenoidPort());
        }
        return robotGrabSolenoid;
    }

    public DigitalInput getCargoSensor() {
        if (cargoSensor == null) {
            cargoSensor = new DigitalInput(portProps.getCargoBannerDioPort());
        }
        return cargoSensor;
    }

    public DigitalInput getElevatorTopLimit() {
        if (elevatorTopLimit == null) {
            elevatorTopLimit = new DigitalInput(portProps.getElevatorTopLimitDioPort());
        }
        return elevatorTopLimit;
    }

    public DigitalInput getElevatorBottomLimit() {
        if (elevatorBottomLimit == null) {
            elevatorBottomLimit = new DigitalInput(portProps.getElevatorBottomLimitDioPort());
        }
        return elevatorBottomLimit;
    }

    public AnalogPotentiometer getIntakePot() {
        if (intakePot == null) {
            intakePot = new AnalogPotentiometer(portProps.getIntakePivotPotentiometerPort());
        }
        return intakePot;
    }

    public Ultrasonic getHatchSensor() {
        if (hatchSensor == null) {
            hatchSensor = new Ultrasonic(portProps.getHatchUltrasonicTriggerDioPort(), portProps.getHatchUltrasonicEchoDioPort(), Ultrasonic.Unit.kInches);
        }
        return hatchSensor;
    }

    public SerialPort getTrackingCameraSerialPort(){
        return new SerialPort(115200, SerialPort.Port.kUSB);
    }

    public SerialPort getLivestreamCmeraSerialPort(){
        return new SerialPort(115200, SerialPort.Port.kUSB1);
    }

}
