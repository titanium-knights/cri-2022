package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Park (Strafe Left)")
public class FarParkBlue extends FarPark {
    @Override
    Direction getDirection() {
        return Direction.LEFT;
    }
}
