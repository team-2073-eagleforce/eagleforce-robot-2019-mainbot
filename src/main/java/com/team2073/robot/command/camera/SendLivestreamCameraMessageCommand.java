package com.team2073.robot.command.camera;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;

public class SendLivestreamCameraMessageCommand extends AbstractLoggingCommand {

    private final LivestreamCameraMessage livestreamCameraMessage;

    public SendLivestreamCameraMessageCommand(LivestreamCameraMessage livestreamCameraMessage) {
        this.livestreamCameraMessage = livestreamCameraMessage;
    }

    public enum LivestreamCameraMessage{
        LESS_THAN_NEGATIVE_2("A"),
        BETWEEN_2_AND_NEGATIVE_2("B"),
        ABOVE_POSITIVE_2("C"),
        NOT_TRACKING("D");

        private String message;
        LivestreamCameraMessage(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getLivestreamCamSerial().writeString(livestreamCameraMessage.getMessage() + "\n");
    }

    @Override
    protected boolean isFinishedDelegate() {
        return false;
    }
}
