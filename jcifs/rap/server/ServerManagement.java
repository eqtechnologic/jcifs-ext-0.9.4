package jcifs.rap.server;

import java.io.IOException;

import java.util.ArrayList;

import jcifs.rap.LevelFactory;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.Rap;
import jcifs.smb.RapException;
import jcifs.smb.SmbFile;
import jcifs.smb.WinError;

public class ServerManagement extends Rap implements ServerConstants {

    public ServerManagement() {
        super(null, null);
    }

    public ServerManagement(String target) {
        super(target, null);
    }

    public ServerManagement(NtlmPasswordAuthentication auth) {
        super(null, auth);
    }

    public ServerManagement(String target, NtlmPasswordAuthentication auth) {
        super(target, auth);
    }

    public ServerManagement(SmbFile target) {
        super(target);
    }

    public ServerInfo[] netServerEnum(int level, int serverType, String domain)
            throws IOException {
        ServerInfo infoTemplate = (ServerInfo)
                LevelFactory.createInformationLevel(ServerInfo.class, level);
        NetServerEnum2 operation =
                new NetServerEnum2(infoTemplate, serverType, domain);
        int result = call(operation);
        if (result == WinError.ERROR_SUCCESS) return operation.servers;
        if (result != WinError.ERROR_MORE_DATA) throw new RapException(result);
        int length = operation.servers.length;
        ArrayList servers = new ArrayList(length);
        for (int i = 0; i < length; i++) servers.add(operation.servers[i]);
        String followUpName;
        do {
            followUpName = operation.servers[length - 1].name;
            operation = new NetServerEnum3(infoTemplate, serverType, domain,
                    followUpName);
            result = call(operation);
            if (result == WinError.ERROR_MORE_DATA ||
                    result == WinError.ERROR_SUCCESS) {
                length = operation.servers.length;
                servers.ensureCapacity(servers.size() + length);
                for (int i = followUpName.equals(operation.servers[0].name) ?
                        1 : 0; i < length; i++) {
                    servers.add(operation.servers[i]);
                }
            }
        } while (result == WinError.ERROR_MORE_DATA);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return (ServerInfo[]) servers.toArray(new ServerInfo[servers.size()]);
    }

    public ServerInfo netServerGetInfo(int level) throws IOException {
        ServerInfo info = (ServerInfo)
                LevelFactory.createInformationLevel(ServerInfo.class, level);
        NetServerGetInfo operation = new NetServerGetInfo(info);
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return info;
    }

    public void netServerSetInfo(ServerInfo info) throws IOException {
        int result = call(new NetServerSetInfo(info));
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
    }

    public String[] netServerDiskEnum() throws IOException {
        NetServerDiskEnum operation = new NetServerDiskEnum();
        int result = call(operation);
        if (result != WinError.ERROR_SUCCESS) throw new RapException(result);
        return operation.disks;
    }

    public String netGetDcName(String domain) throws IOException {
        if (domain == null) domain = DEFAULT_TARGET;
        ServerInfo[] servers = netServerEnum(0, SV_TYPE_DOMAIN_CTRL, domain);
        return (servers == null || servers.length == 0) ? null :
                servers[0].name;
    }

    public String netGetAnyDcName(String domain) throws IOException {
        if (domain == null) domain = DEFAULT_TARGET;
        ServerInfo[] servers = netServerEnum(0, SV_TYPE_DOMAIN_CTRL |
                SV_TYPE_DOMAIN_BAKCTRL, domain);
        return (servers == null || servers.length == 0) ? null :
                servers[0].name;
    }

}
