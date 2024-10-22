package jcifs.rap.session;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

public class NetSessionDel extends Operation {

    private static final int NET_SESSION_DEL = 8;

    private String client;

    private int session;

    public NetSessionDel(String client, int session) {
        this.client = client;
        this.session = session;
        setNumber(NET_SESSION_DEL);
        setParameterDescriptor("zW");
    }

    public void writeRequestParameters(Buffer buffer) {
        buffer.writeString(client.length() > 20 ? client.substring(0, 20) :
                client);
        buffer.writeShort(session);
    }

}
