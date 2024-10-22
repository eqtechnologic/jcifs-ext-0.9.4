package jcifs.rap.share;

import java.io.IOException;

import jcifs.rap.LevelFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.Rap;
import jcifs.smb.RapException;
import jcifs.smb.SmbFile;
import jcifs.smb.WinError;

public class ShareManagement extends Rap implements ShareConstants {

    public ShareManagement() {
        super(null, null);
    }

    public ShareManagement(String target) {
        super(target, null);
    }

    public ShareManagement(NtlmPasswordAuthentication auth) {
        super(null, auth);
    }

    public ShareManagement(String target, NtlmPasswordAuthentication auth) {
        super(target, auth);
    }

    public ShareManagement(SmbFile target) {
        super(target);
    }

    public ConnectionInfo[] netConnectionEnum(String qualifier, int level)
            throws IOException {
        ConnectionInfo infoTemplate = (ConnectionInfo)
                LevelFactory.createInformationLevel(ConnectionInfo.class,
                        level);
        NetConnectionEnum operation = new NetConnectionEnum(qualifier,
                infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.connections;
    }

    public void netShareAdd(ShareInfo info) throws IOException {
        int result = call(new NetShareAdd(info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public int netShareCheck(String share) throws IOException {
        NetShareCheck operation = new NetShareCheck(share);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.type;
    }

    public void netShareDel(String share) throws IOException {
        int result = call(new NetShareDel(share));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public ShareInfo[] netShareEnum(int level) throws IOException {
        ShareInfo infoTemplate = (ShareInfo)
                LevelFactory.createInformationLevel(ShareInfo.class, level);
        NetShareEnum operation = new NetShareEnum(infoTemplate);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.shares;
    }

    public ShareInfo netShareGetInfo(String share, int level)
            throws IOException {
        ShareInfo info = (ShareInfo)
                LevelFactory.createInformationLevel(ShareInfo.class, level);
        NetShareGetInfo operation = new NetShareGetInfo(share, info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

    public void netShareSetInfo(String share, ShareInfo info)
            throws IOException {
        int result = call(new NetShareSetInfo(share, info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

}
