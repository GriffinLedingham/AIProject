import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private float[][] K;
    
    //[height][width]
    private float[][] data;
    private float[] y;
    private int height,width;
    
	public boolean debug = false;
	
	public float runPerceptron(String filename, int max_iterations)
	{
		float testError;
		
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
		computeKernel();
        
		float[] c = new float[height];
		//printClassification(c);
        
        c= classify(max_iterations);
		
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
        width = in.nextInt();
        height = in.nextInt();
        data = new float[height][width];
        y = new float[height];
        
        for(int i = 0;i<height;i++)
        {
            for(int j = 0;j<width;j++)
            {
                data[i][j] = in.nextFloat();
                System.out.println(data[i][j]);
            }
            y[i] = in.nextFloat();
        }
	}
    
	private void computeKernel()
    {
        K = new float[height][height];
        
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < height; j++)
            {
                float dot = 0.0f;
                for(int k=0;k<width;k++)
                {
                    dot += data[i][k]*data[j][k];
                }
                
                K[j][i] = (float)Math.pow(1 + dot, width);
            }
        }
    }
	
	private void printClassification(float[] classifcation)
	{
		int i;
		System.out.print("c = ");
		for(i = 0; i < height; i++)
		{
			System.out.print(classifcation[i] + " ");
		}
		System.out.println();
	}
    
	private float[] classify(int max_iterations)
	{
		int count = 0;
        
		// the classifier
		float[] c = new float[height];
		Arrays.fill(c, 0);
        
		// loop control
		boolean misclassified = true;
        
		while(misclassified && (count < max_iterations))
		{
			count++;
			misclassified = false;
			for(int j=0;j<height;j++)
			{
				float sum = 0.0f;
				for(int k=0;k<height;k++)
				{
					sum+= K[k][j]*c[k];
				}
				if(sum*y[j] <= 0)
				{
					c[j] += y[j];
					misclassified = true;
				}
			}
		}
		
		return c;
	}
}

