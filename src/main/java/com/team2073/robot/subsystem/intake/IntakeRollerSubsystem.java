package com.team2073.robot.subsystem.intake;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.SpeedController;

import static com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState;

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
        switch (state) {
            case INTAKE_SPEED:
                intakeRoller.set(IntakeRollerState.INTAKE_SPEED.getPercent());
                intakeRoller2.set(IntakeRollerState.INTAKE_SPEED.getPercent());
                break;
            case OUTTAKE_SPEED:
                intakeRoller.set(IntakeRollerState.OUTTAKE_SPEED.getPercent());
                intakeRoller2.set(IntakeRollerState.OUTTAKE_SPEED.getPercent());
            case STOP:
                intakeRoller.set(IntakeRollerState.STOP.getPercent());
                intakeRoller2.set(IntakeRollerState.STOP.getPercent());
            case DISABLED:
                intakeRoller.set(0);
                intakeRoller2.set(0);
        }
    }

    @Override
    public IntakeRollerState currentState() {
        return state;
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
