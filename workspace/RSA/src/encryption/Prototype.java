package encryption;

import javax.crypto.spec.SecretKeySpec;

public class Prototype {

	public static void main(String[] args) {

		MessagePacket msgPacket = new MessagePacket();
		EncryptedKey encryptKey = new EncryptedKey();
		Data data = new Data();
		String msg = "Tivakar";
		
		Object msg_obj = (Object) msg;
		
		byte[] encryptedMsg = null;
		SecretKeySpec secretKeySpec1= null;

		// Create AES key and encrypt it
		SecretKeySpec secretKeySpec = encryptKey.createAESKey();
		encryptKey.setKey(encryptKey.encryptAESKey(secretKeySpec));
		
		try {
			data.setData(data.encryptData(secretKeySpec, msg_obj));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Put encrypt key and encrypted data into the message
		msgPacket.setEncryptKey(encryptKey);
		msgPacket.setData(data);

		/****************************************** Decryption ****************************************************/

		DecryptMessagePacket decryptPacket = new DecryptMessagePacket();

		try {
			secretKeySpec1 = decryptPacket
					.decryptMessage(msgPacket);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object msg_obj_decrypt = null;
		
		try {
			msg_obj_decrypt = data.decryptData(secretKeySpec1, data.getData());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String decrypted = (String)msg_obj_decrypt;
		
		System.out.println(decrypted);
	}

}
