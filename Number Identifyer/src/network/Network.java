package network;

import math.Matrix;
import math.MatrixMath;
import math.Sigmoid;

public class Network {
	
	private int inputSize, hiddenSize, outputSize;
	
	private Matrix inputWeights, inputBiases, hiddenWeights, hiddenBiases;
	
	private double n = 0.04;
	
	public Network(int inputNeurons, int hiddenNeurons, int outputNeurons){
		
		inputWeights = new Matrix(inputNeurons,hiddenNeurons);
		inputBiases = new Matrix(1, hiddenNeurons);
		
		hiddenWeights = new Matrix(hiddenNeurons,outputNeurons);
		hiddenBiases = new Matrix(1, outputNeurons);
		
		inputWeights.randomize(-0.1, 0.1);
		inputBiases.randomize(-0.1, 0.1);
		
		hiddenWeights.randomize(-0.1, 0.1);
		hiddenBiases.randomize(-0.1, 0.1);
		
		inputSize = inputNeurons;
		hiddenSize = hiddenNeurons;
		outputSize = outputNeurons;
		
	}
	
	public Network(String s){
		
		String[] chars = s.split(" ");
		
		inputSize = Integer.parseInt(chars[0]);
		hiddenSize = Integer.parseInt(chars[1]);
		outputSize = Integer.parseInt(chars[2]);
		
		int i = 3;
		
		double[][] inputWeightA = new double[inputSize][hiddenSize], inputBiasA = new double[1][hiddenSize], 
				   hiddenWeightA = new double[hiddenSize][outputSize], hiddenBiasA = new double[1][outputSize];
		
		int temp = i;
		
		for(;i < temp + inputSize * hiddenSize; i++){
			
			int x = (i - temp)/inputSize, y = (i-temp)%inputSize;
			
			inputWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize * hiddenSize; i++){
			
			int x = (i - temp)/hiddenSize, y = (i-temp)%hiddenSize;
			
			hiddenWeightA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + hiddenSize; i++){
			
			int x = i - temp, y = 0;
			
			inputBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		temp = i;
		
		for( ;i < temp + outputSize; i++){
			
			int x = i - temp, y = 0;
			
			hiddenBiasA[y][x] = Double.parseDouble(chars[i]);
			
		}
		
		inputWeights = new Matrix(inputWeightA);
		inputBiases = new Matrix(inputBiasA);
		
		hiddenWeights = new Matrix(hiddenWeightA);
		hiddenBiases = new Matrix(hiddenBiasA);
		
	}
	
	public void stochasticTraining(Matrix inputs, Matrix outputs, Matrix delta2, Matrix delta1, Matrix output){
		
		Matrix dJdB2 = MatrixMath.elementMultiplication(Sigmoid.sigmoidPrime(delta2), MatrixMath.subtract(output,outputs));
		Matrix dJdW2 = MatrixMath.multiply(Sigmoid.sigmoid(delta1).getTransposition(), dJdB2);
		
		Matrix dJdB1 = MatrixMath.multiply(Sigmoid.sigmoidPrime(delta1), MatrixMath.dot(dJdB2,hiddenWeights.getTransposition()));
		Matrix dJdW1 = MatrixMath.multiply(inputs.getTransposition(), dJdB1);
		
		
		hiddenBiases = MatrixMath.subtract(hiddenBiases, MatrixMath.multiply(dJdB2, n));
		hiddenWeights = MatrixMath.subtract(hiddenWeights, MatrixMath.multiply(dJdW2, n));
		
		inputBiases = MatrixMath.subtract(inputBiases, MatrixMath.multiply(dJdB1, n));
		inputWeights = MatrixMath.subtract(inputWeights, MatrixMath.multiply(dJdW1, n));
		
	}
	
	public Matrix[] test(Matrix inputs){
		
		Matrix delta1 = MatrixMath.add(MatrixMath.multiply(inputs, inputWeights), inputBiases);
		
		Matrix delta2 = MatrixMath.add(MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights), hiddenBiases);
		
		return new Matrix[]{delta1, delta2,(Sigmoid.sigmoid(delta2))};
		
	}
	
	public Matrix simpleTest(Matrix inputs){
		
		Matrix delta1 = MatrixMath.add(MatrixMath.multiply(inputs, inputWeights), inputBiases);
		
		Matrix delta2 = MatrixMath.add(MatrixMath.multiply(Sigmoid.sigmoid(delta1), hiddenWeights), hiddenBiases);
		
		return (Sigmoid.sigmoid(delta2));
		
	}
	
	private double sigs(double input){
		
		int pow = (int)Math.log10(input);
		
		input = (input/Math.pow(10, pow))*1000;
		
		input = (int)input;
		
		input = (input/1000)*Math.pow(10, pow);
		
		return input;
		
	}
	
	public String toString(){
		
		String output = inputSize + " " + hiddenSize + " " + outputSize + " ";
		
		
		for(double d:inputWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenWeights.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:inputBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		for(double d:hiddenBiases.getSingleArray()){
			output += sigs(d) + " ";
		}
		
		return output;
		
	}
	
	
}
