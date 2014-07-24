package org.discobots.aerialassist.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.discobots.aerialassist.HW;
import org.discobots.aerialassist.commands.drive.CheesyArcadeDrive;
import org.discobots.aerialassist.utils.DiscoGyro;
import org.discobots.aerialassist.utils.UltrasonicSRF02_I2C;

public class Drivetrain extends Subsystem {

    private Talon leftFront;
    private Talon leftMiniFront;
    private Talon leftRear;
    private Talon leftMiniRear;
    private Talon rightFront;
    private Talon rightMiniFront;
    private Talon rightRear;
    private Talon rightMiniRear;
    private RobotDrive drive;
    private RobotDrive miniDrive;
    
    private DiscoGyro gyroscope;
    private Encoder forwardEncoder;
    private UltrasonicSRF02_I2C ultrasonicIntake;
    private UltrasonicSRF02_I2C ultrasonicShooter;
    
    private Relay decorativeLeds;
    private Relay functionalLeds;
    
    boolean useMini = true;

    public Drivetrain() {
        super("Drivetrain");
        leftFront = new Talon(HW.motorModule, HW.leftFrontMotor);
        leftMiniFront = new Talon(HW.motorModule, HW.leftFrontMiniMotor);
        leftRear = new Talon(HW.motorModule, HW.leftRearMotor);
        leftMiniRear = new Talon(HW.motorModule, HW.leftRearMiniMotor);
        rightFront = new Talon(HW.motorModule, HW.rightFrontMotor);
        rightMiniFront = new Talon(HW.motorModule, HW.rightFrontMiniMotor);
        rightRear = new Talon(HW.motorModule, HW.rightRearMotor);
        rightMiniRear = new Talon(HW.motorModule, HW.rightRearMiniMotor);
        drive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);
        miniDrive = new RobotDrive(leftMiniFront, leftMiniRear, rightMiniFront, rightMiniRear);

        drive.setSafetyEnabled(false);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true); // Should be false
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true); // Should be false

        miniDrive.setSafetyEnabled(false);
        miniDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        miniDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        miniDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true); // Should be false
        miniDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true); // Should be false

        gyroscope = new DiscoGyro(HW.gyroscope);
        
        forwardEncoder = new Encoder(HW.forwardEncoderA, HW.forwardEncoderB);
        forwardEncoder.setDistancePerPulse(HW.distancePerPulse);
        forwardEncoder.start();
        
        ultrasonicIntake = new UltrasonicSRF02_I2C(224);
        ultrasonicShooter = new UltrasonicSRF02_I2C(242);
        
        decorativeLeds = new Relay(1, HW.decorativeALedRelay);
        functionalLeds = new Relay(1, HW.functionalLedRelay);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new CheesyArcadeDrive());
    }

    public void tankDrive(double leftVal, double rightVal) {
        drive.tankDrive(leftVal, rightVal);
        if (this.useMini) {
            miniDrive.tankDrive(leftVal, rightVal);
        }
    }

    public void setMiniCimUsage(boolean a) {
        this.useMini = a;
    }
    
    public double getGyroscopeAngle() {
        return this.gyroscope.getAngle();
    }
    
    public double getEncoderForwardDistance() {
        return this.forwardEncoder.getDistance();
    }
    
    public int getUltrasonicIntakeAverageValue() {
        return this.ultrasonicIntake.getAverageValue();
    }
    
    public int getUltrasonicShooterAverageValue() {
        return this.ultrasonicShooter.getAverageValue();
    }
    
    public boolean getMiniCimUsage() {
        return this.useMini;
    }
    
    boolean decorativeLEDState;
    public void writeDecorativeLEDState(boolean state) {
        if (state) {
            this.decorativeLeds.set(Relay.Value.kOn);
        } else {
            this.decorativeLeds.set(Relay.Value.kOff);
        }
        this.decorativeLEDState = state;
    }
    
    public boolean getDecorativeLEDState() {
        return this.decorativeLEDState;
    }
    
    boolean functionalLEDState;
    public void writeFunctionalLEDState(boolean state) {
        if (state) {
            this.functionalLeds.set(Relay.Value.kOn);
        } else {
            this.functionalLeds.set(Relay.Value.kOff);
        }
        this.functionalLEDState = state;
    }
    
    public boolean getFunctionalLEDState() {
        return this.functionalLEDState;
    }
}
