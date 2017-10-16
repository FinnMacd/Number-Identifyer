package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import main.Main;

public class ImportImages {
	
	private String path, path2;
	private FileInputStream pixelScanner, labelScanner;
	private int width;
	
	public ImportImages(String path, String path2, int width) throws IOException{
		
		pixelScanner = new FileInputStream(path);
		labelScanner = new FileInputStream(path2);
		
		this.path = path;
		this.path2 = path2;
		
		for(int i = 0; i < 8; i++){
			pixelScanner.read();
			pixelScanner.read();
			labelScanner.read();
		}
		
		this.width = width;
		
	}
	
	public double[][] nextImage() throws IOException{
		
		
		
		double[] label = new double[]{(double)labelScanner.read()};
		
		if(label[0] == -1){
			
			Main.import_ = new ImportImages(path,path2,width);
			return Main.import_.nextImage();
			
		}
		
		double[] pixels = new double[width*width];
		
		for(int i = 0; i < width*width;i++){
			
			pixels[i] = (double)pixelScanner.read()/255;
			
		}
		
		return new double[][]{label, pixels};
		
	}
	
}
