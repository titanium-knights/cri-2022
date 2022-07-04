package io.github.titanium_knights.util;

public class ButtonToggler {
    private boolean on;
    private boolean mode;
    private double duration;

    public ButtonToggler() {
        on = false;
        mode = false;
        duration = 0;
    }

    public boolean ifPress (boolean current) {
        if (!on && current) {
            mode = !mode;
            return true;
        }
        return false;
    }

    public boolean ifRelease (boolean current) {
        if (on && !current) {
            mode = !mode;
            return true;
        }
        return false;
    }

    public void update (boolean current) {
        on = current;
    }

    public boolean getMode () {
        return mode;
    }

}
