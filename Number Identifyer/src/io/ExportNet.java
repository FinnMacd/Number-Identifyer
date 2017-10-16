package io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import network.Network;

public class ExportNet {
	
	public static void saveNet(String path, Network net) throws IOException{
		
		Writer writer;
		
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + ".net"), "utf-8"));
		
		writer.write(net.toString());
		
		writer.flush();
		
		writer.close();
		
	}
	
}
