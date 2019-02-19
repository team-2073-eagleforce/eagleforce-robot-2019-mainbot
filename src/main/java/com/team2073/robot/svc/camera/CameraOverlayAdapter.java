package com.team2073.robot.svc.camera;

import com.team2073.common.camera.CameraMessageReceiverSerialImpl;
import com.team2073.common.camera.CameraMessageService;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.domain.CameraMessage;
import com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import edu.wpi.first.wpilibj.SerialPort;

public class CameraOverlayAdapter implements PeriodicRunnable {
    private final String TO_FAR_RIGHT = "A";
    private final String AT_CENTER = "B";
    private final String TO_FAR_LEFT = "C";
    private final String NOT_TRACKING = "D";

    SerialPort trackingCamPort;
    SerialPort liveStreamPort;
    String command;

    CameraMessageParserTargetTrackerImpl parser = new CameraMessageParserTargetTrackerImpl();
    ShooterSubsystem.ShooterState shooterState = new ShooterSubsystem().currentState();
    boolean hatchDetected = new HatchManipulatorSubsystem().hatchDetected();

    public CameraOverlayAdapter(){
        autoRegisterWithPeriodicRunner();
    }


    @Override
    public void onPeriodic() {
        if(hatchDetected){
            sendMessage("1", trackingCamPort);
        }

        //Not sure how to check for whether it has the ball or not. This is just a guess.
        if(shooterState == ShooterSubsystem.ShooterState.HIGH_SHOOT){
            sendMessage("0", trackingCamPort);
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
