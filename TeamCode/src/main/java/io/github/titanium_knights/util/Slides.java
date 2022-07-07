package io.github.titanium_knights.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@Config public class Slides {
    DcMotor motor;

    //slide positions
    public static int MAX_POSITION = 1614;
    public static int MID_POSITION = 463; //MUST TEST THIS VALUE
    public static int MIN_POSITION = 0;

    public static double IDLE_POWER = 0;
    public static double IDLE_POWER_RTP = 0;
    public static boolean USE_ENCODER = true;

    public static int POSITION_BUFFER_HIGH = 50;
    public static int POSITION_BUFFER_MID = 15;
    public static int POSITION_BUFFER_LOW = 25;

    public static int CARRIAGE_STUCK_THRESHOLD = 80; //TEST VALUE
    public static int RAMP_MOVEMENT_THRESHOLD = 400;
    public static int CARRIAGE_MOVE_DOWN_POS = 200;

    public Slides(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("slides");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void setPower(double pow){
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (pow == 0) {
            pow = IDLE_POWER;
        }

        motor.setPower(pow);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void runToPosition(int pos, double multiplier){
        if (USE_ENCODER) {
            motor.setTargetPosition(pos);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            motor.setPower(1 * multiplier);
        } else {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            int currentPos = motor.getCurrentPosition();
            //double multiplier = Math.min(1, Math.max(0, Math.abs(pos - currentPos) / 150.0));
            if (pos - currentPos > 30) {
                setPower(1 * multiplier);
            } else if (pos - currentPos < -30) {
                setPower(-1 * multiplier);
            } else if (pos == 0) {
                setPower(0);
            } else {
                setPower(IDLE_POWER_RTP);
            }
        }
    }

    public void runToPosition(int pos) {
        runToPosition(pos, 1.0);
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }
}
