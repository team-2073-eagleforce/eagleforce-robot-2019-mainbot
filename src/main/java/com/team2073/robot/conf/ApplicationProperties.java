package com.team2073.robot.conf;

import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.robot.subsystem.ElevatorSubsystem.ElevatorProperties;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterProperties;
import com.team2073.robot.subsystem.intake.IntakePivotSubsystem.IntakePivotProperties;

@PropertyContainer
public class ApplicationProperties {

    private ElevatorProperties elevatorProperties;
    private ShooterProperties shooterProperties;
    private IntakePivotProperties intakePivotProperties;

    public ElevatorProperties getElevatorProperties() {
        return elevatorProperties;
    }

    public void setElevatorProperties(ElevatorProperties elevatorProperties) {
        this.elevatorProperties = elevatorProperties;
    }

    public ShooterProperties getShooterProperties() {
        return shooterProperties;
    }

    public void setShooterProperties(ShooterProperties shooterProperties) {
        this.shooterProperties = shooterProperties;
    }

    public IntakePivotProperties getIntakePivotProperties() {
        return intakePivotProperties;
    }

    public void setIntakePivotProperties(IntakePivotProperties intakePivotProperties) {
        this.intakePivotProperties = intakePivotProperties;
    }
}
