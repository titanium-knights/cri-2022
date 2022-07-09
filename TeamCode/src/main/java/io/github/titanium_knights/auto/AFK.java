package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.*;

import java.util.concurrent.atomic.AtomicInteger;

@Config
abstract class AFK extends LinearOpMode {
    public static String RED_PATH = JSONParsingUtils.stringFromResource("/AFKRed.json");
    public static String BLUE_PATH = JSONParsingUtils.stringFromResource("/AFKBlue.json");
    public static int ARM_END_POSITION = -60;
    public static int SLIDE_DUMP_POSITION_HIGH = 1594;
    public static int SLIDE_DUMP_POSITION_MID = 1240;
    public static int SLIDE_DUMP_POSITION_LOW = 1060;

    abstract boolean isRed();

    @Override
    public void runOpMode() throws InterruptedException {
        AtomicInteger LEVEL = new AtomicInteger(2);

        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, isRed() ? RED_PATH : BLUE_PATH);
        Carriage carriage = new Carriage(hardwareMap);
        Slides slides = new Slides(hardwareMap);
        CapstoneVision vision = new CapstoneVision(hardwareMap, telemetry);
        CapstoneMechanism capstone = new CapstoneMechanism(hardwareMap);
        odometryRetraction.extend();

        carriage.setRampPos(Carriage.RAMP_CLOSE);
        carriage.setTrapdoorPos(Carriage.TRAPDOOR_IDLE_POS);

        session.registerCallback("extendSlides", () -> {
            if (LEVEL.get() == 0) {
                slides.runToPosition(SLIDE_DUMP_POSITION_LOW);
            } else if (LEVEL.get() == 1) {
                slides.runToPosition(SLIDE_DUMP_POSITION_MID);
            } else {
                slides.runToPosition(SLIDE_DUMP_POSITION_HIGH);
            }
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
            carriage.setArmPosition(ARM_END_POSITION);
        });

        waitForStart();
        LEVEL.set(vision.getPosition());
        capstone.setPosition(CapstoneMechanism.autoStart);
        sleep(500);
        carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
        session.run(this);
    }
}
