/* jcifs smb client library in Java
 * Copyright (C) 2000  "Michael B. Allen" <jcifs at samba dot org>
 *                     "Eric Glass" <jcifs at samba dot org> 
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package jcifs.smb;

import jcifs.rap.Buffer;
import jcifs.rap.Operation;

class RapResponse extends SmbComTransactionResponse {

    private Operation operation;

    int converter;

    RapResponse(Operation operation) {
        this.operation = operation;
    }

	int readParametersWireFormat(byte[] src, int index, int length) {
        Buffer buffer = new Buffer(src, index);
        status = buffer.readShort();
        converter = buffer.readShort();
        int preLength = buffer.length();
        try {
            operation.readResponseParameters(buffer);
            return buffer.length();
        } catch (RuntimeException ex) {
            if (status == WinError.ERROR_SUCCESS) throw ex;
            return preLength;
        }
	}

	int readDataWireFormat(byte[] src, int index, int length) {
        Buffer buffer = new Buffer(src, index, index - converter);
        int preLength = buffer.length();
        try {
            operation.readResponseData(buffer);
            return buffer.length();
        } catch (RuntimeException ex) {
            if (status == WinError.ERROR_SUCCESS) throw ex;
            return preLength;
        }
	}

	int readSetupWireFormat(byte[] src, int index, int length) {
		return 0;
	}

	int writeSetupWireFormat(byte[] dest, int index) {
		return 0;
	}

	int writeParametersWireFormat(byte[] dest, int index) {
		return 0;
	}

	int writeDataWireFormat(byte[] dest, int index) {
		return 0;
	}

	public String toString() {
        return "RapResponse[" + super.toString() + "]";
	}

}
