package com.team2073.robot.svc.camera;

import com.team2073.common.camera.CameraMessageReceiverSerialImpl;
import com.team2073.common.camera.CameraMessageService;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.domain.CameraMessage;
import edu.wpi.first.wpilibj.SerialPort;

public class CameraOverlayAdapter implements AsyncPeriodicRunnable {
    private static final String TO_FAR_RIGHT = "A";
    private static final String AT_CENTER = "B";
    private static final String TO_FAR_LEFT = "C";
    private static final String NOT_TRACKING = "D";
    private static final String HATCH = "0";
    private static final String BALL = "1";
    private static final String REQUEST_MESSAGE = "2";

    private SerialPort trackingCamPort = ApplicationContext.getInstance().getTrackingCamSerial();
    private SerialPort liveStreamPort = ApplicationContext.getInstance().getLivestreamCamSerial();
    private String angle;

    private CameraMessageParserTargetTrackerImpl parser = new CameraMessageParserTargetTrackerImpl();
    private CameraMessageReceiverSerialImpl cameraMessageReceiverSerial = new CameraMessageReceiverSerialImpl(new SerialPort(115200, SerialPort.Port.kUSB2));
    private CameraMessageService cameraMessageService;

    public CameraOverlayAdapter() {
        RobotContext.getInstance().getPeriodicRunner().registerAsync(this, 30);
        cameraMessageService = new CameraMessageService<>(parser, cameraMessageReceiverSerial);
    }

    @Override
    public void onPeriodicAsync() {
        if (ApplicationContext.getInstance().getShooterSubsystem().hasBall()) {
            sendMessage(BALL, trackingCamPort);
        } else if (ApplicationContext.getInstance().getHatchManipulatorSubsystem().hatchDetected()) {
            sendMessage(HATCH, trackingCamPort);
        }
//        String alignState = getAlignState(
//                getMessage().getRetroreflectiveAlign());
//        sendMessage(alignState, liveStreamPort);
    }

    public CameraMessage getMessage() {
        if(cameraMessageService.currentMessage() == null){
            return new CameraMessage();
        }else
            return (CameraMessage) cameraMessageService.currentMessage();
    }

    public void sendMessage(String msg, SerialPort port) {
        port.writeString(msg + "\n");
    }

    public String getAlignState(double angle) {
        if (getMessage().isTracking() != 1) {
            this.angle = NOT_TRACKING;
        } else {
            if (angle <= -2) {
                this.angle = TO_FAR_LEFT;
            } else if (angle > -2 && angle < 2) {
                this.angle = AT_CENTER;
            } else {
                this.angle = TO_FAR_RIGHT;
            }
        }
        return this.angle;
    }

}
