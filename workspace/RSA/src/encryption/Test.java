package encryption;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MessagePacket e = new MessagePacket();
		MessagePacket e1 = new MessagePacket();
		
		System.out.println(e.hashCode());
		MessagePacket e2 = e;
		System.out.println(e1.hashCode());
		System.out.println(e2.hashCode());
		System.out.println(e.hashCode());
	}

}
