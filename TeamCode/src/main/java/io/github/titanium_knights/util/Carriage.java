package io.github.titanium_knights.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config public class Carriage {
    DcMotor arm;
    Servo trapdoor;
    Servo ramp;

    public static double TRAPDOOR_DUMP_POS = 1; //NEED TO TEST
    public static double TRAPDOOR_IDLE_POS = .5; //NEED TO TEST

    public static double ARM_POWER = 0.5;

    public static double RAMP_OPEN = 0;
    public static double RAMP_CLOSE = 0;

    public Carriage(HardwareMap hmap){
        arm = hmap.get(DcMotor.class, "carriage");
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        ramp = hmap.get(Servo.class, "ramp");
        trapdoor = hmap.get(Servo.class, "trapdoor");
    }

    //arm
    public void setArmPower(double pwr){
        arm.setPower(pwr);
    }
    public int getArmPosition(){
        return arm.getCurrentPosition();
    }
    public void setArmPosition(int pos){
        arm.setTargetPosition(pos);
        arm.setPower(ARM_POWER);
    }
    //setting position trapdoor and ramp
    public void setTrapdoorPos(double pos) {
        trapdoor.setPosition(pos);
    }

    public void setRampPos(double pos) {
        ramp.setPosition(pos);
    }


}