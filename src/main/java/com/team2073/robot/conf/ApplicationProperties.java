package com.team2073.robot.conf;

import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.common.proploader.model.PropertyContainerField;
import com.team2073.robot.AppConstants;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;

@PropertyContainer
public class ApplicationProperties {
    @PropertyContainerField(name = "elevator")
    private ElevatorSubsystem.ElevatorProperties elevatorProperties;
    @PropertyContainerField(name = "shooter")
    private ShooterSubsystem.ShooterProperties shooterProperties;
    @PropertyContainerField(name = "ports")
    private AppConstants.RobotPortsProperties portsProperties;

    public MotorDirectionalityProperties getMotorDirectionalityProperties() {
        return motorDirectionalityProperties;
    }

    public void setMotorDirectionalityProperties(MotorDirectionalityProperties motorDirectionalityProperties) {
        this.motorDirectionalityProperties = motorDirectionalityProperties;
    }

    @PropertyContainerField(name = "directionality")
    private MotorDirectionalityProperties motorDirectionalityProperties;

    public ElevatorSubsystem.ElevatorProperties getElevatorProperties() {
        return elevatorProperties;
    }

    public void setElevatorProperties(ElevatorSubsystem.ElevatorProperties elevatorProperties) {
        this.elevatorProperties = elevatorProperties;
    }

    public ShooterSubsystem.ShooterProperties getShooterProperties() {
        return shooterProperties;
    }

    public void setShooterProperties(ShooterSubsystem.ShooterProperties shooterProperties) {
        this.shooterProperties = shooterProperties;
    }

    public AppConstants.RobotPortsProperties getPortsProperties() {
        return portsProperties;
    }

    public void setPortsProperties(AppConstants.RobotPortsProperties portsProperties) {
        this.portsProperties = portsProperties;
    }
}
