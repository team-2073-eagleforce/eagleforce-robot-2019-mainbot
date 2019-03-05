package com.team2073.robot.conf;

import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.common.proploader.model.PropertyContainerField;
import com.team2073.robot.AppConstants;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.CarriageSubsystem;

@PropertyContainer
public class ApplicationProperties {
    @PropertyContainerField(name = "elevator")
    private ElevatorSubsystem.ElevatorProperties elevatorProperties;
    @PropertyContainerField(name = "carriage")
    private CarriageSubsystem.ShooterProperties shooterProperties;
    @PropertyContainerField(name = "ports")
    private AppConstants.RobotPortsProperties portsProperties;
    @PropertyContainerField(name = "directionality")
    private MotorDirectionalityProperties motorDirectionalityProperties;

    public MotorDirectionalityProperties getMotorDirectionalityProperties() {
        return motorDirectionalityProperties;
    }

    public void setMotorDirectionalityProperties(MotorDirectionalityProperties motorDirectionalityProperties) {
        this.motorDirectionalityProperties = motorDirectionalityProperties;
    }


    public ElevatorSubsystem.ElevatorProperties getElevatorProperties() {
        return elevatorProperties;
    }

    public void setElevatorProperties(ElevatorSubsystem.ElevatorProperties elevatorProperties) {
        this.elevatorProperties = elevatorProperties;
    }

    public CarriageSubsystem.ShooterProperties getShooterProperties() {
        return shooterProperties;
    }

    public void setShooterProperties(CarriageSubsystem.ShooterProperties shooterProperties) {
        this.shooterProperties = shooterProperties;
    }

    public AppConstants.RobotPortsProperties getPortsProperties() {
        return portsProperties;
    }

    public void setPortsProperties(AppConstants.RobotPortsProperties portsProperties) {
        this.portsProperties = portsProperties;
    }
}
