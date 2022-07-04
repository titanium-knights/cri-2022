package io.github.titanium_knights.cri2022;
import android.widget.ToggleButton;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import io.github.titanium_knights.util.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp @Config
public class Teleop extends OpMode {
    MecanumDrive drive;
    Slides slides;
    Carriage carriage;
    TubeIntake intake;
    Carousel carousel;
    OdometryRetraction odometry;
    CapstoneMechanism capstone;
    Claw claw;

    ButtonToggler btnX; //used for capstone claw
    ButtonToggler btnB; //used for carriage trapdoor

    public static double DRIVE_SPEED = 0.5;

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

        btnX = new ButtonToggler();
        btnB = new ButtonToggler();

        odometry.retract();
    }

    @Override
    public void loop() {
        //driving
        drive.teleOpRobotCentric(gamepad1, DRIVE_SPEED);

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
        else{
            capstone.setManualPower(0);
        }

        //capstone -- claw (using button toggler)
        btnX.ifRelease(gamepad1.x);
        btnX.update(gamepad1.x);
        if(btnX.getMode()) {
            claw.grab();
        }
        else{
            claw.release();
        }

        telemetry.addData("carriage val", carriage.getArmPosition());
        telemetry.addData("slides val", slides.getCurrentPosition());

        //slides
        if(gamepad1.y){
            if ((carriage.getArmPosition() < Carriage.ARM_SAFE_POSITION) && (slides.getCurrentPosition() < Slides.CARRIAGE_STUCK_THRESHOLD)) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION); //move arm up
            }
            else {
                slides.runToPosition(Slides.MAX_POSITION);
            }
        }
        else if(gamepad1.a){
            if ((slides.getCurrentPosition() > Slides.MAX_POSITION-500) && (carriage.getArmPosition() > Carriage.ARM_SAFE_POSITION)) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION); //move arm down before retracts
            }
            else {
                slides.runToPosition(Slides.MIN_POSITION+500);
            }
            slides.runToPosition(Slides.MIN_POSITION+500);

        }

        //carriage -- ramp
        if (gamepad1.dpad_left) {
            carriage.setRampPos(Carriage.RAMP_OPEN);
        }
        else if (gamepad1.dpad_right) {
            carriage.setRampPos(Carriage.RAMP_CLOSE);
        }

        //carriage --arm
        if (slides.getCurrentPosition() > Slides.CARRIAGE_STUCK_THRESHOLD) {
            if (Math.abs(gamepad1.right_trigger) > 0.1 && carriage.getArmPosition() < Carriage.ARM_MAX) {
                carriage.setManualMode();
                carriage.setArmPower(gamepad1.right_trigger);
            } else if (Math.abs(gamepad1.left_trigger) > 0.1) {
                carriage.setManualMode();
                carriage.setArmPower(-gamepad1.left_trigger);
            } else {
                carriage.setArmPower(0);
            }
        }

        //carriage --trapdoor (using button toggler)
        btnB.ifRelease(gamepad1.b);
        btnB.update(gamepad1.b);
        if(btnB.getMode()) {
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_DUMP_POS);
            telemetry.addData("trapdoor open?", true);
        }
        else{
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_IDLE_POS);
            telemetry.addData("trapdoor open?", false);
        }
        telemetry.update();
    }
}
