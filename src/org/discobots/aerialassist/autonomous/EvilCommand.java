package org.discobots.aerialassist.autonomous;

import org.discobots.aerialassist.autonomous.utils.AngleSensor;
import org.discobots.aerialassist.autonomous.utils.LinearMotionSensor;
import org.discobots.aerialassist.autonomous.utils.MotorOutput;
import org.discobots.aerialassist.autonomous.utils.Pose;
import org.discobots.aerialassist.commands.CommandBase;

/**
 *
 * @author Nolan Shah
 */
public class EvilCommand extends CommandBase {
    
    
    AngleSensor gyroscope;
    LinearMotionSensor encoder;
    Localizer localizer;
    Navigator navigator;
    MotorOutput leftOutput;
    MotorOutput rightOutput;
    float left, right;
    static final Pose[] poses = {
    new Pose(0, 10, 180),
    new Pose(5, 10, 270)
    };
            
    public EvilCommand() {
        requires(drivetrainSub);
        requires(launcherSub);
        requires(intakeSub);
        requires(compressorSub);
        gyroscope = new AngleSensor() {
            public float getAngle() {
                return (float) drivetrainSub.getGyroscopeAngle();
            }
        };
        encoder = new LinearMotionSensor() {
            public float getMotionOnAxisYSinceLastCallInches() {
                return (float) drivetrainSub.getEncoderForwardDistance() * 7 / 9;
            }

            public float getMotionOnAxisXSinceLastCallInches() {
                return 0.0f;
            }
        };
        leftOutput = new MotorOutput() {
            public void setOutput(float val) {
                left = val;
            }
        };
        rightOutput = new MotorOutput() {
            public void setOutput(float val) {
                right = val;
            }
        };
        localizer = new Localizer(gyroscope, encoder, new Pose(0, 0, 0));
        navigator = new Navigator(leftOutput, rightOutput, localizer);
    }

    protected void initialize() {
        compressorSub.on();
        navigator.setWaypoints(poses);
        try {
            navigator.start();
        } catch (Exception e) {}
    }

    protected void execute() {
        drivetrainSub.tankDrive(left, right);
        System.out.println(localizer.getCurrentPose().getX() + " " + localizer.getCurrentPose().getY() + " " + localizer.getCurrentPose().getNormalizedAngle() + " " + left + " " + right);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        navigator.stop();
        drivetrainSub.tankDrive(0, 0);
        launcherSub.fire(false);
        intakeSub.setExtended(false);
        intakeSub.setIntakeSpeed(0);
    }

    protected void interrupted() {
        end();
    }
    
}
