import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private int[] x;
	private int[] y;
	private int[][] K;
	private int num_samples;

	public boolean debug = false;
	
	public float runPerceptron(String filename, int max_iterations, int dimension)
	{
		float testError;
		
		/* usage: java KernelPerceptron <inputfile> <max iterations> <kernel dimension>
		 * <input file> txt file with number of samples as first line and every line following contains two ints, <x,y>
		 * <max iterations> is the number of iterations the program should perform before giving up on classifying the samples
		 * <kernel dimension> is the dimension mapped to
		 */
		
		// read in the samples
		File infile = new File(filename);
		Scanner in;
		try {
			in = new Scanner(infile);
			parseInput(in);
		} catch (FileNotFoundException e) {
			System.out.println("error reading file");
			e.printStackTrace();
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

	private void parseInput(Scanner in)
	{
		num_samples = in.nextInt();
		x = new int[num_samples];
		y = new int[num_samples];

		if(debug) System.out.println("num samples: " + num_samples);
		for(int i = 0; i < num_samples; i++)
		{
			x[i] = in.nextInt();
			y[i] = in.nextInt();		
		}
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

