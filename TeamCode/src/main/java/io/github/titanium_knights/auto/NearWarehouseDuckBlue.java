package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Near Warehouse Duck (Blue)")
public class NearWarehouseDuckBlue extends NearWarehouseDuck {
    @Override
    boolean isRed() {
        return false;
    }
}
