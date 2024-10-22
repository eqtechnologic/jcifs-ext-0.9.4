package jcifs.rap.user;

import jcifs.rap.Buffer;

import jcifs.util.Hexdump;

public class UserInfo11 extends UserInfo10 {

    public int privilegeLevel;

    public long authFlags;

    public long passwordAge; 

    public String homeDirectory;

    public String applicationParameters;

    public long lastLogon;

    public long lastLogoff;

    public int logonFailureCount;

    public int logonSuccessCount;

    public String logonServer;

    public int countryCode;

    public String workstations;

    public long maxStorage;

    public int unitsPerWeek;

    public final byte[] logonHours = new byte[21];

    public int codePage;

    public String getDescriptor() {
        return super.getDescriptor() + "WDDzzDDWWzWzDWb21W";
    }

    public void read(Buffer buffer) {
        super.read(buffer);
        privilegeLevel = buffer.readShort();
        authFlags = buffer.readLong();
        passwordAge = buffer.readLong(); 
        homeDirectory = buffer.readFreeString();
        applicationParameters = buffer.readFreeString();
        lastLogon = buffer.readLong();
        lastLogoff = buffer.readLong();
        logonFailureCount = buffer.readShort();
        logonSuccessCount = buffer.readShort();
        logonServer = buffer.readFreeString();
        countryCode = buffer.readShort();
        workstations = buffer.readFreeString();
        maxStorage = buffer.readLong();
        unitsPerWeek = buffer.readShort();
        // probably need to do some endian swapping to make this useful.
        buffer.readDataBuffer(logonHours);
        codePage = buffer.readShort();
    }

    public void write(Buffer buffer) {
        super.write(buffer);
        buffer.writeShort(privilegeLevel);
        buffer.writeLong(authFlags);
        buffer.writeLong(passwordAge); 
        buffer.writeFreeString(homeDirectory);
        buffer.writeFreeString(applicationParameters);
        buffer.writeLong(lastLogon);
        buffer.writeLong(lastLogoff);
        buffer.writeShort(logonFailureCount);
        buffer.writeShort(logonSuccessCount);
        buffer.writeFreeString(logonServer);
        buffer.writeShort(countryCode);
        buffer.writeFreeString(workstations);
        buffer.writeLong(maxStorage);
        buffer.writeShort(unitsPerWeek);
        buffer.writeDataBuffer(logonHours);
        buffer.writeShort(codePage);
    }

    public String toString() {
        return super.toString() +
                ";privilegeLevel=" + privilegeLevel +
                ";authFlags=" + authFlags +
                ";passwordAge=" + passwordAge +
                ";homeDirectory=" + homeDirectory +
                ";applicationParameters=" + applicationParameters +
                ";lastLogon=" + lastLogon +
                ";lastLogoff=" + lastLogoff +
                ";logonFailureCount=" + logonFailureCount +
                ";logonSuccessCount=" + logonSuccessCount +
                ";logonServer=" + logonServer +
                ";countryCode=" + countryCode +
                ";workstations=" + workstations +
                ";maxStorage=" + maxStorage +
                ";unitsPerWeek=" + unitsPerWeek +
                ";logonHours=" + Hexdump.toHexString(logonHours, 0, 21) +
                ";codePage=" + codePage;
    }

}
