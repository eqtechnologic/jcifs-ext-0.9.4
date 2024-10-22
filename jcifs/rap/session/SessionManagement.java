package jcifs.rap.session;

import java.io.IOException;

import jcifs.rap.LevelFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.Rap;
import jcifs.smb.RapException;
import jcifs.smb.SmbFile;
import jcifs.smb.WinError;

public class SessionManagement extends Rap {

    public SessionManagement() {
        super(null, null);
    }

    public SessionManagement(String target) {
        super(target, null);
    }

    public SessionManagement(NtlmPasswordAuthentication auth) {
        super(null, auth);
    }

    public SessionManagement(String target, NtlmPasswordAuthentication auth) {
        super(target, auth);
    }

    public SessionManagement(SmbFile target) {
        super(target);
    }

    public void netSessionDel(String client, int session) throws IOException {
        int result = call(new NetSessionDel(client, session));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public SessionInfo[] netSessionEnum(int level) throws IOException {
        SessionInfo infoTemplate = (SessionInfo)
                LevelFactory.createInformationLevel(SessionInfo.class, level);
        NetSessionEnum operation = new NetSessionEnum(infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.sessions;
    }

    public SessionInfo netSessionGetInfo(String client, int level)
            throws IOException {
        SessionInfo info = (SessionInfo)
                LevelFactory.createInformationLevel(SessionInfo.class, level);
        NetSessionGetInfo operation = new NetSessionGetInfo(client, info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

}
