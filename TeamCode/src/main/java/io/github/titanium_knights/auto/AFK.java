package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.JSONParsingUtils;
import io.github.titanium_knights.util.OdometryRetraction;
import io.github.titanium_knights.util.TrajectorySession;

@Config
@Autonomous(name = "AFK (Prototype)")
public class AFK extends LinearOpMode {
    public static String PATH = JSONParsingUtils.stringFromResource("/AFKPrototype.json");

    @Override
    public void runOpMode() throws InterruptedException {
        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, PATH);
        odometryRetraction.extend();
        waitForStart();
        session.run(this);
    }
}
