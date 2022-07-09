package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.*;

@Config
public abstract class NearWarehouseCycle extends LinearOpMode {
    public static String RED_PATH = JSONParsingUtils.stringFromResource("/NearWarehouseRed.json");
    public static String BLUE_PATH = JSONParsingUtils.stringFromResource("/NearWarehouseBlue.json");
    public static int SLIDE_DUMP_POS = 800;

    abstract boolean isRed();

    @Override
    public void runOpMode() throws InterruptedException {
        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, isRed() ? RED_PATH : BLUE_PATH);
        Slides slides = new Slides(hardwareMap);
        Carriage carriage = new Carriage(hardwareMap);
        TubeIntake intake = new TubeIntake(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);
        CapstoneMechanism capstone = new CapstoneMechanism(hardwareMap);

        session.registerCallback("startDuck", carousel::spinReverse);

        session.registerCallback("stopDuck", carousel::stop);

        session.registerCallback("startIntake", () -> {
            intake.setPower(-0.75);
            carriage.setRampPos(Carriage.RAMP_CLOSE);
        });

        session.registerCallback("reverseIntake", () -> {
            intake.setPower(0.85);
            carriage.setRampPos(Carriage.RAMP_OPEN);
        });

        session.registerCallback("stopIntake", () -> {
            intake.stop();
            carriage.setRampPos(Carriage.RAMP_CLOSE);
        });

        session.registerCallback("extendSlides", () -> {
            carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
            slides.runToPosition(SLIDE_DUMP_POS);
        });

        session.registerCallback("raiseCarriage", () -> {
            carriage.setArmPosition(Carriage.ARM_MAX);
        });

        session.registerCallback("openTrapdoor", () -> {
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_DUMP_POS);
        });

        session.registerCallback("lowerCarriage", () -> {
            carriage.setTrapdoorPos(Carriage.TRAPDOOR_IDLE_POS);
            carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
        });

        session.registerCallback("retractSlides", () -> {
            slides.runToPosition(Slides.MIN_POSITION);
        });

        session.registerCallback("openRamp", () -> {
            carriage.setRampPos(Carriage.RAMP_OPEN);
            carriage.setArmPosition(AFK.ARM_END_POSITION);
        });

        odometryRetraction.extend();
        carriage.setRampPos(Carriage.RAMP_CLOSE);

        waitForStart();
        capstone.setPosition(CapstoneMechanism.autoStart);
        sleep(500);
        carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
        session.run(this);
    }
}
