package net.cdn.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7454106634292031252L;
	private byte[] key = null;

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public SecretKeySpec createAESKey() {

		// Get the KeyGenerator
		KeyGenerator kgen = null;

		try {
			kgen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		kgen.init(128); // 192 and 256 bits may not be available

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		return skeySpec;

	}

	/*
	 * encryptAESKey - Encrypts SecretKeySpec obj with the RSA Public Key
	 */
	public byte[] encryptAESKey(SecretKeySpec secretKeySpec) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] msg = null;
		byte[] encryptedKey = null;

		Object key = (Object) secretKeySpec;

		try {

			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(key);

			oos.flush();
			oos.close();
			bos.close();

			msg = bos.toByteArray();

		} catch (IOException ex) {

		}

		RSA rsa = new RSA();

		// Encrypts AES Key
		try {

			encryptedKey = rsa.rsaEncrypt(msg);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return encryptedKey;
	}

	public SecretKeySpec decryptAESKey(AESKey encryptedKey) {

		RSA rsa = null;
		byte[] decrytp_arr = null;
		
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		SecretKeySpec skeySpec = null;
		
		// Get encrypted Key
		byte[] encrypt_arr = encryptedKey.getKey();

		// Decrypt Key
		try {

			rsa = new RSA();
			decrytp_arr = rsa.rsaDecrypt(encrypt_arr);

		} catch (Exception e1) {
			
			System.out.println("Error in decryptAESKey");
			e1.printStackTrace();
		}

		bis = new ByteArrayInputStream(decrytp_arr);

		try {
			
			ois = new ObjectInputStream(bis);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Get SecretKeySpec
		try {
			
			skeySpec = (SecretKeySpec) ois.readObject();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return skeySpec;

	}

}
