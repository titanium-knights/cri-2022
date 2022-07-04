package io.github.titanium_knights.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class GenericMotorTest extends OpMode {
    DcMotor motor;
    public static String MOTOR_NAME = "carriage";
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        telemetry.addData("motor", MOTOR_NAME);
        motor.setPower(gamepad1.left_stick_y);
        telemetry.addData("Motor Encoder Val", motor.getCurrentPosition());
        telemetry.update();
    }
}
