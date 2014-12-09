package encryption;

public class MessagePacket {

	// if true, authentication message is present in the packet
	private boolean authenticationPresent = false;

	// if true, the data is encrypted
	private boolean encryptionPresent = false;

	// if true, the packet have hash code
	private boolean hashCodePresent = false;

	private EncryptedKey encryptKey = null;

	private Data data = null;

	public boolean isAuthenticationPresent() {
		return authenticationPresent;
	}

	public void setAuthenticationPresent(boolean authenticationPresent) {
		this.authenticationPresent = authenticationPresent;
	}

	public boolean isEncryptionPresent() {
		return encryptionPresent;
	}

	public void setEncryptionPresent(boolean encryptionPresent) {
		this.encryptionPresent = encryptionPresent;
	}

	public boolean isHashCodePresent() {
		return hashCodePresent;
	}

	public void setHashCodePresent(boolean hashCodePresent) {
		this.hashCodePresent = hashCodePresent;
	}

	public EncryptedKey getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(EncryptedKey encryptKey) {
		this.encryptKey = encryptKey;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
