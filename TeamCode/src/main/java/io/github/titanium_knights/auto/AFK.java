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

    @Override
    public void runOpMode() throws InterruptedException {
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
            slides.runToPosition(Slides.MAX_POSITION);
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
            carriage.setArmPosition(0);
        });

        waitForStart();
        session.run(this);
    }
}
