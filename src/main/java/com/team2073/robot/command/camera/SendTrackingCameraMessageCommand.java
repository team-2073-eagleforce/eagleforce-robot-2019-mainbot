package com.team2073.robot.command.camera;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class SendTrackingCameraMessageCommand extends AbstractLoggingCommand {

    public SendTrackingCameraMessageCommand(TrackingCameraMessage trackingCameraMessage) {
        this.trackingCameraMessage = trackingCameraMessage;
    }

    public enum TrackingCameraMessage{
        HATCH("0"),
        BALL("1"),
        GET_JSON("2");

        private String message;

        TrackingCameraMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private final TrackingCameraMessage trackingCameraMessage;
    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getTrackingCamSerial().writeString(trackingCameraMessage.getMessage() + "\n");
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }

}
