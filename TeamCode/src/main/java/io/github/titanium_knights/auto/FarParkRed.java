package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Park (Strafe Right)")
public class FarParkRed extends FarPark {
    @Override
    Direction getDirection() {
        return Direction.RIGHT;
    }
}
