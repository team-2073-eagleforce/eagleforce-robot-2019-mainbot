package com.team2073.robot.ctx;

import com.ctre.phoenix.motorcontrol.IMotorController;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team2073.common.ctx.RobotContext;
import com.team2073.robot.AppConstants;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.DrivetrainSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.CarriageSubsystem;
import com.team2073.robot.subsystem.StiltSubsystem;
import com.team2073.robot.subsystem.driveprofile.DriveProfileManager;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import com.team2073.robot.svc.camera.CameraOverlayAdapter;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.team2073.robot.AppConstants.Ports.*;

public class ApplicationContext {

	private ApplicationProperties applicationProperties = RobotContext.getInstance().getPropertyLoader().registerPropContainer(ApplicationProperties.class);
	private AppConstants.RobotPortsProperties portProps = applicationProperties.getPortsProperties();

	private static ApplicationContext instance;

	private Logger log = LoggerFactory.getLogger(getClass());
	/*CAMERA */
	private UsbCamera trackingCam;
	private SerialPort trackingCamSerial;
	private SerialPort livestreamCamSerial;
	private CameraOverlayAdapter cameraOverlayAdapter;
	//	====================================================================================================================
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
	private IMotorControllerEnhanced rightShooter;
	private IMotorController intakePivotSlave;
	//	====================================================================================================================
	/* VICTOR SPs */
	private SpeedController intakeRoller;
	private SpeedController intakeRoller2;
	//	====================================================================================================================
	/*SUBSYSTEMS*/
	private DrivetrainSubsystem drivetrainSubsystem;
	private IntakeRollerSubsystem intakeRollerSubsystem;
	private IntakePivotSubsystem intakePivotSubsystem;
	private CarriageSubsystem carriageSubsystem;
	private StiltSubsystem stiltSubsystem;
	private ElevatorSubsystem elevatorSubsystem;
	//	====================================================================================================================
	/*SOLENOIDS*/
	private DoubleSolenoid driveShiftSolenoid;
	private DoubleSolenoid elevatorShiftSolenoid;
	private DoubleSolenoid shooterClampSolenoid;
	private DoubleSolenoid carriageSlideSolenoid;
	private DoubleSolenoid engageStiltSolenoid;

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
	private DigitalOutput cameraLED;
	private DigitalInput intakeIndex;
	//	====================================================================================================================
	private double climbHeight = 19d;

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

	public CameraOverlayAdapter getCameraOverlayAdapter() {
		if (cameraOverlayAdapter == null) {
			cameraOverlayAdapter = new CameraOverlayAdapter();
		}
		return cameraOverlayAdapter;
	}

	public UsbCamera getTrackingCam() {
		if (trackingCam == null) {
			trackingCam = new UsbCamera("trackingCamera", 0);
		}
		return trackingCam;
	}

	public SerialPort getTrackingCamSerial() {
		if (trackingCamSerial == null) {
//            trackingCamSerial = new SerialPort(115200, SerialPort.Port.kUSB2);
		}
		return trackingCamSerial;
	}

	public SerialPort getLivestreamCamSerial() {
		if (livestreamCamSerial == null) {
			livestreamCamSerial = new SerialPort(115200, SerialPort.Port.kUSB);
		}
		return livestreamCamSerial;
	}

	public DriveProfileManager getDriveProfileManager() {
		if (driveProfileManager == null) {
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

	public CarriageSubsystem getCarriageSubsystem() {
		if (carriageSubsystem == null) {
			carriageSubsystem = new CarriageSubsystem();
		}
		return carriageSubsystem;
	}

	public StiltSubsystem getStiltSubsystem() {
		if (stiltSubsystem == null) {
			stiltSubsystem = new StiltSubsystem();
		}
		return stiltSubsystem;
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

	public IMotorController getIntakePivotSlave() {
		if (intakePivotSlave == null) {
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

	public IMotorControllerEnhanced getRightShooter() {
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

	public DoubleSolenoid getShooterClampSolenoid() {
		if (shooterClampSolenoid == null) {
			shooterClampSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getShooterClampSolenoidPort(), portProps.getShooterOpenSolenoidPort());
		}
		return shooterClampSolenoid;
	}

	public DoubleSolenoid getCarriageSlideSolenoid() {
		if (carriageSlideSolenoid == null) {
			carriageSlideSolenoid = new DoubleSolenoid(portProps.getPcm2CanId(), portProps.getCarriageOutSolenoidPort(), portProps.getCarriageInSolenoidPort());
		}
		return carriageSlideSolenoid;
	}

	public DoubleSolenoid getEngageStiltSolenoid() {
		if (engageStiltSolenoid == null) {
			engageStiltSolenoid = new DoubleSolenoid(portProps.getPcm1CanId(), portProps.getDeployStiltSolenoid(), portProps.getCloseStiltSolenoid());
		}
		return engageStiltSolenoid;
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

	public DigitalOutput getCameraLED() {
		if (cameraLED == null) {
			cameraLED = new DigitalOutput(4);
		}
		return cameraLED;
	}

	public DigitalInput getIntakeIndex() {
		if (intakeIndex == null) {
			intakeIndex = new DigitalInput(9);
		}
		return intakeIndex;
	}

	public double getClimbHeight() {
		return climbHeight;
	}

	public void setClimbHeight(double climbHeight) {
		this.climbHeight = climbHeight;
	}
}
