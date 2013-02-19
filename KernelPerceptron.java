import java.io.*;
import java.lang.Math.*;
import java.util.*;

public class KernelPerceptron {
	static int[] x;
	static int[] y;
	static int[][] K;
	static int num_samples;
	static int max_iterations;

	static boolean debug = false;

	public static void main(String[] args)
	{
		/* usage: java KernelPerceptron <inputfile> <max iterations> <kernel dimension>
		 * <input file> txt file with number of samples as first line and every line following contains two ints, <x,y>
		 * <max iterations> is the number of iterations the program should perform before giving up on classifying the samples
		 * <kernel dimension> is the dimension mapped to
		 */
		if( args.length != 3)
		{
			System.out.println("usage: java KernelPerceptron <inputfile> <max iterations> <kernel dimension>\n" +
					" <input file> txt file with number of samples as first line and every line following contains two ints, <x,y> \n" +
					" <max iterations> is the number of iterations the program should perform before giving up on classifying the samples\n " +
					" <kernel dimension> is the dimension mapped to");
		}

		// read in the samples
		File infile = new File(args[0]);
		Scanner in;
		try {
			in = new Scanner(infile);
			parseInput(in);
		} catch (FileNotFoundException e) {
			System.out.println("error reading file");
			e.printStackTrace();
		}
		

		// other logistical stuff
		max_iterations = Integer.parseInt(args[1]);
		int dimension = Integer.parseInt(args[2]);

		// compute the kernel matrix
		computeKernel(dimension);

		int[] c = classify();


	}

	public static void parseInput(Scanner in)
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

	public static void computeKernel(int dimension)
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

	public static int[] classify()
	{
		int i, j, k;
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
		if(misclassified) System.out.println("did not converge after " + max_iterations + "  iterations");
		System.out.print("c = ");
		for(i = 0; i < num_samples; i++)
		{
			System.out.print(c[i] + " ");
		}
		System.out.println();
		
		
		return c;
	}
}

