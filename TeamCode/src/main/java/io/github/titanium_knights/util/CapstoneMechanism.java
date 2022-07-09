package io.github.titanium_knights.util;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.jetbrains.annotations.NotNull;

@Config public class CapstoneMechanism{
    DcMotor motor;
    public static double power = 0.8;
    public static int idle = 200; //idle position
    public static int pickup = -1392; //picking up capstone from floor
    public static int capping = -195; //"dumping" capstone
    public static int autoStart = -1000;

    public CapstoneMechanism(HardwareMap hardwareMap, boolean resetEncoders){
        motor = hardwareMap.dcMotor.get("capstone");
        if (resetEncoders) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public CapstoneMechanism(HardwareMap hardwareMap) {
        this(hardwareMap, true);
    }

    public void setPosition(int pos){
        motor.setTargetPosition(pos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    public void setManualPower(double pow){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(pow);
    }

    public int getPosition(){
        return motor.getCurrentPosition();
    }

    public static int getIdle(){
        return idle;
    }
    public static int getPickup() { return pickup; }



}
