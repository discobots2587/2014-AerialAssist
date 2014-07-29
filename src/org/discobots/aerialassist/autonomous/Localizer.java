package org.discobots.aerialassist.autonomous;

import org.discobots.aerialassist.autonomous.utils.Pose;
import org.discobots.aerialassist.autonomous.utils.AngleSensor;
import org.discobots.aerialassist.autonomous.utils.LinearMotionSensor;

/**
 *
 * @author Nolan Shah
 */
public class Localizer {

    private AngleSensor angleSensor;
    private LinearMotionSensor linearMotionSensor;
    private Pose currentPose;

    private LocalizerUpdateThread localizerUpdateThread;

    public Localizer(AngleSensor angleSensor, LinearMotionSensor linearMotionSensor, Pose initialPose) {
        this.angleSensor = angleSensor;
        this.linearMotionSensor = linearMotionSensor;
        this.currentPose = new Pose(initialPose.getX(), initialPose.getY(), initialPose.getAngle());

        localizerUpdateThread = new LocalizerUpdateThread();
        localizerUpdateThread.start();

    }

    private class LocalizerUpdateThread extends Thread {

        public void run() {
            while (true) {

                float dRobotX = linearMotionSensor.getMotionOnAxisXSinceLastCallInches();
                float dRobotY = linearMotionSensor.getMotionOnAxisYSinceLastCallInches();
                //System.out.println(dRobotX + " " + dRobotY);
                float robotAngle = angleSensor.getAngle();
                float dFieldY = (float) (Math.sin(Math.toRadians(-(robotAngle - 90))) * dRobotX - Math.cos(Math.toRadians(-(robotAngle - 90))) * dRobotY);
                float dFieldX = (float) (Math.cos(Math.toRadians(-(robotAngle - 90))) * dRobotX - Math.sin(Math.toRadians(-(robotAngle - 90))) * dRobotY);

                currentPose.setX(currentPose.getX() + dFieldX);
                currentPose.setY(currentPose.getY() + dFieldY);
                currentPose.setAngle(robotAngle);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }
    
    public Pose getCurrentPose() {
        return this.currentPose.clone();
    }
    
}
