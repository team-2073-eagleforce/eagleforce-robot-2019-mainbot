package com.team2073.robot.svc.camera;

import com.team2073.common.camera.CameraMessageParser;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.SmartDashboardAware;
import com.team2073.robot.domain.CameraMessage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraMessageParserTargetTrackerImpl implements CameraMessageParser<CameraMessage> {
    private static final String TRACKING_JSON_KEY = "Trk";
    private static final String RETROREFLECTIVE_ALIGN_JSON_KEY = "Deg2Tgt";
    private static final String RETROREFLECTIVE_DISTANCE_JSON_KEY = "Dist";
    private static final String TIMESTAMP_JSON_KEY = "ImTime";
    private static final String POSE = "Pose";
    private static final String rawMsg = "Not yet recieved";
    private CameraMessage lastMsg = new CameraMessage();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CameraMessage parseMsg(String msg) {
        CameraMessage message = new CameraMessage();
        try{
            if(msg == null){
                logger.trace("Null JSON String received.");
                return lastMsg;
            }

            if(!msg.startsWith("{")){
                logger.trace("String is not JSON [{}]", msg);
                return lastMsg;
            }

            if(msg.contains("}\n{")){
                msg = msg.split("\n", 2)[0];
            }
            logger.trace("Parsing camera message: [{}]", msg);

            JSONObject jsonObject = new JSONObject(msg);
            message.setTracking(jsonObject.getInt(TRACKING_JSON_KEY));
            message.setRetroreflectiveAlign(jsonObject.getDouble(RETROREFLECTIVE_ALIGN_JSON_KEY));
            message.setRetroreflectiveDistance(jsonObject.getDouble(RETROREFLECTIVE_DISTANCE_JSON_KEY));
            message.setPose(jsonObject.getDouble(POSE));
            message.setTimeStamp(jsonObject.getDouble(TIMESTAMP_JSON_KEY));
            logger.trace("CameraMessage: [{}].", message.toString());
            lastMsg = message;
            return message;
        }catch (JSONException e){
            logger.error("Could not parse camera message: [{}]", msg);
            return lastMsg;
        }
    }

}

