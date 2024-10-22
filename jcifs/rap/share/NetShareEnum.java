package jcifs.rap.share;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetShareEnum extends Operation {

    private static final int NET_SHARE_ENUM = 0;

    public ShareInfo[] shares;

    private ShareInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetShareEnum(ShareInfo infoTemplate) {
        this.infoTemplate = infoTemplate;
        setNumber(NET_SHARE_ENUM);
        setMaxParameterLength(8);
        setParameterDescriptor("WrLeh");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        shares = new ShareInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            shares[i] = (ShareInfo) infoTemplate.clone();
            shares[i].read(buffer);
        }
    }

}
