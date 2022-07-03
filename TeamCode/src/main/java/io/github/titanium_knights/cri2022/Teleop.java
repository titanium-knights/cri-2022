package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import io.github.titanium_knights.util.*;
@TeleOp
public class Teleop extends OpMode {
    MecanumDrive drive;
    Slides slides;
    Carriage carriage;
    TubeIntake intake;
    Carousel carousel;
    OdometryRetraction odometry;

    double driveSpeed = 0.5;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        carriage = new Carriage(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        carousel = new Carousel(hardwareMap);
        odometry = new OdometryRetraction(hardwareMap);

        odometry.retract();
    }

    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1, driveSpeed);
        //intake
        if (Math.abs(gamepad1.right_trigger) > 0.1) {
            intake.setPower(gamepad1.right_trigger);
        }
        else if (Math.abs(gamepad1.left_trigger) > 0.1) {
            intake.setPower(-gamepad1.left_trigger);
        }
        else {
            intake.stop();
        }

        //carousel
        if(gamepad1.dpad_left) {
            carousel.spinReverse();
        }
        else if(gamepad1.dpad_right){
            carousel.spin();
        }
        else{
            carousel.stop();
        }










    }
}
