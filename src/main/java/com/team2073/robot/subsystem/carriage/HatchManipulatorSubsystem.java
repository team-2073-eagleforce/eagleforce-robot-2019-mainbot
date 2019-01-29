package com.team2073.robot.subsystem.carriage;

import com.team2073.common.ctx.RobotContext;
import com.team2073.common.periodic.PeriodicRunnable;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.StateSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Ultrasonic;

import static com.team2073.robot.subsystem.carriage.HatchManipulatorSubsystem.*;

public class HatchManipulatorSubsystem implements PeriodicRunnable, StateSubsystem<HatchState> {
    private final RobotContext robotCtx = RobotContext.getInstance();
    private final ApplicationContext appCtx = ApplicationContext.getInstance();

    private DoubleSolenoid hatchPosition = appCtx.getHatchPositionSolenoid();
    private DoubleSolenoid hatchPlace = appCtx.getHatchPlaceSolenoid();
    private Ultrasonic ultraSensor = appCtx.getHatchSensor();

    private HatchState state = HatchState.STARTING_CONFIG;

    @Override
    public HatchState currentState() {
        return state;
    }

    @Override
    public void set(HatchState goalState) {
        this.state = goalState;
    }

    public enum HatchState {
        STARTING_CONFIG(DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kReverse),
        READY_TO_INTAKE(DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward),
        GRABED_HATCH(DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kOff),
        RELEASE_HATCH(DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward);

        private DoubleSolenoid.Value verticalPistonActive;
        private DoubleSolenoid.Value fingerPistonActive;

        public DoubleSolenoid.Value isVerticalPistonActive() {
            return verticalPistonActive;
        }

        public DoubleSolenoid.Value isFingerPistonActive() {
            return fingerPistonActive;
        }

        HatchState(DoubleSolenoid.Value fingerPiston, DoubleSolenoid.Value vertPiston) {
            this.fingerPistonActive = fingerPiston;
            this.verticalPistonActive = vertPiston;
        }
    }

    public HatchManipulatorSubsystem() {
        autoRegisterWithPeriodicRunner();
        ultraSensor.setAutomaticMode(true);
    }

    @Override
    public void onPeriodic() {
       if(ultrasonicSample() >= 3 && ultrasonicSample() <= 36) {
            set(HatchState.READY_TO_INTAKE);
            hatchPosition.set(HatchState.READY_TO_INTAKE.isVerticalPistonActive());
            hatchPlace.set(HatchState.READY_TO_INTAKE.isFingerPistonActive());

        }else if (ultrasonicSample() == 0 && ultrasonicSample() < 3) {
            set(HatchState.GRABED_HATCH);
           hatchPosition.set(HatchState.GRABED_HATCH.isVerticalPistonActive());
           hatchPlace.set(HatchState.GRABED_HATCH.isFingerPistonActive());
       } else {

       }

    }

    public double ultrasonicSample() {
        return ultraSensor.getRangeInches(); // reads the range on the ultrasonic sensor
    }



}
