package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Far Warehouse (Blue)")
public class FarWarehouseBlue extends FarWarehouseCycle {
    @Override
    boolean isRed() {
        return false;
    }
}
