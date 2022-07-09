package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Near Warehouse (Red)")
public class NearWarehouseDuckRed extends NearWarehouseDuck {
    @Override
    boolean isRed() {
        return true;
    }
}
