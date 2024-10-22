package jcifs.rap.server;

import jcifs.rap.Buffer;

public class NetServerEnum3 extends NetServerEnum2 {

    public static final int NET_SERVER_ENUM3 = 215;

    private String followUpName;

    public NetServerEnum3(ServerInfo infoTemplate, int serverType,
            String domain, String followUpName) {
        super(infoTemplate, serverType, domain);
        this.followUpName = followUpName;
        setNumber(NET_SERVER_ENUM3);
        setParameterDescriptor("WrLehDzz");
    }

    public void writeRequestParameters(Buffer buffer) {
        super.writeRequestParameters(buffer);
        buffer.writeString(followUpName);
    }

}
