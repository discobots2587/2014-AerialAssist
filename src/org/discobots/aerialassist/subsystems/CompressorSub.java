package org.discobots.aerialassist.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.discobots.aerialassist.HW;
import org.discobots.aerialassist.utils.PressureSensor;

public class CompressorSub extends Subsystem {
  
    PressureSensor pressureSensor;
    Compressor compressor;
    
    public CompressorSub() {
        super("Compressor");
        compressor = new Compressor(1, HW.pressureSwitch, 1, HW.compressorRelay);
        pressureSensor = new PressureSensor(HW.pressureSensor);
    }

    public void initDefaultCommand() {
    }
    
    public void on() {
        compressor.start();
    }

    public void off() {
        compressor.stop();
    }
    
    public int getPressurePSIInt() {
        return this.pressureSensor.getInteger();
    }
    
    public double getPressurePSIDou() {
        return this.pressureSensor.getDouble();
    }
    
    public double getPressureVoltage() {
        return this.pressureSensor.getVoltage();
    }

    public boolean isEnabled() {
        return compressor.enabled();
    }

    public boolean isFull() {
        return compressor.getPressureSwitchValue();
    }
}
