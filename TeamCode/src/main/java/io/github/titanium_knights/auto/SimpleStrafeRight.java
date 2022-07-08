package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Park (Strafe Right)")
public class SimpleStrafeRight extends SimpleStrafe {
    @Override
    Direction getDirection() {
        return Direction.RIGHT;
    }
}
