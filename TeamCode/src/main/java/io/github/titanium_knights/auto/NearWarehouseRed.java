package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Near Warehouse (Red)")
public class NearWarehouseRed extends NearWarehouseCycle {
    @Override
    boolean isRed() {
        return true;
    }
}
