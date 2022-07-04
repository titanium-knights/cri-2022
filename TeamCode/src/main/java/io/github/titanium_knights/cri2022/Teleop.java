package io.github.titanium_knights.cri2022;

import android.widget.ToggleButton;

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
    CapstoneMechanism capstone;
    Claw claw;

    int extension = Slides.MAX_POSITION;
    int retraction = Slides.MIN_POSITION+500;

    double driveSpeed = 0.5;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        carriage = new Carriage(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        carousel = new Carousel(hardwareMap);
        odometry = new OdometryRetraction(hardwareMap);
        capstone = new CapstoneMechanism(hardwareMap);
        claw = new Claw(hardwareMap);
        odometry.retract();
    }

    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1, driveSpeed);
        //intake
        if (gamepad1.right_bumper) {
            intake.setPower(0.5);
        }
        else if (gamepad1.left_bumper) {
            intake.setPower(-0.5);
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

        //capstone
        if(gamepad1.dpad_up) {
            capstone.setPosition(CapstoneMechanism.idle);
        }
        else if(gamepad1.dpad_down) {
            capstone.setPosition(CapstoneMechanism.pickup);
        }
        else if(gamepad1.right_bumper) {
            capstone.setManualPower(-.5);
        }
        else{
            capstone.setManualPower(0);
        }

        //claw for capstone
        if(gamepad1.x) {
            claw.grab();
        }
        else if(gamepad1.b) {
            claw.release();
        }

        //slides
        if(gamepad1.y){
            slides.runToPosition(extension);
        }
        else if(gamepad1.a){
            slides.runToPosition(retraction);
        }

        telemetry.addData("slide posisiton", slides.getCurrentPosition());
        telemetry.addData("extension", extension);
        telemetry.addData("retraction", retraction);

        //carriage -- ramp
        if (slides.getCurrentPosition() < slides.RAMP_OPEN_THRESHOLD) {
            carriage.setRampPos(carriage.RAMP_OPEN);

        }
        else if (slides.getCurrentPosition() > slides.RAMP_CLOSE_THRESHOLD) {
            carriage.setRampPos(carriage.RAMP_CLOSE);
        }


        if (Math.abs(gamepad1.right_trigger) > 0.1) {
            carriage.setArmPower(gamepad1.right_trigger);
        }
        else if (Math.abs(gamepad1.left_trigger) > 0.1) {
            carriage.setArmPower(-gamepad1.left_trigger);
        }
        else {
            carriage.setArmPower(0);
        }

        //carriage --trapdoor
        if (gamepad1.b) {
            carriage.setTrapdoorPos(carriage.DUMP_POS);
        }










    }
}
