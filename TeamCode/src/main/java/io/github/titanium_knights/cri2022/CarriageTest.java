package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.dashboard.config.Config;


import io.github.titanium_knights.util.Carriage;

@Config @TeleOp
public class CarriageTest extends OpMode {
    DcMotor arm;
    Servo trapdoor;
    Carriage carriage;

    public static double TRAPDOOR_DUMP = 0.3;
    public static double TRAPDOOR_START = 0;

    boolean dumped = false;

    @Override
    public void init() {
        carriage = new Carriage(hardwareMap);
        arm = hardwareMap.get(DcMotor.class, "carriage");
    }

    @Override
    public void loop() {
        //arm movement
        if (Math.abs(gamepad1.right_stick_x) > 0.1) {
            arm.setPower(gamepad1.right_stick_x);
            telemetry.addData("arm power", gamepad1.right_stick_x);
        }
        else{
            arm.setPower(0);
        }

        //trapdoor toggle
        if (gamepad1.b && dumped == false) {
            carriage.setTrapdoorPos(TRAPDOOR_DUMP); //"dump" position
            dumped = true;
        }
        else if (gamepad1.b && dumped) {
            carriage.setTrapdoorPos(TRAPDOOR_START);
            dumped = false;
        }

        telemetry.addData("dumped", dumped);
        telemetry.update();
    }


}
