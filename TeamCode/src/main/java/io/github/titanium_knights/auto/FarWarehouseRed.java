package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Far Warehouse (Red)")
public class FarWarehouseRed extends FarWarehouseCycle {
    @Override
    boolean isRed() {
        return true;
    }
}
