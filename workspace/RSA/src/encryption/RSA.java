package encryption;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import encryption.EncryptedKey;

public class RSA {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		EncryptedKey enKey = new EncryptedKey();
		//enKey.createAESKey();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] msg =null;
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject((Object)enKey.createAESKey());
			oos.flush();
			oos.close();
			bos.close();
			msg = bos.toByteArray();
		} catch (IOException ex) { // TODO: Handle the exception }
		}

		System.out.println("----Before encrypt-----");
		System.out.println(new String(msg));
		System.out.println("----Before encrypt-----");
		//byte[] msg = { 'H', 'd' };
		RSA rsa = new RSA();
		rsa.createPrivateNPublicKey();
//		System.out.println(rsa.rsaDecrypt(rsa.rsaEncrypt(msg)));
//		System.out.println(rsa.rsaEncrypt(msg));
		//System.out.println("msg---" + new String(msg));
		//System.out.println();
		byte[] msg1 = rsa.rsaDecrypt(rsa.rsaEncrypt(msg));
		System.out.println("----After decrypt-----");
		System.out.println(new String(msg1));
		System.out.println("----After decrypt-----");
		//System.out.println(new String(rsa.rsaDecrypt(rsa.rsaEncrypt(msg))));
	}

	public void createPrivateNPublicKey() throws Exception {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kpg.initialize(2048);// bit length of modulus
		KeyPair kp = kpg.genKeyPair();

		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
				RSAPublicKeySpec.class);
		RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
				RSAPrivateKeySpec.class);

		saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
		saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());

	}

	public void saveToFile(String fileName, BigInteger mod, BigInteger exp)
			throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}

	public byte[] rsaEncrypt(byte[] data) throws Exception {
		PublicKey pubKey = readKeyFromFilePublic("public.key");
		System.out.println("------------Public---------");
		System.out.println("pubKey- " + pubKey);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;
	}

	PublicKey readKeyFromFilePublic(String keyFileName) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(
				keyFileName));

		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	public byte[] rsaDecrypt(byte[] cipherData) throws Exception {
		PrivateKey privateKey = readKeyFromFilePrivate("private.key");
		System.out.println("------------Private---------");
		System.out.println("private- " + privateKey);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] data = cipher.doFinal(cipherData);
		return data;
	}

	PrivateKey readKeyFromFilePrivate(String keyFileName) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(
				keyFileName));

		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger d = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, d);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = fact.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

}
