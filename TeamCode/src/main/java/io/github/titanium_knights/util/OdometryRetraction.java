package io.github.titanium_knights.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config public class OdometryRetraction {
    private final Servo vertical;
    private final Servo horizontal;

    public static String VERTICAL_NAME = "vertical_odo";
    public static String HORIZONTAL_NAME = "horizontal_odo";

    public static double VERTICAL_EXTENDED_POS = 0;
    public static double HORIZONTAL_EXTENDED_POS = 0.1;
    public static double VERTICAL_RETRACTED_POS = 0.9;
    public static double HORIZONTAL_RETRACTED_POS = 0.9;

    public Servo getVertical() {
        return vertical;
    }

    public Servo getHorizontal() {
        return horizontal;
    }

    public OdometryRetraction(HardwareMap hardwareMap) {
        vertical = hardwareMap.servo.get(VERTICAL_NAME);
        horizontal = hardwareMap.servo.get(HORIZONTAL_NAME);
    }

    public boolean isRetracted() {
        double position = vertical.getPosition();
        return Math.abs(position - VERTICAL_EXTENDED_POS) > Math.abs(position - VERTICAL_RETRACTED_POS);
    }

    public void extend() {
        vertical.setPosition(VERTICAL_EXTENDED_POS);
        horizontal.setPosition(HORIZONTAL_EXTENDED_POS);
    }

    public void retract() {
        vertical.setPosition(VERTICAL_RETRACTED_POS);
        horizontal.setPosition(HORIZONTAL_RETRACTED_POS);
    }
}
