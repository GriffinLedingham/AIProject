import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private int[][] K;
	private int num_samples;
	float[][] trainingSetXY;
	int[] classLabels;
	int trainingSetRowSize;
	int trainingSetColSize;

	public boolean debug = false;
	
	public float runPerceptron(String dataFilename, String classFilename, int max_iterations, int dimension)
	{
		float testError;
		
		/* usage: java KernelPerceptron <inputfile> <max iterations> <kernel dimension>
		 * <input file> txt file with number of samples as first line and every line following contains two ints, <x,y>
		 * <max iterations> is the number of iterations the program should perform before giving up on classifying the samples
		 * <kernel dimension> is the dimension mapped to
		 */
		
		// read in the samples
			File infile = new File(dataFilename);
			Scanner inData, inClass;
			try {
				inData = new Scanner(infile);
				parseDataInput(inData);
			} catch (FileNotFoundException e) {
				System.out.println("error reading file");
				e.printStackTrace();
				return -1;
			}
			
			File inClassFile = new File(classFilename);
			try {
				inClass = new Scanner(inClassFile);
				parseClassInput(inClass);
			} catch (FileNotFoundException e) {
				System.out.println("error reading file");
				e.printStackTrace();
				return -1;
			}
		
		// compute the kernel matrix
		computeKernel(dimension);

		int[] c = classify(max_iterations);
		
		printClassification(c);
		
		testError = testError();
		
		return testError;
	}
	
	private float testError()
	{
		float error = 0;
		//TODO
		
		return error;
	}

	private void parseDataInput(Scanner in)
	{
		int i = 0;
		trainingSetColSize = in.nextInt();
		trainingSetRowSize = in.nextInt();
		trainingSetXY = new float[trainingSetColSize][trainingSetRowSize];

		while(in.hasNext()){
			for(int j=0; j<trainingSetColSize; j++)
			{
				trainingSetXY[j][i] = in.nextFloat();
			}
			i++;
		}

		return;
	}
	
	private void parseClassInput(Scanner in)
	{
		int i=0;
		classLabels = new int[trainingSetRowSize];

		while(in.hasNext()){
			classLabels[i] = in.nextInt();
			i++;
		}

		return;
	}

	private void computeKernel(int dimension)
	{
		if(debug) System.out.println("Kernel Matrix");
		K = new int[num_samples][num_samples];
		for(int i = 0; i < num_samples; i++)
		{
			for(int j = 0; j < num_samples; j++)
			{
				K[i][j] = (int) Math.pow(1 + x[i]*y[j], dimension);
				if(debug) System.out.print(K[i][j] + " ");
			}
			if(debug) System.out.println();
		}

	}
	
	private float computeDotProd(int dataindex, int classindex)
	{
		// need dot product of trainsetXY and classLabels
		
		
	}
	
	private void printClassification(int[] classifcation)
	{
		int i;
		System.out.print("c = ");
		for(i = 0; i < num_samples; i++)
		{
			System.out.print(classifcation[i] + " ");
		}
		System.out.println();
	}

	private int[] classify(int max_iterations)
	{
		int j, k;
		int sum;
		int count = 0;

		// the classifier
		int[] c = new int[num_samples];
		Arrays.fill(c, 0);

		// loop control
		boolean misclassified = true;

		while(misclassified && (count < max_iterations))
		{
			count++;
			misclassified = false;
			for(j=0; j < num_samples; j++)
			{
				sum = 0;
				for(k = 0; k < num_samples; k++)
				{
					sum += c[k]*K[k][j];	
				}
				sum *= y[j];

				if(sum <= 0)
				{
					c[j] += y[j];
					misclassified = true;
				}
			}

		}

		System.out.println();		
		
		return c;
	}
}

