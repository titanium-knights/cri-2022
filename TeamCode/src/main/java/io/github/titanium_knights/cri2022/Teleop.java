package io.github.titanium_knights.cri2022;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import io.github.titanium_knights.util.*;

import com.acmerobotics.dashboard.config.Config;

enum SlideState {
    HIGH, MID, LOW_RESETTING_CARRIAGE, LOW_MOVING, MANUAL,
    HIGH_SAFING, MID_SAFING, MANUAL_SAFING,
    LOW_UNSAFE;

    boolean allowsManualCarriageMovement() {
        return (this == HIGH || this == MID || this == MANUAL || this == LOW_UNSAFE);
    }

    boolean isSafing() {
        return (this == HIGH_SAFING || this == MID_SAFING || this == MANUAL_SAFING);
    }

    boolean shouldOpenRamp() {
        return (this == LOW_RESETTING_CARRIAGE || this == LOW_MOVING || this == LOW_UNSAFE);
    }
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
    ButtonToggler btnB; //used for carriage trapdoor (G1)
    ButtonToggler btnX; //used for slowmode toggle (G1)
    SlideState slidesState;

    public static double DRIVE_SPEED = 0.8;
    public static double DRIVE_SPEED_SLOW = 0.5;

    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
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
        btnX = new ButtonToggler();

        odometry.retract();
        slidesState = SlideState.LOW_UNSAFE;
    }

    @Override
    public void loop() {
        btnX.ifRelease(gamepad1.x);
        btnX.update(gamepad1.x);

        //driving + slow mode
        if (btnX.getMode()) {
            drive.teleOpRobotCentric(gamepad1, DRIVE_SPEED_SLOW);
        }

        else {
            drive.teleOpRobotCentric(gamepad1, DRIVE_SPEED);
        }

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
            slidesState = SlideState.HIGH_SAFING;
        }

        else if (gamepad2.x) {
            slidesState = SlideState.MID_SAFING;
        }

        else if (gamepad2.a) {
            slidesState = SlideState.LOW_RESETTING_CARRIAGE;
        }

        if (gamepad2.left_trigger > 0.1 || gamepad2.right_trigger > 0.1) {
            if (slidesState != SlideState.MANUAL) slidesState = SlideState.MANUAL_SAFING;
        }

        carriage.setRampPos(slidesState.shouldOpenRamp() ? Carriage.RAMP_OPEN : Carriage.RAMP_CLOSE);

        if (slidesState == SlideState.HIGH) {
            slides.runToPosition(Slides.MAX_POSITION);
        } else if (slidesState == SlideState.MID) {
            slides.runToPosition(Slides.MID_POSITION);
        } else if (slidesState == SlideState.LOW_RESETTING_CARRIAGE) {
            slides.setPower(0);
            if (Math.abs(carriage.getArmPosition() - Carriage.ARM_SAFE_POSITION) < Carriage.ARM_POSITION_BUFFER) {
                slidesState = SlideState.LOW_MOVING;
            } else {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
            }
        } else if (slidesState == SlideState.LOW_MOVING) {
            slides.runToPosition(Slides.MIN_POSITION);
            btnB.setMode(false);
            if (Math.abs(slides.getCurrentPosition() - Slides.CARRIAGE_MOVE_DOWN_POS) < Slides.POSITION_BUFFER_LOW) {
                slidesState = SlideState.LOW_UNSAFE;
            }
        } else if (slidesState == SlideState.MANUAL) {
            if ((gamepad2.right_trigger > 0.1) && (slides.getCurrentPosition() < Slides.MAX_POSITION)) {
                slides.setPower(gamepad2.right_trigger);
            }
            else if ((gamepad2.left_trigger > 0.1) && (slides.getCurrentPosition() > Slides.MIN_POSITION)) {
                slides.setPower(-gamepad2.left_trigger);
            }
            else{
                slides.setPower(0);
            }
        } else if (slidesState.isSafing()) {
            slides.setPower(0);
            if (carriage.getArmPosition() < (Carriage.ARM_SAFE_POSITION - Carriage.ARM_POSITION_BUFFER)) {
                carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
            } else {
                if (slidesState == SlideState.HIGH_SAFING) {
                    slidesState = SlideState.HIGH;
                } else if (slidesState == SlideState.MID_SAFING) {
                    slidesState = SlideState.MID;
                } else if (slidesState == SlideState.MANUAL_SAFING) {
                    slidesState = SlideState.MANUAL;
                } else {
                    throw new IllegalStateException("Unknown safing state!");
                }
            }
        } else if (slidesState == SlideState.LOW_UNSAFE) {
            slides.runToPosition(Slides.MIN_POSITION);
            carriage.setArmPosition(Carriage.ARM_INTAKE_POSITION);
        } else {
            throw new IllegalStateException("Unknown state!");
        }

        //carriage --arm
        if (slidesState.allowsManualCarriageMovement() && slides.getCurrentPosition() > Slides.CARRIAGE_STUCK_THRESHOLD) {
            long max = (slides.getCurrentPosition() > Slides.CARRIAGE_STUCK_THRESHOLD) ? Carriage.ARM_MAX : Carriage.ARM_SAFE_POSITION;
            if (Math.abs(gamepad2.right_stick_y) > 0.1 && carriage.getArmPosition() < max) {
                carriage.setManualMode();
                carriage.setArmPower(-gamepad2.right_stick_y);
            } else if (carriage.isManualMode()) {
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
