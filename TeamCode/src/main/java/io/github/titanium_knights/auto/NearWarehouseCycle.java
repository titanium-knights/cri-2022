package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.JSONParsingUtils;
import io.github.titanium_knights.util.TrajectorySession;

@Autonomous(name = "Near Warehouse Cycle (Prototype)")
public class NearWarehouseCycle extends LinearOpMode {
    public static String PATH = JSONParsingUtils.stringFromResource("/nearWarehousePrototype.json");

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, PATH);
        waitForStart();
        session.run(this);
    }
}
