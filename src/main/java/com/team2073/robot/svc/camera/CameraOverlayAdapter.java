package com.team2073.robot.svc.camera;

import com.team2073.common.camera.CameraMessageReceiverSerialImpl;
import com.team2073.common.camera.CameraMessageService;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.AsyncPeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.domain.CameraMessage;
import edu.wpi.first.wpilibj.SerialPort;

public class CameraOverlayAdapter implements AsyncPeriodicRunnable {
    private final String TO_FAR_RIGHT = "A";
    private final String AT_CENTER = "B";
    private final String TO_FAR_LEFT = "C";
    private final String NOT_TRACKING = "D";
    private final String HATCH = "0";
    private final String BALL = "1";
    private final String SEND_JSON = "2";

    private SerialPort trackingCamPort = ApplicationContext.getInstance().getTrackingCamSerial();
    private SerialPort liveStreamPort = ApplicationContext.getInstance().getLivestreamCamSerial();
    private String command;

    private CameraMessageParserTargetTrackerImpl parser = new CameraMessageParserTargetTrackerImpl();
    private CameraMessageReceiverSerialImpl cameraMessageReceiverSerial = new CameraMessageReceiverSerialImpl(ApplicationContext.getInstance().getTrackingCamSerial());
    private CameraMessageService cameraMessageService = new CameraMessageService<>(parser, cameraMessageReceiverSerial);

    public CameraOverlayAdapter() {
        RobotContext.getInstance().getPeriodicRunner().registerAsync(this, 30);
    }

    @Override
    public void onPeriodicAsync() {
        trackingCamPort.writeString(SEND_JSON + "\n");
        if (ApplicationContext.getInstance().getShooterSubsystem().hasBall()) {
            sendMessage(BALL, trackingCamPort);
        } else if (ApplicationContext.getInstance().getHatchManipulatorSubsystem().hatchDetected()) {
            sendMessage(HATCH, trackingCamPort);
        }
        String alignState = getAlignState(getMessage().getRetroreflectiveAlign());
        sendMessage(alignState, liveStreamPort);
    }

    public CameraMessage getMessage() {
        return (CameraMessage) cameraMessageService.currentMessage();
    }

    public void sendMessage(String msg, SerialPort port) {
        port.writeString(msg + "\n");
    }

    public String getAlignState(double angle) {
        if (!getMessage().isTracking()) {
            command = NOT_TRACKING;
        } else {
            if (angle <= -2) {
                command = TO_FAR_LEFT;
            } else if (angle > -2 && angle < 2) {
                command = AT_CENTER;
            } else {
                command = TO_FAR_RIGHT;
            }
        }
        return command;
    }

}
