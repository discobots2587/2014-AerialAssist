/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.discobots.aerialassist.autonomous.utils;

/**
 *
 * @author nolan
 */
public interface LinearMotionSensor {
    public abstract float getMotionOnAxisYSinceLastCallInches(); // forward/back
    public abstract float getMotionOnAxisXSinceLastCallInches(); // left/right
}