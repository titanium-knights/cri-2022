package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import io.github.titanium_knights.util.OdometryRetraction;

@Autonomous(name = "Extend Odometry")
public class ExtendOdometryOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        OdometryRetraction retraction = new OdometryRetraction(hardwareMap);
        waitForStart();
        retraction.extend();
        sleep(4000);
    }
}
