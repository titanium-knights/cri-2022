package io.github.titanium_knights.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.jetbrains.annotations.NotNull;

public class Carousel {
    public CRServo motor;
    public CRServo motor2;

    public Carousel(HardwareMap hmap){
        motor = hmap.get(CRServo.class,"carousel_l");
        motor2 = hmap.get(CRServo.class, "carousel_r");
    }
    public void setPower(double power) {
        motor.setPower(power);
        motor2.setPower(-power);
    }
    public void spin(boolean fast){
        setPower(fast ? (1) : 0.5);
    }
    public void spin() {
        this.spin(false);
    }
    public void stop(){
        setPower(0);
    }
    public void spinReverse(boolean fast){ setPower(fast ? (-1) : -0.5); }
    public void spinReverse() {
        this.spinReverse(false);
    }


}