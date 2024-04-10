/*
 * Copyright (c) 2017 Simon816
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.simon816.j65el02.device;

/**
 * Implements the 65el02 Redbus.
 */
public class RedBus extends Device {

    /**
     * A Redbus peripheral.
     */
    public interface Peripheral {

        void write(int address, int data);

        int read(int address);

        void update();

    }

    private Peripheral[] peripherals = new Peripheral[0x100];

    public RedBus() {
        super(-1, -1); // there is no fixed address for the redbus
    }

    @Override
    public void write(int address, int data, RedBusState state) {
        if (!state.enabled) {
            return;
        }
        Peripheral peripheral = this.peripherals[state.activeDeviceId];
        if (peripheral != null) {
            peripheral.write(address, data & 0xff);
        }
    }

    @Override
    public int read(int address, boolean cpuAccess, RedBusState state) {
        if (!state.enabled) {
            return 0;
        }
        Peripheral peripheral = this.peripherals[state.activeDeviceId];
        if (peripheral != null) {
            return peripheral.read(address);
        }
        return 0;
    }
    public int getSize(RedBusState state) {
        return (state.offset+0xff) - state.offset + 1;
    }
    @Override
    public int startAddress(RedBusState state) {
        return state.offset;
    }
    @Override
    public boolean inRange(int address, RedBusState state) {
        return address >= state.offset && address <= (state.offset+0xff);
    }

    public void setPeripheral(int id, Peripheral peripheral) {
        this.peripherals[id] = peripheral;
    }

    public void updatePeripheral(RedBusState state) {
        Peripheral peripheral = this.peripherals[state.activeDeviceId];
        if (peripheral != null) {
            peripheral.update();
        }
    }

}
