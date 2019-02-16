package com.team2073.robot.subsystem.climber;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.mediator.condition.Condition;
import com.team2073.common.mediator.condition.StateBasedCondition;
import com.team2073.common.mediator.subsys.ColleagueSubsystem;
import com.team2073.common.mediator.subsys.StateBasedSubsystem;
import com.team2073.common.mediator.subsys.SubsystemStateCondition;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.common.util.ConversionUtil;
import com.team2073.common.util.Timer;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.jetbrains.annotations.NotNull;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;
import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState.DEPLOY_FORKS;
import static com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState.DISABLED;

public class RobotIntakeSubsystem implements PeriodicRunnable, StateSubsystem<RobotIntakeState>, StateBasedSubsystem<RobotIntakeState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private DoubleSolenoid forkSolenoid = appCtx.getForkDeploySolenoid();
    private DoubleSolenoid robotGrabSolenoid = appCtx.getRobotGrabSolenoid();

    private boolean timeStart;

    private static final double DEPLOY_FORKS_WAIT_TIME = 0.25;

    private Timer timer = new Timer();

    private RobotIntakeState state = RobotIntakeState.STORE;

    public RobotIntakeSubsystem() {
        autoRegisterWithPeriodicRunner();
        appCtx.getCommonMediator().registerColleague((ColleagueSubsystem)this);
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

//        switch (state) {
//            // Forks up, clamp down
//            case STORE:
//                forkSolenoid.set(DoubleSolenoid.Value.kForward);
//                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
//                break;
//            // Forks down, clamps up
//            case DEPLOY_FORKS:
//                forkSolenoid.set(DoubleSolenoid.Value.kReverse);
////                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
//                if (!timeStart){
//                    timer.start();
//                    timeStart = true;
//                }
//                if (timer.hasWaited(ConversionUtil.secondsToMs(DEPLOY_FORKS_WAIT_TIME))) {
//                    robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
//                    timer.stop();
//                    timeStart = false;
//                }
//
//                break;
//            // Clamps up
//            case OPEN_INTAKE:
//                robotGrabSolenoid.set(DoubleSolenoid.Value.kReverse);
//                break;
//            // Clamps down
//            case CLAMP:
//                robotGrabSolenoid.set(DoubleSolenoid.Value.kForward);
//                break;
//            case DISABLED:
//                break;
//            default:
//                throw new IllegalStateException("Unknown state: " + state);
//        }

    }

    @NotNull
    @Override
    public Condition<RobotIntakeState> getCurrentCondition() {
        return new StateBasedCondition(state);
    }

    public enum RobotIntakeState implements SubsystemStateCondition {
        STORE,
        DEPLOY_FORKS,
        OPEN_INTAKE,
        CLAMP,
        DISABLED
    }
}

