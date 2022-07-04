package io.github.titanium_knights.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config public class Carriage {
    DcMotor arm;
    Servo trapdoor;
    Servo ramp;

    public static int DUMP_POS = -192;
    public static int IDLE_POS = -5;
    public static double CARRIAGE_POWER = 0.5;
    public final String MOTOR_NAME = "carriage";


    public static double RAMP_OPEN = 0;
    public static double RAMP_CLOSE = 0;

    public Carriage(HardwareMap hmap){
        arm = hmap.get(DcMotor.class, MOTOR_NAME);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ramp = hmap.get(Servo.class, "ramp");
        trapdoor = hmap.get(Servo.class, "trapdoor");
    }
    public void setArmPower(double pwr){
        arm.setPower(pwr);
    }
    public int getPosition(){
        return arm.getCurrentPosition();
    }
    public void setPosition(int pos){
        arm.setTargetPosition(pos);
        arm.setPower(CARRIAGE_POWER);
    }
    public void dump(){
        this.setPosition(DUMP_POS);
    }
    public void idle(){
        this.setPosition(IDLE_POS);
    }
    public boolean isBusy() {
        return arm.isBusy();
    }


    //new code
    public void setTrapdoorPos(double pos) {
        trapdoor.setPosition(pos);
    }

    public void setRampPos(double pos) {
        ramp.setPosition(pos);
    }


}