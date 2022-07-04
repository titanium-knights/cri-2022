package io.github.titanium_knights.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.*;
import org.jetbrains.annotations.NotNull;

@Config
public class Claw {
    public Servo claw;
    public static double releasePos = 1.0;
    public static double grabPos = 0.67;
//    public static double armMultiplier = -0.5;

    public Claw(HardwareMap hardwareMap) {
        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public boolean isGrabbed() {
        if (claw.getPosition()==grabPos) {
            return true;
        }
        else {
            return false;
        }
    }

    public void grab() {
        claw.setPosition(grabPos);
    }

    public void release() {
        claw.setPosition(releasePos);
    }

}