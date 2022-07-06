package io.github.titanium_knights.cri2022;
import android.widget.Button;
import android.widget.ToggleButton;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import io.github.titanium_knights.util.*;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

enum SlideState {
    HIGH, MID, LOW, IDLE;
}

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
    ButtonToggler btnBackG1; //used for capstone claw
    ButtonToggler btnBackG2;
    ButtonToggler btnB; //used for carriage trapdoor
    SlideState slidesState;

    public static double DRIVE_SPEED = 0.8;

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

        btnBackG1 = new ButtonToggler();
        btnBackG2 = new ButtonToggler();
        btnB = new ButtonToggler();

        odometry.retract();
        slidesState = SlideState.IDLE;
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
        if(gamepad1.dpad_left || gamepad2.dpad_left) {
            carousel.spinReverse();
        }
        else if(gamepad1.dpad_right || gamepad2.dpad_right){
            carousel.spin();
        }
        else{
            carousel.stop();
        }

        //capstone
        if(gamepad1.dpad_up || gamepad2.dpad_up) {
            capstone.setPosition(CapstoneMechanism.idle);
        }
        else if(gamepad1.dpad_down || gamepad2.dpad_down) {
            capstone.setPosition(CapstoneMechanism.pickup);
        }
        else{
            capstone.setManualPower(0);
        }

        //capstone -- claw (using button toggler)
        btnBackG1.ifRelease(gamepad1.back);
        btnBackG1.update(gamepad1.back);

        btnBackG2.ifRelease(gamepad2.back);
        btnBackG2.update(gamepad2.back);

        if(btnBackG1.getMode() || btnBackG2.getMode()) {
            claw.grab();
        }
        else{
            claw.release();
        }

        telemetry.addData("carriage val", carriage.getArmPosition());
        telemetry.addData("slides val", slides.getCurrentPosition());

        //slides -- using gamepad 2
        //slides --presets
        if (gamepad2.y) {
            slidesState = SlideState.HIGH;
        }

        else if (gamepad2.x) {
            slidesState = SlideState.MID;
        }

        else if (gamepad2.a) {
            slidesState = SlideState.LOW;
        }

        //slides preset code
        if (slidesState == SlideState.IDLE) {
            //slides --manual
            if ((gamepad2.right_trigger > 0.1) && (slides.getCurrentPosition() < Slides.MAX_POSITION)) {
                slides.setPower(gamepad2.right_trigger);
            }
            else if ((gamepad2.left_trigger > 0.1) && (slides.getCurrentPosition() > Slides.MIN_POSITION)) {
                slides.setPower(-gamepad2.left_trigger);
            }
            else{
                slides.setPower(0);
            }
        }

        else if (slidesState == slidesState.HIGH) {
            if ((carriage.getArmPosition() < Carriage.ARM_SAFE_POSITION) && (slides.getCurrentPosition() < Slides.CARRIAGE_STUCK_THRESHOLD)) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION); //move arm up
            }
            else {
                carriage.setRampPos(Carriage.RAMP_CLOSE);
                slides.runToPosition(Slides.MAX_POSITION);
            }
            if (Math.abs(slides.getCurrentPosition()-Slides.MAX_POSITION) < Slides.POSITION_BUFFER_HIGH) {
                slides.setPower(0);
                slidesState = slidesState.IDLE;
            }
        }

        else if (slidesState == slidesState.MID) {
            if ((carriage.getArmPosition() < Carriage.ARM_SAFE_POSITION) && ((slides.getCurrentPosition() < Slides.CARRIAGE_STUCK_THRESHOLD) || (slides.getCurrentPosition() > Slides.CARRIAGE_STUCK_THRESHOLD))) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION); //move arm up
            }
            else {
                carriage.setRampPos(Carriage.RAMP_CLOSE);
                slides.runToPosition(Slides.MID_POSITION);
            }
            if (Math.abs(slides.getCurrentPosition()-Slides.MID_POSITION) < Slides.POSITION_BUFFER_MID) {
                slides.setPower(0);
                slidesState = slidesState.IDLE;
            }
        }

        else if (slidesState == slidesState.LOW) {
            carriage.setRampPos(Carriage.RAMP_OPEN);
            if ((slides.getCurrentPosition() > Slides.MAX_POSITION-500 || slides.getCurrentPosition() > Slides.MID_POSITION-500) && (carriage.getArmPosition() > Carriage.ARM_SAFE_POSITION)) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION); //move arm down before retracts
            }
            else {
                slides.runToPosition(Slides.MIN_POSITION+150);
            }

            if (Math.abs(slides.getCurrentPosition()-Slides.MIN_POSITION-150) < Slides.POSITION_BUFFER_LOW) {
                slides.setPower(0);
                slidesState = slidesState.IDLE;
            }
        }

        //carriage --arm
        if (slides.getCurrentPosition() > Slides.CARRIAGE_STUCK_THRESHOLD) {
            if (Math.abs(gamepad2.right_stick_y) > 0.1 && carriage.getArmPosition() < Carriage.ARM_MAX) {
                carriage.setManualMode();
                carriage.setArmPower(-gamepad2.right_stick_y);
            } else {
                carriage.setArmPower(0);
            }
        }


        //carriage --trapdoor (using button toggler)
        btnB.ifRelease(gamepad2.b);
        btnB.update(gamepad2.b);

        if(btnB.getMode()) {
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_DUMP_POS);
            telemetry.addData("trapdoor open?", true);
        }
        else{
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_IDLE_POS);
            telemetry.addData("trapdoor open?", false);
        }

        telemetry.addData("slide state", slidesState);
        telemetry.update();
    }
}
