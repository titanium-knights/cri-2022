package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import io.github.titanium_knights.roadrunner.drive.SampleMecanumDrive;
import io.github.titanium_knights.util.CapstoneMechanism;
import io.github.titanium_knights.util.Carousel;
import io.github.titanium_knights.util.Carriage;
import io.github.titanium_knights.util.JSONParsingUtils;
import io.github.titanium_knights.util.OdometryRetraction;
import io.github.titanium_knights.util.Slides;
import io.github.titanium_knights.util.TrajectorySession;
import io.github.titanium_knights.util.TubeIntake;

public abstract class NearWarehouseDuck extends LinearOpMode {
    public static String RED_PATH = JSONParsingUtils.stringFromResource("/NearWarehouseDuckRed.json");
    public static String BLUE_PATH = JSONParsingUtils.stringFromResource("/NearWarehouseDuckBlue.json");

    abstract boolean isRed();

    @Override
    public void runOpMode() throws InterruptedException {
        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        TrajectorySession session = TrajectorySession.buildFromJSON(drive, isRed() ? RED_PATH : BLUE_PATH);
        Carousel carousel = new Carousel(hardwareMap);
        CapstoneMechanism capstone = new CapstoneMechanism(hardwareMap);

        session.registerCallback("startDuck", carousel::spinReverse);

        session.registerCallback("stopDuck", carousel::stop);




        odometryRetraction.extend();

        waitForStart();
        capstone.setPosition(CapstoneMechanism.autoStart);
        sleep(500);
        session.run(this);
    }
}
