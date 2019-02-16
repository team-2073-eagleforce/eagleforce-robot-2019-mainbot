package com.team2073.robot.conf;

import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.common.proploader.model.PropertyContainerField;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;

@PropertyContainer
public class ApplicationProperties {
    @PropertyContainerField(name = "elevator")
    private ElevatorSubsystem.ElevatorProperties elevatorProperties;
    @PropertyContainerField(name = "shooter")
    private ShooterSubsystem.ShooterProperties shooterProperties;

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
}
