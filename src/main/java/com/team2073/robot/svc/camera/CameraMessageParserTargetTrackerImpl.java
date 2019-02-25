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

public class CameraMessageParserTargetTrackerImpl implements CameraMessageParser<CameraMessage>, SmartDashboardAware {
    private static final String TRACKING_JSON_KEY = "Trking";
    private static final String RETROREFLECTIVE_ALIGN_JSON_KEY = "Xcenter";
    private static final String RETROREFLECTIVE_DISTANCE_JSON_KEY = "distance";
    private static final String TIMESTAMP_JSON_KEY = "Timer";
    private static final String POSE = "Pose";
    private static final String rawMsg = "Not yet recieved";
    private CameraMessage lastMsg = new CameraMessage();

    public CameraMessageParserTargetTrackerImpl(){
        RobotContext.getInstance().getSmartDashboardRunner().registerInstance(this);
    }

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
            message.setTracking(jsonObject.getBoolean(TRACKING_JSON_KEY));
            message.setRetroreflectiveAlign(jsonObject.getDouble(RETROREFLECTIVE_ALIGN_JSON_KEY));
            message.setRetroreflectiveDistance(jsonObject.getDouble(RETROREFLECTIVE_DISTANCE_JSON_KEY));
            message.setPose(jsonObject.getFloat(POSE));
            logger.trace("CameraMessage: [{}].", message.toString());
            lastMsg = message;
            return message;
        }catch (JSONException e){
            logger.error("Could not parse camera message: [{}]", msg);
            SmartDashboard.putString("JSON object error", msg);
            return lastMsg;
        }
    }

    @Override
    public void updateSmartDashboard() {
        SmartDashboard.putString("CameraMessage", rawMsg);
    }

}

