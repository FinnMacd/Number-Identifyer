package main;

import io.ExportNet;
import io.ImportImages;
import io.ImportNet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math.Matrix;
import network.Network;


public class Main extends JPanel implements Runnable, MouseListener, MouseMotionListener{
	
	private static int width;
	
	private static double[] board;
	
	public static ImportImages import_;
	
	public static Network network;
	
	static Button train, run, save, load;
	static JFrame j;
	static JTextField textFeild;
	
	public int mx = 0,my = 0, lx,ly;
	
	public static void main(String[] args){
		
		j = new JFrame();
		Main main = new Main(28);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setTitle("NN");
		
		j.add(main);
		j.pack();
		j.setLocationRelativeTo(null);
		
		j.setVisible(true);
		
	}
	
	public Main(int width){
		this.width = width;
		
		board = new double[width*width];
		
		network = new Network(width*width, 200,10);
		
		train = new Button("Train", 20, 700, 40);
		run = new Button("Run", 120, 700, 40);
		save = new Button("Save", 320, 700, 40);
		load = new Button("Load", 420, 700, 40);
		
		textFeild = new JFormattedTextField();
		textFeild.setBounds(220,670,80,40);
		textFeild.setFont(new Font("TimesNewRoman",0,40));
		
		
		this.add(textFeild);
		this.setLayout(null);
		setPreferredSize(new Dimension(680,760));
		
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		try {
			import_ = new ImportImages("./res/images","./res/labels",28);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(this).start();
		
		for(int i = 0;i < board.length; i++){
				
			board[i] = 0;
				
		}
		
	}
	
	public void run() {
		
		while(true){
			train.update(key == 1, mx, my);
			run.update(key == 1, mx, my);
			save.update(key == 1, mx, my);
			load.update(key == 1, mx, my);
			
			if(run.isClicked()){
				key = 0;
							
				Matrix output = network.simpleTest(Matrix.rowMatrix(board));
				
				double in = 0, l = 0;
				
				for(int i = 0; i < output.getWidth(); i++){
					
					if(output.getAttribute(i, 0)>l){
						l = output.getAttribute(i, 0);
						in = i;
					}
					
				}
				
				System.out.println(in);
			}
			
			if(save.isClicked()){
				key = 0;
				try {
					ExportNet.saveNet("net", network);
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("saved");
				
			}
			
			if(load.isClicked()){
				key = 0;
				try {
					network = new Network(ImportNet.readNet("net"));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.println("loaded");
			}
			
			if(train.isClicked()){
				key = 0;
				
				double[][] input = null;
				double[] outputs = new double[10];
				Matrix[] output = null;
				
				double limit = Integer.parseInt(textFeild.getText());
				
				double counter = 0;
				
				main:
				for(int j = 0; j < limit; j++){
				
					
					if(limit>=1000 && j%(limit/20) == 0)System.out.println(j*100/limit +"% done");
					
					try {
						input = import_.nextImage();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					board = input[1];
					
					for(int i = 0; i < 10; i++){
						
						outputs[i] = 0;
						if(i == input[0][0])outputs[i] = 1;
						
					}
					
					output = network.test(Matrix.rowMatrix(board));

					network.stochasticTraining(Matrix.rowMatrix(board), Matrix.rowMatrix(outputs), output[2], output[0], output[1]);
					
					double in = 0, l = 0;
					
					for(int i = 0; i < output[2].getWidth(); i++){
						
						if(output[2].getAttribute(i, 0)>l){
							l = output[2].getAttribute(i, 0);
							in = i;
						}
						
					}
					
					if(in == input[0][0])counter++;
					
					
				}
				
				double in = 0, l = 0;
				
				for(int i = 0; i < output[2].getWidth(); i++){
					
					if(output[2].getAttribute(i, 0)>l){
						l = output[2].getAttribute(i, 0);
						in = i;
					}
					
				}
				
				System.out.println("Actual: " + input[0][0] + ", Predicted: " + in);
				
				
				System.out.println("Accuracy = " + counter/limit);
				
				//output.printMatrix();
				
			}
			
			
			j.repaint();
			
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < width; y++){
				
				if(board[y*width + x] > 1)board[y*width + x] = 1;
				
				g.setColor(new Color((int)(255*(1-board[y*width + x])),(int)(255*(1-board[y*width + x])),(int)(255*(1-board[y*width + x]))));
				g.fillRect(20 + x*(640/width), 20 + y*(640/width), (640/width), (640/width));
				g.setColor(Color.black);
				g.drawRect(20 + x*(640/width), 20 + y*(640/width), (640/width), (640/width));
				
			}
		}
		
		train.draw((Graphics2D)g);
		run.draw((Graphics2D)g);
		save.draw((Graphics2D)g);
		load.draw((Graphics2D)g);
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
	static int key = 0;
	
	public void mousePressed(MouseEvent e) {
		key = e.getButton();
		
		if(key == 3){
			for(int i = 0;i < board.length; i++){
				
				board[i] = 0;
					
			}			
		}
	}

	public void mouseReleased(MouseEvent e) {
		key = 0;
		ly = lx = -1;
	}

	public void mouseDragged(MouseEvent e){
		int x = (int)((e.getX()-31)/(640/width));
		int y = (int)((e.getY()-31)/(640/width));
		
		mx = e.getX();
		my = e.getY();

		if(ly == y && lx == x)return;

		lx = x;
		ly = y;
		
		if(!(x >= 0 && x < width && y >= 0 && y < width))return;
		
		if(key == 1){
			
			try{
				
				board[y*width + x] += 0.7;
				board[y*width + x + 1]+=0.7;
				board[y*width + x + 1 + width]+=0.7;
				board[y*width + x + width]+=0.7;
				
			}catch(ArrayIndexOutOfBoundsException t){
			}
			
		}else if(key == 3){
			for(int i = 0;i < board.length; i++){
				
				board[i] = 0;
					
			}			
		}
		
	}

	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		
	}
	
}
