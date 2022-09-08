package com.project.encrypt.enumeration;

@SuppressWarnings("all")
public enum EncryptionTypeEnum {

	TWO_TWO_FOUR("TWO_TWO_FOUR", "224", "SHA-224"),
	TWO_FIVE_SIX("TWO_FIVE_SIX", "256", "SHA-256"),
	THREE_EIGHT_FOUR("THREE_EIGHT_FOUR", "384", "SHA-384"),
	FIVE_ONE_TWO("FIVE_ONE_TWO", "512", "SHA-512"),
	FIVE_ONE_TWO_SLASH_TWO_TWO_FOUR("FIVE_ONE_TWO_SLASH_TWO_TWO_FOUR", "512/224", "SHA-512/224"),
	FIVE_ONE_TWO_SLASH_TWO_FIVE_SIX("FIVE_ONE_TWO_SLASH_TWO_FIVE_SIX", "512/256", "SHA-512/256"),
	ONE_TWO_EIGHT("ONE_TWO_EIGHT", "128", "128"),
	ONE_NINE_TWO("ONE_NINE_TWO", "192", "192"),
	ONE_ZERO_TWO_FOUR("ONE_ZERO_TWO_FOUR", "1024", "1024"),
	TWO_ZERO_FOUR_EIGHT("TWO_ZERO_FOUR_EIGHT", "2048", "2048"),
	FOUR_ZERO_NINE_SIX("FOUR_ZERO_NINE_SIX", "4096", "4096"),;

	public static final EncryptionTypeEnum DEFAULT_ENCRYPTION_TYPE = TWO_FIVE_SIX;

	String status;
	String size;
	String type;

	EncryptionTypeEnum(String status, String size,  String type) {
		this.status = status;
		this.size = size;
		this.type = type;
	}
	
	public String getStatus() {
		return status;
	}

	public String getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public boolean isEqual(String size) {
		return getSize().equalsIgnoreCase(size);
	}

	public static EncryptionTypeEnum of(String size) {
		for (EncryptionTypeEnum dt : EncryptionTypeEnum.values())
			if (dt.isEqual(size))
				return dt;
		return null;
	}
}
