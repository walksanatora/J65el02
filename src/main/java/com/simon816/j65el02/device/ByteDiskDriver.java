package com.simon816.j65el02.device;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteDiskDriver implements DiskDriver{

    private final byte[] bytes;
    private final byte[] drive_name;
    private final byte[] drive_serial;
    private int seek = 0;

    public ByteDiskDriver(byte[] bytes, String name, String serial) {
        this.bytes = bytes;
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
        this.seek = location;
    }

    @Override
    public void read(ByteBuffer buffer) throws IOException {
        byte[] slice = Arrays.copyOfRange(bytes,seek,bytes.length);
        int old_offset = buffer.arrayOffset();
        System.arraycopy(slice,0,buffer.array(),buffer.arrayOffset(),Math.min(slice.length, buffer.capacity()));
        seek += buffer.arrayOffset() - old_offset;
        System.out.println(buffer);
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        seek += Math.min(bytes.length-seek, buffer.capacity());
    }

    @Override
    public void writeDiskName(byte[] diskName) throws IOException {

    }
}
