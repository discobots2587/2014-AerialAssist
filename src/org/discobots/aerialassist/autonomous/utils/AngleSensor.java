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
public interface AngleSensor {
    public abstract float getAngle();
    // requirements for output are that 0 is forward, right is +, left is -
}
