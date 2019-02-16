package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.common.mediator.Mediator;
import com.team2073.common.mediator.condition.PositionBasedCondition;
import com.team2073.common.mediator.condition.StateBasedCondition;
import com.team2073.common.mediator.request.Request;
import com.team2073.robot.ctx.ApplicationContext;
import com.team2073.robot.subsystem.ElevatorSubsystem;
import com.team2073.robot.subsystem.carriage.ShooterSubsystem;
import org.apache.commons.lang3.Range;

public class MediatorTestingCommand extends AbstractLoggingCommand {

    ApplicationContext applicationContext = ApplicationContext.getInstance();
    Mediator mediator = applicationContext.getCommonMediator();

    @Override
    protected void initializeDelegate() {
        mediator.add(new Request(ElevatorSubsystem.class, new PositionBasedCondition(30d, Range.between(25d, 35d))));
        System.out.println("in command");
//        mediator.add(new Request(ShooterSubsystem.class, new StateBasedCondition(ShooterSubsystem.ShooterState.INTAKE)));
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
