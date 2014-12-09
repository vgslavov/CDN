package net.cdn.functions;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class messageDigest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MessageDigest msgDigest = null;
		byte[] output1 = null;
		byte[] output2 = null;
		
		String heho = "Tivakar";
		String hehe = "Divakar";
		
		
		try {
			
			msgDigest = MessageDigest.getInstance("SHA");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		msgDigest.update(heho.getBytes());
		output1 = msgDigest.digest();
		
		System.out.println(new String(output1));
		
		msgDigest.update(hehe.getBytes());
		output1 = msgDigest.digest();
		
		System.out.println(new String(output1));
		
		msgDigest.update(heho.getBytes());
		output2 = msgDigest.digest();
		
		System.out.println(new String(output2));
		
		msgDigest.update(heho.getBytes());
		output1 = msgDigest.digest();
		
		System.out.println(new String(output1));
		
		System.out.println(Arrays.equals(output1, output2));
		
	}

}
