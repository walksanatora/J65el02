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

package com.simon816.j65el02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.simon816.j65el02.device.Device;
import com.simon816.j65el02.device.RedBus;
import com.simon816.j65el02.device.RedBusState;

public class Bus {

    protected RedBus redBus;
    protected List<Device> devices;

    private int[] boundaries;

    public Bus(RedBus redBus) {
        this.redBus = redBus;
        this.devices = new ArrayList<>();
        this.boundaries = new int[0];
    }

    public void addDevice(Device device, RedBusState state) {
        this.devices.add(device);
        int[] newBoundaries = new int[this.boundaries.length + 1];
        System.arraycopy(this.boundaries, 0, newBoundaries, 1, this.boundaries.length);
        newBoundaries[0] = device.startAddress(state);
        Arrays.sort(newBoundaries);
        this.devices.sort(Comparator.comparingInt(a -> a.startAddress(state)));
        this.boundaries = newBoundaries;
    }

    public void write(int address, int data, RedBusState state) {
        Device device = findDevice(address,state);
        device.write(address - device.startAddress(state), data, state);
    }

    public int read(int address, boolean cpuAccess, RedBusState state) {
        Device device = findDevice(address,state);
        return device.read(address - device.startAddress(state), cpuAccess, state) & 0xff;
    }

    public RedBus getRedBus() {
        return this.redBus;
    }

    public void update(RedBusState state) {
        this.redBus.updatePeripheral(state);
    }

    protected Device findDevice(int address, RedBusState state) {
        // RedBus takes priority
        if (this.redBus.inRange(address, state)) {
            return this.redBus;
        }
        int idx = Arrays.binarySearch(this.boundaries, address);
        if (idx < 0) {
            idx = -idx - 2;
        }
        return this.devices.get(idx);
    }


}
