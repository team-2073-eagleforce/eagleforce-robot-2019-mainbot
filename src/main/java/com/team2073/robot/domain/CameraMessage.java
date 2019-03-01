package com.team2073.robot.domain;

public class CameraMessage {
    private int tracking = -2;
    private double retroreflectiveAlign = -2;
    private double retroreflectiveDistance = -2;
    private double timeStamp = -2;
    private double pose = -2;

    public double getPose() {
        return pose;
    }

    public void setPose(double pose) {
        this.pose = pose;
    }

    public int isTracking() {
        return tracking;
    }

    public void setTracking(int tracking) {
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

    public double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(double timeStamp) {
        this.timeStamp = timeStamp;
    }
}
