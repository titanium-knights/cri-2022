package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AFK (Blue)")
public class AFKBlue extends AFK {
    @Override
    boolean isRed() {
        return false;
    }
}
