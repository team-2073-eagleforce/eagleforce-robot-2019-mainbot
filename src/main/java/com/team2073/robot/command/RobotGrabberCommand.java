package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem.RobotIntakeState;

public class RobotGrabberCommand extends AbstractLoggingCommand {

    private ApplicationContext appctx = ApplicationContext.getInstance();
    private RobotIntakeSubsystem roboIntake = appctx.getRobotIntakeSubsystem();
    private final RobotIntakeState state;

    public RobotGrabberCommand(RobotIntakeState state) {
        this.state = state;
    }

    @Override
    protected void initializeDelegate() {
        roboIntake.set(state);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}