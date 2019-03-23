package com.team2073.robot;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.robot.AbstractRobotDelegate;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.domain.CameraMessage;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.svc.camera.CameraOverlayAdapter;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class RobotDelegate extends AbstractRobotDelegate {

    private RobotContext robotCtx = RobotContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
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
//        robotCtx.getDataRecorder().disable();
        oi = new OperatorInterface();
        mediator = appCtx.getMediator();
//        UsbCamera trackingCam = new UsbCamera("trackingCam", 0);
    }

    @Override
    public void robotPeriodic() {
//        System.out.println("ELEVATOR MAX: " + appCtx.getElevatorTopLimit().get() +
//                "\t ELEVATOR MIN: " + appCtx.getElevatorBottomLimit().get());
        if(!appCtx.getElevatorBottomLimit().get()){
            System.out.println("\n\n\n ZERO \n\n\n");
        }
//        System.out.println("ElevatorPosition: " + appCtx.getElevatorSubsystem().position()
//                + "\t IntakePosition: " + appCtx.getIntakePivotSubsystem().position() );

    }

}
