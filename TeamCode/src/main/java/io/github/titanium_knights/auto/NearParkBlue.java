package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.*;

@Config
@Autonomous(name = "Near Park (Blue)")
public class NearParkBlue extends LinearOpMode {
    public static String PATH = JSONParsingUtils.stringFromResource("/NearParkBlue.json");

    @Override
    public void runOpMode() throws InterruptedException {
        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, PATH);
        CapstoneMechanism capstone = new CapstoneMechanism(hardwareMap);
        odometryRetraction.extend();

        waitForStart();
        capstone.setPosition(CapstoneMechanism.autoStart);

        sleep(1000);
        session.run(this);
    }
}
