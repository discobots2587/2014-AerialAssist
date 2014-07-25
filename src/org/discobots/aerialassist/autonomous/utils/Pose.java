package org.discobots.aerialassist.autonomous.utils;

/**
 *
 * @author Nolan Shah
 */
public class Pose {
    private float x, y;
    private float angle;
    
    public Pose(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public Pose clone() {
        return new Pose(x, y, angle);
    }
    
    public float getNormalizedAngle() {
        return Pose.normalizeAngle(angle, 0.0f);
    }
    
    public static float normalizeAngle(float a, float low) {
        float b = a;
        while (b < low) {
            b += 360.0f;
        }
        while (b >= low + 360.0f) {
            b -= 360.0f;
        }
        return b;
    }
    
}
