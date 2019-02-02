package com.team2073.robot.subsystem.intake;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.SpeedController;

import static com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState;
import static com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState.DISABLED;

public class IntakeRollerSubsystem implements PeriodicRunnable, StateSubsystem<IntakeRollerState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private SpeedController intakeRoller = appCtx.getIntakeRoller();
    private SpeedController intakeRoller2 = appCtx.getIntakeRoller2();

    private IntakeRollerState state = IntakeRollerState.STOP;

    public IntakeRollerSubsystem() {
        autoRegisterWithPeriodicRunner();
    }


    @Override
    public void onPeriodic() {
        if (state == DISABLED) {
            return;
        }
        
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

    public enum IntakeRollerState {
        INTAKE_SPEED(.9),
        OUTTAKE_SPEED(-.9),
        STOP(0d),
        DISABLED(null);

        private Double percent;

        IntakeRollerState(Double percent) {
            this.percent = percent;
        }

        public Double getPercent() {
            return percent;
        }
    }
}
