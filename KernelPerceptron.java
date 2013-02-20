import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private int[] x;
	private int[] y;
	private int[][] K;
	private int num_samples;
	private int max_iterations;

	public boolean debug = false;

	public static void main(String[] args)
	{
		KernelPerceptron kernelPerceptron = new KernelPerceptron();
		
		kernelPerceptron.runPerceptron(args);

		return;
	}
	
	public void runPerceptron(String[] args)
	{
		String filename = null;
		int dimension = 0;
		
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

		//If no filename specified, use the default filename
		try{
			filename = args[0];
		} catch(ArrayIndexOutOfBoundsException e){
			filename = "in.txt";
		}
		
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
		

		// other logistical stuff
		//If no cmd line args are specified then it uses default values
		try {
			max_iterations = Integer.parseInt(args[1]);
			dimension = Integer.parseInt(args[2]);
		} catch (ArrayIndexOutOfBoundsException e) {
			max_iterations = 10;
			dimension = 2;
		}

		
		// compute the kernel matrix
		computeKernel(dimension);

		int[] c = classify();
		
		printClassification(c);

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

	private int[] classify()
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

