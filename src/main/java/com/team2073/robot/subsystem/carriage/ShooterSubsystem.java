package com.team2073.robot.subsystem.carriage;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import com.team2073.robot.subsystem.intake.IntakeRollerSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterState;
import static com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterState.DISABLED;

public class ShooterSubsystem implements PeriodicRunnable, StateSubsystem<ShooterState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private ShooterProperties shooterProperties = applicationProperties.getShooterProperties();
    private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();

    private IMotorController shooterLeft = appCtx.getLeftShooter();
    private IMotorController shooterRight = appCtx.getRightShooter();
    private DigitalInput cargoSensor = appCtx.getCargoSensor();

    private ShooterState state = ShooterState.STOP;

    public ShooterSubsystem() {
        autoRegisterWithPeriodicRunner();
        shooterLeft.setInverted(directionalityProperties.isShooterLeft());
        shooterRight.setInverted(directionalityProperties.isShooterRight());
    }

    private void setPower(Double percent) {
        shooterLeft.set(ControlMode.PercentOutput, percent);
        shooterRight.set(ControlMode.PercentOutput, percent);
    }


    @Override
    public void onPeriodic() {
        if (cargoSensor.get() && currentState() != ShooterState.HIGH_SHOOT && currentState() != ShooterState.INTAKE) {
            set(ShooterState.STALL);
        }


        setPower(state.getPercent());
    }

    @Override
    public ShooterState currentState() {
        return state;
    }

    @Override
    public void set(ShooterState goalState) {
        state = goalState;
    }

    public enum ShooterState {
        HIGH_SHOOT(0.9),
        INTAKE(-0.5),
        STOP(0d),
        STALL(-0.12),
        DISABLED(0d);

        private Double percent;

        ShooterState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }

    public static class ShooterProperties {
        private double highShootPercent;
        private double intakePercent;
        private double stallPercent;

        public double getHighShootPercent() {
            return highShootPercent;
        }

        public void setHighShootPercent(double highShootPercent) {
            this.highShootPercent = highShootPercent;
        }

        public double getIntakePercent() {
            return intakePercent;
        }

        public void setIntakePercent(double intakePercent) {
            this.intakePercent = intakePercent;
        }

        public double getStallPercent() {
            return stallPercent;
        }

        public void setStallPercent(double stallPercent) {
            this.stallPercent = stallPercent;
        }
    }
}
