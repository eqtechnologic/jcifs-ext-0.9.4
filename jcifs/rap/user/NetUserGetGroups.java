package jcifs.rap.user;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

import jcifs.rap.group.GroupUsersInfo;

public class NetUserGetGroups extends Operation {

    private static final int NET_USER_GET_GROUPS = 59;

    public GroupUsersInfo[] groups;

    private String user;

    private GroupUsersInfo infoTemplate;

    private int entryCount;

    private int availableBytes;

    public NetUserGetGroups(String user, GroupUsersInfo infoTemplate) {
        this.user = user;
        this.infoTemplate = infoTemplate;
        setNumber(NET_USER_GET_GROUPS);
        setMaxParameterLength(8);
        setParameterDescriptor("zWrLeh");
        setDataDescriptor(infoTemplate.getDescriptor());
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(user.length() > 20 ? user.substring(0, 20) :
                user);
        buffer.writeShort(infoTemplate.getLevel());
        buffer.writeShort(getMaxDataLength());
    }

    public void readResponseParameters(Buffer buffer) {
        entryCount = buffer.readShort();
        availableBytes = buffer.readShort();
    }

    public void readResponseData(Buffer buffer) {
        groups = new GroupUsersInfo[entryCount];
        for (int i = 0; i < entryCount; i++) {
            groups[i] = (GroupUsersInfo) infoTemplate.clone();
            groups[i].read(buffer);
        }
    }

}
