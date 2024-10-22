package jcifs.rap;

import jcifs.Config;

public abstract class Operation {

    public static final int DEFAULT_MAX_PARAMETER_LENGTH = 1024;

    public static final int DEFAULT_MAX_DATA_LENGTH =
            Config.getInt("jcifs.smb.client.transaction_buf_size", 0xffff) -
                    512;

    private String parameterDescriptor;

    private String dataDescriptor;

    private String auxiliaryDataDescriptor;

    private int number;

    private int maxParameterLength = DEFAULT_MAX_PARAMETER_LENGTH;

    private int maxDataLength = DEFAULT_MAX_DATA_LENGTH;

    private int requestAuxiliaryCount = 0;

    private int responseEntryCount = 0;

    public int getNumber() {
        return number;
    }

    protected void setNumber(int number) {
        this.number = number;
    }

    public int getMaxParameterLength() {
        return maxParameterLength;
    }

    protected void setMaxParameterLength(int maxParameterLength) {
        this.maxParameterLength = maxParameterLength;
    }

    public int getMaxDataLength() {
        return maxDataLength;
    }

    protected void setMaxDataLength(int maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    public String getParameterDescriptor() {
        return parameterDescriptor;
    }

    protected void setParameterDescriptor(String parameterDescriptor) {
        this.parameterDescriptor = parameterDescriptor;
    }

    public String getDataDescriptor() {
        return dataDescriptor;
    }

    protected void setDataDescriptor(String dataDescriptor) {
        this.dataDescriptor = dataDescriptor;
    }

    public String getAuxiliaryDataDescriptor() {
        return auxiliaryDataDescriptor;
    }

    protected void setAuxiliaryDataDescriptor(String auxiliaryDataDescriptor) {
        this.auxiliaryDataDescriptor = auxiliaryDataDescriptor;
    }

    public int getRequestAuxiliaryCount() {
        return requestAuxiliaryCount;
    }

    protected void setRequestAuxiliaryCount(int count) {
        this.requestAuxiliaryCount = count;
    }

    public void writeRequestParameters(Buffer buffer) { }

    public void writeRequestData(Buffer buffer) { }

    public void readResponseParameters(Buffer buffer) { }

    public void readResponseData(Buffer buffer) { }

}
