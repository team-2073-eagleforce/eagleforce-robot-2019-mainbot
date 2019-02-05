package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;
import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState.DISABLED;

public class RobotIntakeSubsystem implements PeriodicRunnable, StateSubsystem<RobotIntakeState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
    private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();

    private Timer timer = new Timer();

    private boolean timeStart;


    private RobotIntakeState state = RobotIntakeState.STORE;

    public RobotIntakeSubsystem() {
        autoRegisterWithPeriodicRunner();
    }

    @Override
    public RobotIntakeState currentState() {
        return state;
    }

    @Override
    public void set(RobotIntakeState goalState) {
        state = goalState;
    }

    @Override
    public void onPeriodic() {
        switch (state) {
            // Forks up, clamp down
            case STORE:
                forkSolenoid.set(DoubleSolenoid.Value.kReverse);
                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            // Forks down, clamps up
            case DEPLOY_FORKS:
                forkSolenoid.set(DoubleSolenoid.Value.kForward);
                if (!timeStart){
                    timer.start();
                    timeStart = true;
                }
                if (timer.getElapsedTime() > 0.25) {
                    robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                    timer.stop();
                    timeStart = false;
                }

                break;
            // Clamps up
            case OPEN_INTAKE:
                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            // Clamps down
            case CLAMP:
                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case DISABLED:
                return;
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }

    }

    public enum RobotIntakeState {
        STORE,
        DEPLOY_FORKS,
        OPEN_INTAKE,
        CLAMP,
        DISABLED
    }
}

