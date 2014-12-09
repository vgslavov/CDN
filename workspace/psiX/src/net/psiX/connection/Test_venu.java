package net.psiX.connection;
import java.util.*;

public class Test_venu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 String array[]=function1();
	 System.out.println("Here is the element:"+array[0]);
	    
	
	 
		
	}
	public static String[] function1(){
		String array[];
		ArrayList al = new ArrayList();
		al.add("hello");
		al.add("how");
		array = (String[]) al.toArray(new String[al.size()]); 
		return array;
	}

}
