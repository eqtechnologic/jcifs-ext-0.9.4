package jcifs.smb;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

class RapRequest extends SmbComTransaction {

    private Operation operation;

	RapRequest(Operation operation) {
        this.operation = operation;
		command = SMB_COM_TRANSACTION;
		subCommand = TRANS_TRANSACT_NAMED_PIPE;
		name = new String( "\\PIPE\\LANMAN" );
		timeout = 5000;
		maxSetupCount = (byte) 0x00;
		setupCount = 0;
        maxParameterCount = operation.getMaxParameterLength();
        maxDataCount = operation.getMaxDataLength();
	}

	int writeParametersWireFormat(byte[] dest, int index) {
        Buffer buffer = new Buffer(dest, index);
        buffer.writeShort(operation.getNumber());
        buffer.writeString(operation.getParameterDescriptor());
        buffer.writeString(operation.getDataDescriptor());
        operation.writeRequestParameters(buffer);
        String auxiliaryDataDescriptor = operation.getAuxiliaryDataDescriptor();
        if (auxiliaryDataDescriptor != null) {
            buffer.writeString(auxiliaryDataDescriptor);
        }
        return buffer.length();
	}

	int writeDataWireFormat(byte[] dest, int index) {
        String dataDescriptor = operation.getDataDescriptor();
        if (dataDescriptor == null) return 0;
        int offset = index + calculateSize(dataDescriptor);
        if ((dataDescriptor = operation.getAuxiliaryDataDescriptor()) != null) {
            offset += calculateSize(dataDescriptor) *
                    operation.getRequestAuxiliaryCount();
        }
        Buffer buffer = new Buffer(dest, index, offset);
        operation.writeRequestData(buffer);
        return buffer.length();
	}

	int writeSetupWireFormat(byte[] dest, int index) {
		return 0;
	}

	int readSetupWireFormat(byte[] src, int index, int length) {
		return 0;
	}

	int readParametersWireFormat(byte[] src, int index, int length) {
		return 0;
	}

	int readDataWireFormat(byte[] src, int index, int length) {
		return 0;
	}

	public String toString() {
		return "RapRequest[" + super.toString() + "]";
	}

    private int calculateSize(String dataDescriptor) {
        int size = 0;
        int index = 0;
        int length = dataDescriptor.length();
        while (index < length) {
            int count = 1;
            char c;
            switch (c = dataDescriptor.charAt(index++)) {
            case 'W':
                if (index < length && Character.isDigit(
                        dataDescriptor.charAt(index))) {
                    String digits = new String();
                    for (; index < length && Character.isDigit(c =
                            dataDescriptor.charAt(index)); index++) {
                        digits += c;
                    }
                    count = Integer.parseInt(digits);
                }
                size += 2 * count;
                break;
            case 'D':
                if (index < length && Character.isDigit(
                        dataDescriptor.charAt(index))) {
                    String digits = new String();
                    for (; index < length && Character.isDigit(c =
                            dataDescriptor.charAt(index)); index++) {
                        digits += c;
                    }
                    count = Integer.parseInt(digits);
                }
                size += 4 * count;
                break;
            case 'B':
                if (index < length && Character.isDigit(
                        dataDescriptor.charAt(index))) {
                    String digits = new String();
                    for (; index < length && Character.isDigit(c =
                            dataDescriptor.charAt(index)); index++) {
                        digits += c;
                    }
                    count = Integer.parseInt(digits);
                }
                size += count;
                break;
            case 'b':
                if (index < length && Character.isDigit(
                        dataDescriptor.charAt(index))) {
                    while (index < length &&
                            Character.isDigit(dataDescriptor.charAt(index))) {
                        index++;
                    }
                }
                size += 4;
            case 'O':
                size += 4;
                break;
            case 'z':
                size += 4;
                break;
            case 'N':
                size += 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid character: " +
                        dataDescriptor.charAt(index - 1));
            }
        }
        return size;
    }

}
