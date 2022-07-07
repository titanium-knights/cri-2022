package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Near Warehouse (Blue)")
public class NearWarehouseBlue extends NearWarehouseCycle {
    @Override
    boolean isRed() {
        return false;
    }
}
