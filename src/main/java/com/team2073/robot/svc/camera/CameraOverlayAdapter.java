package com.team2073.robot.svc.camera;

import com.team2073.common.camera.CameraMessageReceiverSerialImpl;
import com.team2073.common.camera.CameraMessageService;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.domain.CameraMessage;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import edu.wpi.first.wpilibj.SerialPort;

public class CameraOverlayAdapter implements PeriodicRunnable {
    private final String TO_FAR_RIGHT = "A";
    private final String AT_CENTER = "B";
    private final String TO_FAR_LEFT = "C";
    private final String NOT_TRACKING = "D";
    private final String BALL_NOT_DETECTED = "0";
    private final String BALL_DETECTED_KEY="1";

    SerialPort trackingCamPort = ApplicationContext.getInstance().getTrackingCameraSerialPort();
    SerialPort liveStreamPort = ApplicationContext.getInstance().getLivestreamCmeraSerialPort();
    String command;

    CameraMessageParserTargetTrackerImpl parser = new CameraMessageParserTargetTrackerImpl();
    boolean hatchDetected = new HatchManipulatorSubsystem().hatchDetected();

    public CameraOverlayAdapter(){
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public void onPeriodic() {

        if(ApplicationContext.getInstance().getShooterSubsystem().hasBall()){
            sendMessage(BALL_DETECTED_KEY, trackingCamPort);
        }else{
            sendMessage(BALL_NOT_DETECTED, trackingCamPort);
        }
        parser.parseMsg(getlastMessage(trackingCamPort).toString());
        String alignState = getAlignState(getlastMessage(trackingCamPort).getRetroreflectiveAlign());
        sendMessage(alignState, liveStreamPort);
    }

    @Override
    public void autoRegisterWithPeriodicRunner() {

    }

    @Override
    public void autoRegisterWithPeriodicRunner(String name) {

    }

    public CameraMessage getlastMessage(SerialPort serialPort){
        CameraMessage lastMessage = new CameraMessageService<CameraMessage>(new CameraMessageParserTargetTrackerImpl(), new CameraMessageReceiverSerialImpl(serialPort)).currentMessage();
        return lastMessage;
    }

    public void sendMessage(String msg, SerialPort port){
        port.writeString(msg);
    }

    public String getAlignState(double angle) {
        if (!getlastMessage(trackingCamPort).isTracking()) {
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
