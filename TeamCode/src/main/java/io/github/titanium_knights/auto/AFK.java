package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.*;

@Config
@Autonomous(name = "AFK (Prototype)")
public class AFK extends LinearOpMode {
    public static String PATH = JSONParsingUtils.stringFromResource("/AFKPrototype.json");
    public static int ARM_END_POSITION = -50;
    public static int LEVEL = 0;
    public static int SLIDE_DUMP_POSITION_HIGH = 1594;
    public static int SLIDE_DUMP_POSITION_MID = 1230;
    public static int SLIDE_DUMP_POSITION_LOW = 1060;

    @Override
    public void runOpMode() throws InterruptedException {
        int LEVEL = AFK.LEVEL;

        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, PATH);
        Carriage carriage = new Carriage(hardwareMap);
        Slides slides = new Slides(hardwareMap);
        odometryRetraction.extend();

        carriage.setArmPosition(Carriage.ARM_SAFE_POSITION);
        carriage.setRampPos(Carriage.RAMP_CLOSE);
        carriage.setTrapdoorPos(Carriage.TRAPDOOR_IDLE_POS);

        session.registerCallback("extendSlides", () -> {
            if (LEVEL == 0) {
                slides.runToPosition(SLIDE_DUMP_POSITION_LOW);
            } else if (LEVEL == 1) {
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
        session.run(this);
    }
}
