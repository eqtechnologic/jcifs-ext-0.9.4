package jcifs.rap.server;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetServerDiskEnum extends Operation {

    public static final int NET_SERVER_DISK_ENUM = 15;

    public String[] disks;

    private int entryCount;

    private int availableBytes;

    public NetServerDiskEnum() {
        setNumber(NET_SERVER_DISK_ENUM);
        setMaxParameterLength(8);
        setParameterDescriptor("WrLeh");
        setDataDescriptor("B3");
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(0);
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        disks = new String[entryCount];
        for (int i = 0; i < entryCount; i++) {
            disks[i] = buffer.readFixedString(2);
        }
    }


}
