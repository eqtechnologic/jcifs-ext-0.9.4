package jcifs.rap.group;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetGroupGetUsers extends Operation {

    private static final int NET_GROUP_GET_USERS = 52;

    public GroupUsersInfo[] users;

    private String group;

    private GroupUsersInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetGroupGetUsers(String group, GroupUsersInfo infoTemplate) {
        this.group = group;
        this.infoTemplate = infoTemplate;
        setNumber(NET_GROUP_GET_USERS);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLeh");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(group.length() > 20 ? group.substring(0, 20) :
                group);
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        users = new GroupUsersInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            users[i] = (GroupUsersInfo) infoTemplate.clone();
            users[i].read(buffer);
        }
    }

}
