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
        
        normalizeInput();
	}
	
	private void normalizeInput()
	{
		float max[] = new float[width];
		float min[] = new float[width];
		float maxy;
		float miny;
		miny = maxy = y[0];
		
		// for each x attribute
		for(int i = 0; i < width; i++)
		{
			min[i] = max[i] = data[0][i];
			// find max and min
			for(int j = 0; j < height; j++)
			{
				if (min[i] > data[j][i]) min[i] = data[j][i];
				if (max[i] < data[j][i]) max[i] = data[j][i];
				
				if (miny > y[j]) miny = y[j];
				if (maxy < y[j]) maxy = y[j];
			}
			
		}
		
		// normalize the data
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < width; j++)
			{
				data[j][i] = (data[j][i] - (max[i] + min[i])/2)/((max[i] - min[i])/2);
			}
		}
		
		// normalize the classifications
		for(int i = 0; i < height; i++)
		{
			y[i] = (y[i] - (maxy + miny)/2)/((maxy - miny)/2);
		}
		
	}
    
	private void computeKernel()
    {
        K = new float[height][height];
        
        //For all rows
        for(int i = 0; i < height; i++)
        {
        	//Find the dot product between data[i] against every row in the matrix
            for(int j = 0; j < height; j++)
            {
                float dot = 0;
				try {
					dot += dot(data[i], data[j]);
				} catch (IllegalArgumentException e) {
					System.out.println("Vectors aren't of the same size");
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
	
	private float dot(float[] v1, float[] v2)
	{
		int lengthV1 = v1.length;
		int lengthV2 = v2.length;
		float result = 0;
		if(lengthV1 != lengthV2)
		{
			throw new IllegalArgumentException();
		}
		
		for(int i=0; i<lengthV1; i++)
		{
			result += v1[i]*v2[i];
		}
		
		return result;
	}
	
	private float[] transposeRow(float[][] data, int colNum )
	{
		float[] result = new float[height];
		
		for(int i=0; i<height; i++)
		{
			result[i] = data[i][colNum];
		}
		
		return result;
	}
}

