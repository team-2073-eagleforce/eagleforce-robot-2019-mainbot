package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.domain.CameraMessage;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import com.team2073.robot.svc.camera.CameraOverlayAdapter;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Watchdog;

import java.sql.SQLOutput;

public class RobotDelegate extends AbstractRobotDelegate {

    private RobotContext robotCtx = RobotContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private IntakePivotSubsystem intakePivot;
    private IntakeRollerSubsystem intakeRoller;
    private ElevatorSubsystem elevator;
    private Mediator mediator;
    private OperatorInterface oi;
    private CameraOverlayAdapter cameraOverlayAdapter;
	UsbCamera livestreamCam = CameraServer.getInstance().startAutomaticCapture();


	public RobotDelegate(double period) {
        super(period);
    }


    @Override
    public void robotInit() {
        loader.autoRegisterAllPropContainers(getClass().getPackage().getName());
        loader.loadProperties();
        intakeRoller = appCtx.getIntakeRollerSubsystem();
        intakePivot = appCtx.getIntakePivotSubsystem();
        elevator = appCtx.getElevatorSubsystem();
//        robotCtx.getDataRecorder().disable();
        oi = new OperatorInterface();
        mediator = appCtx.getMediator();
//        UsbCamera trackingCam = new UsbCamera("trackingCam", 0);
    }

    @Override
    public void robotPeriodic() {
	    System.out.println("ElevatorPosition: " + appCtx.getElevatorSubsystem().position()
			    + "\t IntakePosition: " + appCtx.getIntakePivotSubsystem().position() );
//	    if(appCtx.getController().getRawButton(1)){
//	        intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.INTAKE_SPEED);
//        }else{
//            intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.STOP);
//        }

//	    if(appCtx.getController().getRawButton(2)){
//	        intakePivot.set(90d);
//        }
//
//	    if(appCtx.getController().getRawButton(3)){
//	        intakePivot.set(165d);
//        }
//	    if(appCtx.getController().getRawButton(6)){
//	        intakePivot.set(intakePivot.robotHeightToIntakeAngle(19,19.85 - elevator.position() - 3));
//	        intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.INTAKE_SPEED);
//        }
//	    if(appCtx.getController().getRawButton(6)){
//            elevator.manuallySetMotors(-.55);
//        }else{
//		    intakeRoller.set(IntakeRollerSubsystem.IntakeRollerState.STOP);
//        }
//
//	    if(appCtx.getController().getRawButton(1)){
//	    	if(elevator.position() < 20){
//			    elevator.manuallySetMotors(.6);
//		    }else{
//	    		elevator.manuallySetMotors(0d);
//		    }
//	    	intakePivot.set(120d);
//
//	    }

//        System.out.println("ELEVATOR MAX: " + appCtx.getElevatorTopLimit().get() +
//                "\t ELEVATOR MIN: " + appCtx.getElevatorBottomLimit().get());
//        if(!appCtx.getElevatorBottomLimit().get()){
//            System.out.println("\n\n\n ZERO \n\n\n");
//        }

    }

}
