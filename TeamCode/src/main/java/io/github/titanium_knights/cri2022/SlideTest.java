package io.github.titanium_knights.cri2022;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import io.github.titanium_knights.util.Slides;

@TeleOp
public class SlideTest extends OpMode {
    Slides slide;

    @Override
    public void init() {
        slide = new Slides(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("position",slide.getCurrentPosition());
        if(Math.abs(gamepad1.right_stick_y)>0.1){
            slide.setPower(gamepad1.right_stick_y);
        }
        else{
            slide.setPower(0);
        }
        telemetry.update();
    }
}
