package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AFK (Red)")
public class AFKRed extends AFK {
    @Override
    boolean isRed() {
        return true;
    }
}
