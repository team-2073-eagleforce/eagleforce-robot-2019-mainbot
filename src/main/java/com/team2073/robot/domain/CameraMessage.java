package com.team2073.robot.domain;

public class CameraMessage {
    private boolean tracking;
    private double retroreflectiveAlign;
    private double retroreflectiveDistance;
    private String timeStamp;
    private int trackingHeight;
    private float pose;

    public float getPose() {
        return pose;
    }

    public void setPose(float pose) {
        this.pose = pose;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public double getRetroreflectiveAlign() {
        return retroreflectiveAlign;
    }

    public void setRetroreflectiveAlign(double retroreflectiveAlign) {
        this.retroreflectiveAlign = retroreflectiveAlign;
    }

    public double getRetroreflectiveDistance() {
        return retroreflectiveDistance;
    }

    public void setRetroreflectiveDistance(double retroreflectiveDistance) {
        this.retroreflectiveDistance = retroreflectiveDistance;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTrackingHeight() {
        return trackingHeight;
    }

    public void setTrackingHeight(int trackingHeight) {
        this.trackingHeight = trackingHeight;
    }
}
