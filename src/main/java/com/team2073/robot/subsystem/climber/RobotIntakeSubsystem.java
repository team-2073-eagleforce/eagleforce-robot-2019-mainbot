package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.ExceptionUtil;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;

public class RobotIntakeSubsystem implements PeriodicRunnable, StateSubsystem<RobotIntakeState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private Timer timer = new Timer();
    private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
    private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();

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
        switch(state){
            case STORE:
                forkSolenoid.set(DoubleSolenoid.Value.kReverse);
                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case DEPLOY_FORKS:
                forkSolenoid.set(DoubleSolenoid.Value.kForward);
                timer.start();
                if(timer.getElapsedTime() > 1){
                    robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                }
                break;
            case OPEN_INTAKE:
                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            case CLAMP:
                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            default:
                throw new IllegalStateException();
        }

    }

    public enum RobotIntakeState {
        STORE,
        DEPLOY_FORKS,
        OPEN_INTAKE,
        CLAMP
        }
    }

