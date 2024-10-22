package jcifs.smb;

public class RapException extends SmbException {

    private final String message;

	public RapException(int code) {
		this(code, null);
	}

    public RapException(int code, String message) {
        super(code, true);
        this.message = message;
    }

    public String getMessage() {
        return (message == null) ? super.getMessage() :
                message + " (" + super.getMessage() + ")";
    }

}
