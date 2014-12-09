package encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import rsa.RSA;

public class EncryptedKey {

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

	public byte[] encryptAESKey(SecretKeySpec secretKeySpec) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] msg = null;
		byte[] msg1 = null;

		Object key = (Object) secretKeySpec;
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(key);
			oos.flush();
			oos.close();
			bos.close();
			msg = bos.toByteArray();
		} catch (IOException ex) { // TODO: Handle the exception }
		}

//		System.out.println("----Before encrypt-----");
//		System.out.println(new String(msg));
//		System.out.println("----Before encrypt-----");

//		try {
//			ByteArrayInputStream bis1 = new ByteArrayInputStream(msg);
//			ObjectInputStream ois = new ObjectInputStream(bis1);
//			ois.readObject();
//		} catch (Exception ex) { // TODO: Handle the exception }
//		}

		RSA rsa = new RSA();
		try {
			//rsa.createPrivateNPublicKey();
			msg1 = rsa.rsaEncrypt(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg1;
	}

}
