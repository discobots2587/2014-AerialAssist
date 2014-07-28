package org.discobots.aerialassist.commands.upperbody;

import org.discobots.aerialassist.commands.CommandBase;

public class FireLauncher extends CommandBase {

    private boolean shoot;
    private int check;
    public long startTime;
    private long maxRunTime;
    public static final boolean FIRE = true;
    public static final boolean LOAD = false;
    private long time = 0;
    
    public FireLauncher(boolean fire, int ch) {
        requires(launcherSub);
        check = ch;
        shoot = fire;
        maxRunTime = 1500;
        switch (check) {
            case 1://150, 500 A
                maxRunTime = 200;
                break;
            case 3://1000 B
                maxRunTime = 500;
                break;
        }
    }

    protected void initialize() {
        startTime = System.currentTimeMillis();
        System.out.println("[Debug] Firing a shot of type " + check + " with a run time of " + maxRunTime + " ms.");
    }

    protected void execute() {
        if (intakeSub.isExtended()) {
            launcherSub.fire(shoot);
        }
        time = System.currentTimeMillis();
    }

    protected boolean isFinished() {
        if (System.currentTimeMillis() - startTime >= maxRunTime) {
            System.out.println("[Debug] Shot completed in time " + (time - startTime));
            return true;
        } else {
          return false;  
        }
    }

    protected void end() {
        launcherSub.fire(false);
    }

    protected void interrupted() {
        end();
    }
}
