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

    private boolean haveHatch;

    private double prevDistance;


    @Override
    public HatchState currentState() {
        return state;
    }

    @Override
    public void set(HatchState goalState) {
        this.state = goalState;
    }

    public enum HatchState {
        //Format [name of state] + (the state of the fingers, the state of the vertical piston)
        STARTING_CONFIG(DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kReverse),
        //Starting config: (fingers are kReverse, or false, meaning that they are in their non grabbing position,
        //vertical piston if set to kReverse, meaning that it is in its default position (vertical)
        READY_TO_INTAKE(DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward),
        //Intake: (fingers are contracted to allow hatch, the vertical piston is now horizontal
        GRABED_HATCH(DoubleSolenoid.Value.kReverse, DoubleSolenoid.Value.kOff),
        //Hatch is Grabbed: the fingers return to default position to secure hatch
        //the vertical piston is in its previous state AKA kOff
        RELEASE_HATCH(DoubleSolenoid.Value.kForward, DoubleSolenoid.Value.kForward);
        //Release: the fingers contract once more to allow the hatch to be pushed out,
        //the vertical piston is once again horizontal, just to make sure that there
        //are no state errors with using the previous state //

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
        checkForHatch();
        System.out.println(ultrasonicSample());
    }

    public Double ultrasonicSample() {
        double distance = ultraSensor.getRangeInches(); // reads the range on the ultrasonic sensor

        if (distance - prevDistance >= 10) {
            return prevDistance;
        }else {
            prevDistance = distance;
            return distance;
        }

    }


    public void checkForHatch() {
        if (ultrasonicSample() <= 3) {
            haveHatch = true;
        } else {
            haveHatch = false;
        }

    }
       public boolean hatchDetected() {
        return haveHatch;
    }


}
