package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import io.github.titanium_knights.util.CapstoneMechanism;

@TeleOp(name = "Capstone Mechanism Mover")
public class CapstoneMechanismMover extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        CapstoneMechanism capstoneMechanism = new CapstoneMechanism(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            capstoneMechanism.setManualPower(-gamepad2.right_stick_y);
        }
    }
}
