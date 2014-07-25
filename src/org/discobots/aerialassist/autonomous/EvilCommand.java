package org.discobots.aerialassist.autonomous;

import org.discobots.aerialassist.commands.CommandBase;

/**
 *
 * @author Nolan Shah
 */
public class EvilCommand extends CommandBase {
    
    public EvilCommand() {
        requires(drivetrainSub);
        requires(launcherSub);
        requires(intakeSub);
        requires(compressorSub);
    }

    protected void initialize() {
        compressorSub.on();
    }

    protected void execute() {
        
        
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        drivetrainSub.tankDrive(0, 0);
        launcherSub.fire(false);
        intakeSub.setExtended(false);
        intakeSub.setIntakeSpeed(0);
    }

    protected void interrupted() {
        end();
    }
    
}
