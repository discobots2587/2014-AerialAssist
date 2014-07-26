/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.discobots.aerialassist.autonomous;

import com.sun.squawk.util.MathUtils;
import org.discobots.aerialassist.autonomous.utils.MotorOutput;
import org.discobots.aerialassist.autonomous.utils.Pose;

/**
 *
 * @author nolan
 */
public class Navigator {

    private int index;
    private Pose[] poses;

    private MotorOutput leftSide;
    private MotorOutput rightSide;

    private Localizer localizer;

    private boolean running;

    public Navigator(MotorOutput left, MotorOutput right, Localizer localizer) {
        this.leftSide = left;
        this.rightSide = right;
        this.localizer = localizer;
        running = false;
    }

    public void setWaypoints(Pose[] poses) {
        stop();
        this.poses = poses;
        index = 0;
    }

    public void start() throws Exception {
        if (leftSide == null || rightSide == null) {
            throw new NullPointerException("failed to set motor outputs");
        }
        if (poses.length == 0 || poses == null) {
            throw new IndexOutOfBoundsException("failed to define waypoints");
        }
        running = true;

    }

    public void stop() {
        if (running) {
            leftSide.setOutput(0);
            rightSide.setOutput(0);
        }
        running = false;

    }

    private class NavigatorUpdateThread extends Thread {

        public void run() {
            float accumulatedAngleError = 0.0f;
            while (true) {
                if (running) {
                    Pose currentPose = localizer.getCurrentPose();
                    Pose targetPose = poses[index];
                    float xError = targetPose.getX() - currentPose.getX();
                    float yError = targetPose.getY() - currentPose.getY();
                    float distanceError = (float) Math.sqrt(xError * xError + yError * yError);
                    if (Math.abs(distanceError) > 3.0) {
                        // we aren't at our target yet
                        float targetAngle = (float) Math.toDegrees(MathUtils.atan2(yError, xError));
                        float angleError = Pose.normalizeAngle(currentPose.getAngle(), 360.0f) - Pose.normalizeAngle(targetAngle, 360.0f);
                        // this (angleError ^^^) might turn the robot only one way because of stupid problems. need to look for historic AngleController or FixAngle 
                        if (Math.abs(angleError) > 5.0) {
                            // we need to turn before we move to target
                            float kP = -1.0f / 45.0f;
                            float output = angleError * kP;
                            leftSide.setOutput(-output);
                            rightSide.setOutput(output);
                        } else {
                            // out move angle is good, we can move to target now
                            float kP_distance = 1.0f / 36.0f;
                            float kP_angle = -1.0f / 25.0f;
                            float outputDistance = distanceError * kP_distance;
                            float outputAngle = angleError * kP_angle;
                            leftSide.setOutput(outputDistance - outputAngle);
                            rightSide.setOutput(outputDistance + outputAngle);
                        }
                    } else {
                        // we are at our target, now time to turn to the target angle
                        float angleError = Pose.normalizeAngle(currentPose.getAngle(), 360.0f) - Pose.normalizeAngle(targetPose.getAngle(), 360.0f);
                        // this (angleError ^^^) might turn the robot only one way because of stupid problems. need to look for historic AngleController or FixAngle
                        if (Math.abs(angleError) > 2.0) {
                            float kP = -1.0f / 45.0f;
                            float output = angleError * kP;
                            leftSide.setOutput(-output);
                            rightSide.setOutput(output);
                        } else {
                            index++;
                        }
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }

}
