package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Park (Strafe Left)")
public class SimpleStrafeLeft extends SimpleStrafe {
    @Override
    Direction getDirection() {
        return Direction.LEFT;
    }
}
