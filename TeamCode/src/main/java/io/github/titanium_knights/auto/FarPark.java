package io.github.titanium_knights.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import io.github.titanium_knights.util.CapstoneMechanism;
import io.github.titanium_knights.util.MecanumDrive;

@Config
public abstract class FarPark extends LinearOpMode {
    public static long DELAY_TIME = 27000;
    public static double MOVE_POWER = 1;
    public static long MOVE_TIME = 2000;

    public enum Direction {
        LEFT, RIGHT
    }

    abstract Direction getDirection();

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        CapstoneMechanism capstoneMechanism = new CapstoneMechanism(hardwareMap);

        waitForStart();

        capstoneMechanism.setPosition(CapstoneMechanism.autoStart);

        sleep(DELAY_TIME);

        if (getDirection() == Direction.LEFT) {
            drive.move(-0.1,1,0 );
            drive.driveForwardsWithPower(MOVE_POWER);
        } else {
            drive.move(0.1,1,0 );
            drive.driveForwardsWithPower(MOVE_POWER);
        }
        sleep(MOVE_TIME);
        drive.stop();
    }
}
