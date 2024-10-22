package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetUserEnum extends Operation {

    private static final int NET_USER_ENUM = 53;

    public UserInfo[] users;

    private UserInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetUserEnum(UserInfo infoTemplate) {
        this.infoTemplate = infoTemplate;
        setNumber(NET_USER_ENUM);
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
        users = new UserInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            users[i] = (UserInfo) infoTemplate.clone();
            users[i].read(buffer);
        }
    }

}
