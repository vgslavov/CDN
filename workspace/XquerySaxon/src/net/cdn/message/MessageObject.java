package net.cdn.message;

import java.io.Serializable;

public class MessageObject implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -8319453687246732846L;

	// Key for the encrypted data
	private AESKey encryptKey = null;

	// Encrypted Data
	private Data encryptedData = null;

	public AESKey getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(AESKey encryptKey) {
		this.encryptKey = encryptKey;
	}

	public Data getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(Data encryptedData) {
		this.encryptedData = encryptedData;
	}


}
