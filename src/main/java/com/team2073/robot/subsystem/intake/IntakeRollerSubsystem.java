package com.team2073.robot.subsystem.intake;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.mediator.condition.Condition;
import com.team2073.common.mediator.condition.StateBasedCondition;
import com.team2073.common.mediator.subsys.ColleagueSubsystem;
import com.team2073.common.mediator.subsys.StateBasedSubsystem;
import com.team2073.common.mediator.subsys.SubsystemStateCondition;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.SpeedController;
import org.jetbrains.annotations.NotNull;

import static com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState;

public class IntakeRollerSubsystem implements PeriodicRunnable, StateSubsystem<IntakeRollerState>, StateBasedSubsystem<IntakeRollerState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();


    private SpeedController intakeRoller = appCtx.getIntakeRoller();
    private SpeedController intakeRoller2 = appCtx.getIntakeRoller2();

    private IntakeRollerState state = IntakeRollerState.STOP;

    public IntakeRollerSubsystem() {
        autoRegisterWithPeriodicRunner();
        appCtx.getCommonMediator().registerColleague((ColleagueSubsystem)appCtx.getIntakeRollerSubsystem());
        intakeRoller.setInverted(directionalityProperties.isIntakeRoller());
        intakeRoller2.setInverted(directionalityProperties.isIntakeRoller2());
    }


    @Override
    public void onPeriodic() {
        setPower(state.getPercent());
    }

    @Override
    public IntakeRollerState currentState() {
        return state;
    }

    public void setPower(Double percent) {
        intakeRoller2.set(percent);
        intakeRoller.set(percent);
    }

    @Override
    public void set(IntakeRollerState goalState) {
        state = goalState;
    }

    @NotNull
    @Override
    public Condition<IntakeRollerState> getCurrentCondition() {
        return new StateBasedCondition(state);
    }

    public enum IntakeRollerState implements SubsystemStateCondition<IntakeRollerState> {
        INTAKE_SPEED(1.),
        OUTTAKE_SPEED(-.9),
        STOP(0d),
        DISABLED(0d);

        private Double percent;

        IntakeRollerState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }
}
