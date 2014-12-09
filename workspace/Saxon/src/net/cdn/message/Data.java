package net.cdn.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Data implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -527560067138328780L;
	private byte[] data = null;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] encryptData(SecretKeySpec secretKeySpec, Object msg)
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

	public Object decryptData(SecretKeySpec secretKeySpec, byte[] msg)
			throws Exception {

		// Instantiate the cipher

		Cipher cipher = null;

		cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		byte[] decrypted = cipher.doFinal(msg);
		// System.out.println("encrypted string: " + asHex(encrypted));

		Object obj = null;   
		try {     ByteArrayInputStream bis = new ByteArrayInputStream (decrypted);     
		ObjectInputStream ois = new ObjectInputStream (bis);     
		obj = ois.readObject();  
		}   catch (Exception ex) {   
			//TODO: Handle the exception   }   catch (ClassNotFoundException ex) {     //TODO: Handle the exception   }   return obj
		}
		
		
		return obj;
	}
}
