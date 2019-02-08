package com.team2073.robot.subsystem.carriage;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorController;
import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.proploader.PropertyLoader;
import com.team2073.common.proploader.model.PropertyContainer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.team2073.robot.subsystem.carriage.ShooterSubsystem.ShooterState;

public class ShooterSubsystem implements PeriodicRunnable, StateSubsystem<ShooterState> {
    private static final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private static PropertyLoader loader = robotCtx.getPropertyLoader();
    private static ShooterProperties shooterProperties = loader.registerPropContainer(ShooterProperties.class);

    private IMotorController shooterLeft = appCtx.getLeftShooter();
    private IMotorController shooterRight = appCtx.getRightShooter();
    private DigitalInput cargoSensor = appCtx.getCargoSensor();

    private ShooterState state = ShooterState.STOP;

    public ShooterSubsystem() {
        autoRegisterWithPeriodicRunner();
        shooterLeft.setInverted(true);
    }

    private void setPower(Double percent) {
        shooterLeft.set(ControlMode.PercentOutput, percent);
        shooterRight.set(ControlMode.PercentOutput, percent);
    }


    @Override
    public void onPeriodic() {
        if(appCtx.getController().getRawButton(1)){
            set(ShooterState.INTAKE);
        }else if (appCtx.getController().getRawButton(2)){
            set(ShooterState.HIGH_SHOOT);
        }else{
            set(ShooterState.STOP);
        }
//        if (cargoSensor.get()) {
//            set(ShooterState.STALL);
//        }

        state = currentState();
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
        HIGH_SHOOT(shooterProperties.highShootPercent),
        INTAKE(shooterProperties.intakePercent),
        STOP(0d),
        STALL(shooterProperties.stallPercent),
        DISABLED(0d);

        private Double percent;

        ShooterState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }

    @PropertyContainer
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
