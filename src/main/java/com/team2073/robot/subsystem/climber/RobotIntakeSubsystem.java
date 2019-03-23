package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;

public class RobotIntakeSubsystem implements PeriodicRunnable, StateSubsystem<RobotIntakeState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
    private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();
//    private Solenoid footSolenoid = appCtx.getFootDeploySolenoid();

    private boolean timeStart;

    private static final double DEPLOY_FORKS_WAIT_TIME = 0.25;

    private Timer timer = new Timer();

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

//    public void dropFoot(){
//        footSolenoid.set(true);
//    }

    @Override
    public void onPeriodic() {


        switch (state) {
            // Forks up, clamp down
            case STORE:
//                System.out.println("RUNNING");
                forkSolenoid.set(Value.kReverse);
                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            // Forks down, clamps up
            case DEPLOY_FORKS:
                forkSolenoid.set(Value.kForward);
                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
                System.out.println("SET DOWN");
//                if (!timeStart){
//                    timer.start();
//                    timeStart = true;
//                }
//                if (timer.hasWaited(ConversionUtil.secondsToMs(DEPLOY_FORKS_WAIT_TIME))) {
//                    robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
//                    timer.stop();
//                    timeStart = false;
//                }

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
                break;
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

