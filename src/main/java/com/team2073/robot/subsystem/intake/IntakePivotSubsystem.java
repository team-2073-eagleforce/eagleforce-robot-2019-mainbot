package com.team2073.robot.subsystem.intake;

import com.ctre.phoenix.motorcontrol.*;
import com.team2073.common.controlloop.MotionProfileControlloop;
import com.team2073.common.controlloop.PidfControlLoop;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.motionprofiling.ProfileConfiguration;
import com.team2073.common.motionprofiling.TrapezoidalProfileManager;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.position.converter.PositionConverter;
import com.team2073.common.util.MathUtil;
import com.team2073.common.util.TalonUtil;
import com.team2073.robot.AppConstants;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.dev.IndexZeroer;
import com.team2073.robot.mediator.PositionalSubsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.RobotState;

public class IntakePivotSubsystem implements PeriodicRunnable, PositionalSubsystem {

	//    MAINBOT
	private static final double POT_MIN_VALUE = .3852;
	private static final double POT_MAX_VALUE = .3046;
	private static final double MIN_POSITION = 0;
	private static final double MAX_POSITION = 224;

	//    PRACTICE BOT
//    private static final double POT_MIN_VALUE = .3683;
//    private static final double POT_MAX_VALUE = .4503;
	private static final double INTAKE_WHEELS_LEVEL_WITH_PIVOT = 171d;
	private static final double INTAKE_ARM_LENGTH = 14.25;
	private static final double ANGLE_TO_TAP_LV3 = 127.1333;
	private static final double HEIGHT_OF_PIVOT = 9.125;
	private static final double MAX_VELOCITY = 800;
	private static final double PERCENT_FOR_MAX_VELOCITY = .6;
	private static final double MAX_ACCELERATION = 1500d;
	private static final double TIME_STEP = AppConstants.Subsystems.DEFAULT_TIMESTEP;
	private static final double TICS_PER_DEGREE = 4096d / 360d;
	private static final double KA = .15 / MAX_ACCELERATION;

	private final RobotContext robotCtx = RobotContext.getInstance();
	private final ApplicationContext appCtx = ApplicationContext.getInstance();
	private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
	private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();

	private IMotorControllerEnhanced intakeMaster = appCtx.getIntakePivotMaster();
	private IMotorController intakeSlave = appCtx.getIntakePivotSlave();
	private AnalogPotentiometer pot = appCtx.getIntakePot();

	private Double setpoint = null;
	private boolean hasZeroedFromPot = false;

	private PositionConverter converter = new IntakePositionConverter();
	//    private PidfControlLoop holdingPID = new PidfControlLoop(0.01, 0.0004, 0, 0, .3);
//    private PidfControlLoop holdingPID = new PidfControlLoop(0.005, 0.008, 0.000, 0, .4);
	private PidfControlLoop holdingPID = new PidfControlLoop(0.02, 0.01, 0, 0, 1);
	private ProfileConfiguration profileConfig = new ProfileConfiguration(MAX_VELOCITY, MAX_ACCELERATION, TIME_STEP);
	private MotionProfileControlloop controller = new MotionProfileControlloop(.008, 0,
			PERCENT_FOR_MAX_VELOCITY / MAX_VELOCITY, KA, 1);
	private Counter counter = new Counter(appCtx.getIntakeIndex());

	private IndexZeroer indexZeroer = new IndexZeroer(intakeMaster, converter, appCtx.getIntakeIndex(),
			this::potPosition, 15d);

//    private TrapezoidalProfileManager profileManager = new TrapezoidalProfileManager(controller, profileConfig,
//            this::position, holdingPID);

//    private GraphCSV graph = new GraphCSV("IntakePivot", "time", "position", "velocity", "profile position", "profile velocity", "profile acceleration", "output");

	public IntakePivotSubsystem() {
		autoRegisterWithPeriodicRunner();
		TalonUtil.resetTalon(intakeMaster, TalonUtil.ConfigurationType.SENSOR);
		TalonUtil.resetVictor(intakeSlave, TalonUtil.ConfigurationType.SLAVE);

		intakeSlave.setInverted(directionalityProperties.isIntakePivotSlave());
		intakeMaster.setInverted(directionalityProperties.isIntakePivotMaster());
		intakeSlave.follow(intakeMaster);
		intakeMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		intakeMaster.setSensorPhase(true);
		intakeMaster.setNeutralMode(NeutralMode.Brake);
		intakeSlave.setNeutralMode(NeutralMode.Brake);
		holdingPID.setPositionSupplier(this::position);
		intakeMaster.configPeakOutputForward(1, 10);
		intakeMaster.configPeakOutputReverse(-1, 10);
//        try {
//            graph.initFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
	}

	@Override
	public void set(Double setPoint) {
		this.setpoint = setPoint;
	}

	@Override
	public Double getSetpoint() {
		return setpoint;
	}

	@Override
	public double position() {
		return converter.asPosition(intakeMaster.getSelectedSensorPosition(0));
	}

	@Override
	public double velocity() {
		return converter.asPosition(intakeMaster.getSelectedSensorVelocity(0) * 10);
	}

	private double time;

	@Override
	public void onPeriodic() {
        if(!hasZeroedFromPot)
            zeroFromPot();
        if(hasZeroedFromPot && !indexZeroer.isZeroed()){
        	indexZeroer.attemptZero();
        }

//		if (appCtx.getController().getRawButton(10)) {
//			intakeMaster.setSelectedSensorPosition(0, 0, 10);
//		}
//        System.out.println("Intake Pivot Position: " + position() + "\t Pot value: " + pot.get()
//                + "\t Voltage: " + intakeMaster.getMotorOutputVoltage()
//                + "\t Counter: " + counter.get());
		if (setpoint == null) {
			return;
		}


		if (RobotState.isEnabled()) {
//			profileManager.setPoint(setpoint);
//			profileManager.newOutput();
//			intakeMaster.set(ControlMode.PercentOutput, profileManager.getOutput());
			holdingPID.updateSetPoint(setpoint);
			holdingPID.updatePID(.01);
			intakeMaster.set(ControlMode.PercentOutput, holdingPID.getOutput());

		}else{
			holdingPID.resetAccumulatedError();
		}

	}

	private void zeroFromPot() {
		if (velocity() < 1 && velocity() > -1) {
			intakeMaster.setSelectedSensorPosition(converter.asTics(potPosition()), 0, 10);
			hasZeroedFromPot = true;
		}
	}

	private double potPosition() {
		return (pot.get() - POT_MIN_VALUE) * (MAX_POSITION - MIN_POSITION) / (POT_MAX_VALUE - POT_MIN_VALUE) + MIN_POSITION;
	}

	public double robotHeightToIntakeAngle(double heightOfHab, double height) {
		if (height < INTAKE_ARM_LENGTH) {

			return INTAKE_WHEELS_LEVEL_WITH_PIVOT - MathUtil.degreeArcSine((heightOfHab - HEIGHT_OF_PIVOT) / INTAKE_ARM_LENGTH)
					+ MathUtil.degreeArcSine(height / INTAKE_ARM_LENGTH);
		} else {

			return Math.min(INTAKE_WHEELS_LEVEL_WITH_PIVOT - MathUtil.degreeArcSine((heightOfHab - HEIGHT_OF_PIVOT) / INTAKE_ARM_LENGTH)
					+ 90, 220);
		}
	}

	public void setIntakeToClimbHeight(double heightOfHab, double elevatorHeight) {

		set(robotHeightToIntakeAngle(heightOfHab, 19.85 - elevatorHeight + .25));
	}


	private static class IntakePositionConverter implements PositionConverter {
		@Override
		public double asPosition(int tics) {
			return tics / TICS_PER_DEGREE;
		}

		@Override
		public int asTics(double position) {
			return (int) (position * TICS_PER_DEGREE);
		}

		@Override
		public String positionalUnit() {
			return Units.DEGREES;
		}
	}

}
