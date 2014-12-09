package net.cdn.message;

import java.io.Serializable;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

public class CDNMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4546302434671315540L;

	// if true, the data is encrypted
	private boolean encryptionPresent = false;

	// Message object that contains message object and encryption key in
	// encrypted form
	private MessageObject msgObj = null;

	//
	private HashCode hashCode = null;

	public boolean isEncryptionPresent() {
		return encryptionPresent;
	}

	public void setEncryptionPresent(boolean encryptionPresent) {
		this.encryptionPresent = encryptionPresent;
	}

	public MessageObject getMsgObj() {
		return msgObj;
	}

	public HashCode getHashCode() {
		return hashCode;
	}

	public void setHashCode(HashCode hashCode) {
		this.hashCode = hashCode;
	}

	/*
	 * setMessage method
	 * 
	 * 1. Creates AES key 2. Encrypt AES key using public key of receiver 3.
	 * Encrypt Data using AES 4. set the Object dataToSend
	 */

	public void setMessage(Object orgnlMsg) {

		AESKey aesKey = null;
		SecretKeySpec secretKeySpec = null;
		Data data = null;
		HashCode hashCode = null;
		byte[] hashCodeArray = null;

		// Create AES key and encrypt it
		try {

			aesKey = new AESKey();
			secretKeySpec = aesKey.createAESKey();
			aesKey.setKey(aesKey.encryptAESKey(secretKeySpec));

		} catch (Exception e1) {

			System.out
					.println("Exception in AES key generation and encryption");
			e1.printStackTrace();
		}

		// Data Encryption
		try {

			data = new Data();
			data.setData(data.encryptData(secretKeySpec, orgnlMsg));

		} catch (Exception e2) {

			System.out.println("Exception in data encryption");
			e2.printStackTrace();
		}

		// Generate Hash Code and HashCode Encryption
		try {

			hashCode = new HashCode();

			// Generate Hashcode
			hashCodeArray = hashCode.generateHash(orgnlMsg);

			
			System.out.println("**********" + hashCodeArray);
			
//			hashCode.setHashCode(hashCodeArray);
			
			// Encrypt hashcode and set in hashCode object
			hashCode.setHashCode(hashCode.encryptHashCode(
					hashCodeArray));

//			hashCode.decryptHashCode(hashCode.encryptHashCode(
//					hashCodeArray));
			
			// set hash code in CDNMessage object
			setHashCode(hashCode);

		} catch (Exception e) {

			System.out
					.println("Exception in hashCode Generation and encryption");
			e.printStackTrace();
		}

		// Add Encryption key and encrypted data to message object
		try {
			msgObj = new MessageObject();
			msgObj.setEncryptKey(aesKey);
			msgObj.setEncryptedData(data);
			setEncryptionPresent(true);

		} catch (Exception e3) {

			System.out.println("Exception while setting MessageObject");
			e3.printStackTrace();
		}

	}

	public Object getMessage() {

		Object orgnlMsg = null;
		MessageObject encryptedMsgObj = null;
		SecretKeySpec secretKeySpec = null;
		AESKey aesKey = null;
		Data data = null;

		// When message has encryption
		if (this.isEncryptionPresent()) {

			// Get encrypted message Object
			encryptedMsgObj = this.getMsgObj();

			// Get enrypted AES Key
			aesKey = encryptedMsgObj.getEncryptKey();

			// Get encrypted Data
			data = encryptedMsgObj.getEncryptedData();

			// Decrypt AES Key
			secretKeySpec = aesKey.decryptAESKey(aesKey);

			// Decrypt data
			try {

				orgnlMsg = data.decryptData(secretKeySpec, data.getData());
			} catch (Exception e) {

				System.out.println("Exception when decrypting data");
				e.printStackTrace();
			}

			//Get message and generate hashcode
			byte[] origHashcode = hashCode.generateHash(orgnlMsg);
			
			//Get hashcode from the received message
	    	byte[] gotHashCode = getHashCodeFromEncrypted();
			
	    	//compare both hash code
	    	boolean blnResult = Arrays.equals(origHashcode,gotHashCode);
	    	
	    	//If both are not equal, then user not authenticated
	    	if(!blnResult){
	    		orgnlMsg = null;
	    	}
	    	
		}

		return orgnlMsg;
	}

	public byte[] getHashCodeFromEncrypted() {

		byte[] orgnlMsg = null;
		MessageObject encryptedMsgObj = null;
		SecretKeySpec secretKeySpec = null;
		AESKey aesKey = null;
		HashCode hashCode = null;

		// When message has encryption
		if (this.isEncryptionPresent()) {

			// Get encrypted message Object
			encryptedMsgObj = this.getMsgObj();

			// Get enrypted AES Key
			aesKey = encryptedMsgObj.getEncryptKey();

			// Get encrypted hashCode
			hashCode = getHashCode();

			// Decrypt AES Key
			secretKeySpec = aesKey.decryptAESKey(aesKey);

			// Decrypt data
			try {

				orgnlMsg = hashCode.decryptHashCode(
						hashCode.getHashCode());
				
//				orgnlMsg = hashCode.getHashCode();
				
			} catch (Exception e) {

				System.out.println("Exception when decrypting data");
				e.printStackTrace();
			}

		}
		
		return orgnlMsg;
	}
}
