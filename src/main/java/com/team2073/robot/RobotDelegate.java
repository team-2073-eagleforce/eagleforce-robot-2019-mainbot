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

public class RobotDelegate extends AbstractRobotDelegate {

    private RobotContext robotCtx = RobotContext.getInstance();
    private PropertyLoader loader = robotCtx.getPropertyLoader();
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator;
    private OperatorInterface oi;
    private CameraOverlayAdapter cameraOverlayAdapter;

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
        UsbCamera livestreamCam = CameraServer.getInstance().startAutomaticCapture(1);
        UsbCamera trackingCam = new UsbCamera("trackingCam", 0);
    }

    CameraMessage msg = new CameraMessage();
    double[] gyro = new double[3];
    @Override
    public void robotPeriodic() {
        appCtx.getGyro().getAccumGyro(gyro);
        msg = appCtx.getCameraOverlayAdapter().getMessage();
        System.out.println("Distance: " + (msg.getRetroreflectiveDistance() - 24.625)
                + "\t Angle: " + msg.getRetroreflectiveAlign() + "\t Pose: "
                + msg.getPose() + "\t time: " + msg.getTimeStamp() + "\t Actual Gyro: " + gyro[2]);
    }

}
