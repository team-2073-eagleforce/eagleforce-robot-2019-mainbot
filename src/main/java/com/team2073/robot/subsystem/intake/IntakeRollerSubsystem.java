package com.team2073.robot.subsystem.intake;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.conf.ApplicationProperties;
import com.team2073.robot.conf.MotorDirectionalityProperties;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import edu.wpi.first.wpilibj.SpeedController;

import static com.team2073.robot.subsystem.intake.IntakeRollerSubsystem.IntakeRollerState;

public class IntakeRollerSubsystem implements PeriodicRunnable, StateSubsystem<IntakeRollerState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();
    private ApplicationProperties applicationProperties = robotCtx.getPropertyLoader().registerPropContainer(ApplicationProperties.class);
    private MotorDirectionalityProperties directionalityProperties = applicationProperties.getMotorDirectionalityProperties();


    private SpeedController intakeRoller = appCtx.getIntakeRoller();
    private SpeedController intakeRoller2 = appCtx.getIntakeRoller2();

    private IntakeRollerState state = IntakeRollerState.STOP;

    public IntakeRollerSubsystem() {
        autoRegisterWithPeriodicRunner();
        intakeRoller.setInverted(directionalityProperties.isIntakeRoller());
        intakeRoller2.setInverted(directionalityProperties.isIntakeRoller2());
    }


    @Override
    public void onPeriodic() {
        if(appCtx.getElevatorSubsystem().getCurrentState() != ElevatorSubsystem.ElevatorState.CLIMBING){

            setPower(state.getPercent());
        }else{
            if(appCtx.getController().getRawButton(1)){
                setPower(IntakeRollerState.INTAKE_SPEED.percent);
            }else{
                setPower(state.getPercent());
            }
        }

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
        INTAKE_SPEED(1d),
        OUTTAKE_SPEED(-.9),
        STOP(0d),
        CLIMB_ROLL(.3),
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
