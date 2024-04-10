package com.simon816.j65el02.device;

/**
 * RedBusState is a *per-{@see Cpu}* state for RedBus
 */
public class RedBusState {
    public int activeDeviceId;
    public boolean enabled;
    public int memoryWindow;
    @SuppressWarnings("unused")
    public boolean enableWindow;
    public int offset;
    public RedBusState(int activeDeviceId, boolean enabled, int memoryWindow, boolean enableWindow, int offset) {
        this.activeDeviceId = activeDeviceId;
        this.enabled = enabled;
        this.memoryWindow = memoryWindow;
        this.enableWindow = enableWindow;
        this.offset = offset;
    }
    public RedBusState() {
        this.activeDeviceId = 0;
        this.enabled = true;
        this.memoryWindow = 0;
        this.enableWindow = false;
        this.offset = -1;
    }
}
