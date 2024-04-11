package com.simon816.j65el02.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteDiskDriver implements DiskDriver{

    private final ByteBuffer bytes;
    private final byte[] drive_name;
    private final byte[] drive_serial;

    public ByteDiskDriver(byte[] bytes, String name, String serial) {
        this.bytes = ByteBuffer.wrap(bytes);
        this.drive_name = name.getBytes(StandardCharsets.UTF_8);
        this.drive_serial = serial.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] getDriveName() {
        return drive_name;
    }

    @Override
    public byte[] getDriveSerial() {
        return drive_serial;
    }

    @Override
    public void seek(int location) throws IOException {
        this.bytes.position(Math.min(bytes.limit(),location));
    }

    @Override
    public void read(ByteBuffer buffer) throws IOException {
        int size = Math.min(buffer.capacity(),bytes.remaining());
        for (int i = 0; i < size; i++) {
            buffer.put(bytes.get());
        }
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        this.bytes.position(this.bytes.position() + buffer.capacity());
    }

    @Override
    public void writeDiskName(byte[] diskName) throws IOException {

    }
}
