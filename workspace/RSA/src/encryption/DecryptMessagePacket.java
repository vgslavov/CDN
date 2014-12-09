package encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class DecryptMessagePacket {

	private MessagePacket msgPacket = null;

	public MessagePacket getMsgPacket() {
		return msgPacket;
	}

	public void setMsgPacket(MessagePacket msgPacket) {
		this.msgPacket = msgPacket;
	}

	public SecretKeySpec decryptMessage(MessagePacket msgPacket) throws Exception {

		Data data = msgPacket.getData();
		EncryptedKey encryptKey = msgPacket.getEncryptKey();

		byte[] encrypt_arr = encryptKey.getKey();
		
		RSA rsa = new RSA();
		byte[] decrytp_arr = rsa.rsaDecrypt(encrypt_arr);
		
		
		
		ByteArrayInputStream bis = new ByteArrayInputStream(decrytp_arr);
		ObjectInputStream ois = null;
		SecretKeySpec skeySpec = null;

		ois = new ObjectInputStream(bis);

		skeySpec = (SecretKeySpec) ois.readObject();
		
		return skeySpec;

	}

}
