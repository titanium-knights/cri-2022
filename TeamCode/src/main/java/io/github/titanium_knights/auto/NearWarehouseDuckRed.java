package io.github.titanium_knights.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Near Warehouse Duck (Red)")
public class NearWarehouseDuckRed extends NearWarehouseDuck {
    @Override
    boolean isRed() {
        return true;
    }
}
