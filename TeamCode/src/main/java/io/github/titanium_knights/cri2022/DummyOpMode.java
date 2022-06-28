package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Dummy Op Mode")
public class DummyOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        sleep(1000);
    }
}
