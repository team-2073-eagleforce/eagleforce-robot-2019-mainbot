package com.team2073.robot.command;

import com.team2073.common.command.AbstractLoggingCommand;
import com.team2073.robot.ctx.ApplicationContext;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DriveShiftCommand extends AbstractLoggingCommand {

    private DoubleSolenoid.Value value;

    public DriveShiftCommand(DoubleSolenoid.Value value){
        this.value = value;
    }
    @Override
    protected void initializeDelegate() {
        ApplicationContext.getInstance().getDrivetrainSubsystem().shiftDrivetrain(value);
    }

    @Override
    protected boolean isFinishedDelegate() {
        return true;
    }
}
