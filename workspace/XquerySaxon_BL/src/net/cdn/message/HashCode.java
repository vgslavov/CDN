package net.cdn.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HashCode implements Serializable {

	private static final long serialVersionUID = -4453638224867053592L;

	private byte[] hashCode = null;

	public byte[] getHashCode() {
		return hashCode;
	}

	public void setHashCode(byte[] hashCode) {
		this.hashCode = hashCode;
	}

	
	/*
	 * Previously encrypted using AES key
	 * 
	 * Delete this
	 */
	
/*	
	public byte[] encryptHashCode(SecretKeySpec secretKeySpec, Object msg)
			throws Exception {

		// Instantiate the cipher

		Cipher cipher = null;

		cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] msgBytes = null;

		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(msg);
		oos.flush();
		oos.close();
		bos.close();
		msgBytes = bos.toByteArray();

		byte[] encrypted = cipher.doFinal(msgBytes);
		// System.out.println("encrypted string: " + asHex(encrypted));

		return encrypted;
	}

	public Object decryptHashCode(SecretKeySpec secretKeySpec, byte[] msg)
			throws Exception {

		// Instantiate the cipher

		Cipher cipher = null;

		cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		byte[] decrypted = cipher.doFinal(msg);
		// System.out.println("encrypted string: " + asHex(encrypted));

		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(decrypted);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (Exception ex) {
			// TODO: Handle the exception } catch (ClassNotFoundException ex) {
			// //TODO: Handle the exception } return obj
		}

		return obj;
	}
*/
	
	
	/*
	 * encryptHashcode - Encrypts SecretKeySpec obj with the RSA Public Key
	 */
	public byte[] encryptHashCode(byte[] hashCodeArray) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] encryptedKey = null;

		RSA rsa = new RSA();

		// Encrypts hash code using private key

		try {

			encryptedKey = rsa.rsaEncryptPrivate(hashCodeArray);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return encryptedKey;
	}

	/*
	 * Decrypt hashCode 
	 */
	public byte[] decryptHashCode(byte[] msg) {

		RSA rsa = null;
		byte[] decrytp_arr = null;
		
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		SecretKeySpec skeySpec = null;
		
		// Decrypt hashcode using public key
		try {

			rsa = new RSA();
			decrytp_arr = rsa.rsaDecryptPublicKey(msg);

		} catch (Exception e1) {
			
			System.out.println("Error in decrypt HashCode");
			e1.printStackTrace();
		}

//		bis = new ByteArrayInputStream(decrytp_arr);
//
//		try {
//			
//			ois = new ObjectInputStream(bis);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// Get SecretKeySpec
//		try {
//			
//			skeySpec = (SecretKeySpec) ois.readObject();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return decrytp_arr;

	}
	
	
	
	/**
	 *
	 *	generateHash Method is used for generating hash code for an object
	 * 
	 */
	public byte[] generateHash(Object msg) {

		MessageDigest msgDigest = null;
		byte[] hashCode = null;
		
		try {

			msgDigest = MessageDigest.getInstance("SHA");

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Get hash code
		msgDigest.update(toByteArray(msg));
		hashCode = msgDigest.digest();
		
//		System.out.println("inga--"+new String(hashCode));
		
		return hashCode;

	}
	
	public byte[] toByteArray (Object obj)
	{
	  byte[] bytes = null;
	  ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  try {
	    ObjectOutputStream oos = new ObjectOutputStream(bos); 
	    oos.writeObject(obj);
	    oos.flush(); 
	    oos.close(); 
	    bos.close();
	    bytes = bos.toByteArray ();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  return bytes;
	}
	    
	public Object toObject (byte[] bytes)
	{
	  Object obj = null;
	  try {
	    ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
	    ObjectInputStream ois = new ObjectInputStream (bis);
	    obj = ois.readObject();
	  }
	  catch (IOException ex) {
	    //TODO: Handle the exception
	  }
	  catch (ClassNotFoundException ex) {
	    //TODO: Handle the exception
	  }
	  return obj;
	}
	

}
