package com.team2073.robot.command.robotIntake;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.mediator.Mediator;
import com.team2073.robot.subsystem.climber.RobotIntakeSubsystem;

public class RobotIntakeDeployForks extends AbstractLoggingCommand {
    private ApplicationContext appCtx = ApplicationContext.getInstance();
    private Mediator mediator = appCtx.getMediator();

    @Override
    protected void initializeDelegate() {
        mediator.robotIntake(RobotIntakeSubsystem.RobotIntakeState.DEPLOY_FORKS);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
