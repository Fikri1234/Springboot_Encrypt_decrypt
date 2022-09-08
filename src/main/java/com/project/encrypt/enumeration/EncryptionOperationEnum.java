package com.project.encrypt.enumeration;

@SuppressWarnings("all")
public enum EncryptionOperationEnum {

	ENCRYPT("ENCRYPT", "Encrypt"),
	DECRYPT("DECRYPT", "Decrypt");

	public static final EncryptionOperationEnum DEFAULT_ENCRYPTION_OPERATION = ENCRYPT;

	String status;
	String message;

	EncryptionOperationEnum(String status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public boolean isEqual(String status) {
		return getStatus().equalsIgnoreCase(status);
	}

	public static EncryptionOperationEnum of(String status) {
		for (EncryptionOperationEnum dt : EncryptionOperationEnum.values())
			if (dt.isEqual(status))
				return dt;
		return null;
	}
}
