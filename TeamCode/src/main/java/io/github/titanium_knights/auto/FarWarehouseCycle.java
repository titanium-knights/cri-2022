package io.github.titanium_knights.auto;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import io.github.titanium_knights.roadrunner.trajectorysequence.TrajectorySequence;
import io.github.titanium_knights.util.CapstoneMechanism;
import io.github.titanium_knights.util.Carousel;
import io.github.titanium_knights.util.Carriage;
import io.github.titanium_knights.util.Claw;
import io.github.titanium_knights.util.MecanumDrive;
import io.github.titanium_knights.util.OdometryRetraction;
import io.github.titanium_knights.util.Slides;
import io.github.titanium_knights.util.TubeIntake;

@Config
@Autonomous
public class FarWarehouseCycle extends LinearOpMode {
    //devices
    MecanumDrive drive;
    Slides slides;
    Carriage carriage;
    TubeIntake intake;
    Carousel carousel;
    OdometryRetraction odometry;
    CapstoneMechanism capstone;
    Claw claw;

    //trajectories
    TrajectorySequence cycle;

    //coordinates --NEED TO GET
    public static double WAREHOUSE_Y = 0;
    public static double ALLIANCE_X = 0;
    public static double ALLIANCE_Y = 0;

    //positions
    public static double ALLIANCE_HEADING = 90;

    protected void setupDevices() {
        drive = new MecanumDrive(hardwareMap);
        slides = new Slides(hardwareMap);
        carriage = new Carriage(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        carousel = new Carousel(hardwareMap);
        odometry = new OdometryRetraction(hardwareMap);
        capstone = new CapstoneMechanism(hardwareMap);
        claw = new Claw(hardwareMap);

    }

    public void runOpMode() throws InterruptedException {
        setupDevices();

        //setup vectors
        Pose2d warehouse = new Pose2d(52, WAREHOUSE_Y, Math.toRadians(0));
    }
}
