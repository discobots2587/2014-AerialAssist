package org.discobots.aerialassist.commands.upperbody;

import org.discobots.aerialassist.commands.CommandBase;

public class AutonomousIntake extends CommandBase {

    private final long maxRunTime;
    private long startTime;
    double power;

    public AutonomousIntake(double p, int time) {
        requires(intakeSub);
        maxRunTime = time;
        power = p;
    }

    protected void initialize() {
        intakeSub.setIntakeSpeed(0);
        startTime = System.currentTimeMillis();
    }

    protected void execute() {
        intakeSub.setIntakeSpeed(power);
    }

    protected boolean isFinished() {
        return System.currentTimeMillis() - startTime > maxRunTime;
    }

    protected void end() {
        intakeSub.setIntakeSpeed(0);
    }

    protected void interrupted() {
        end();
    }
}
