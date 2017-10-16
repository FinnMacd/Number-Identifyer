package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ImportNet {

	public static String readNet(String path) throws FileNotFoundException{
		
		Scanner in = new Scanner(new File(path + ".net"));
		
		String out = in.nextLine();
		
		in.close();
		
		return out;
		
	}
	
}
