import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private float[][] K;
    
    //[height][width]
    private float[][] data;
    private float[][] y;
    private int height,widthx, widthy;

    PrintWriter debugOut;
    
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
        widthx = in.nextInt();
        widthy = in.nextInt();
        height = in.nextInt();
        data = new float[height][widthx];
        y = new float[height][widthy];
        
        for(int i = 0;i<height;i++)
        {
            for(int j = 0;j<widthx;j++)
            {
                data[i][j] = in.nextFloat();
                System.out.println(data[i][j]);
            }
            for(int j = 0;j<widthy;j++)
            {
                y[i][j] = in.nextFloat();
                System.out.println(y[i][j]);
            }
        }
        
        normalizeInput();
	}
	
	private void normalizeInput()
	{
		float max[] = new float[widthx];
		float min[] = new float[widthx];
		float maxy;
		float miny;
		//miny = maxy = y[0];
		
		// for each x attribute
		for(int i = 0; i < widthx; i++)
		{
			min[i] = max[i] = data[0][i];
			// find max and min
			for(int j = 0; j < height; j++)
			{
				if (min[i] > data[j][i]) min[i] = data[j][i];
				if (max[i] < data[j][i]) max[i] = data[j][i];
				
				//if (miny > y[j]) miny = y[j];
				//if (maxy < y[j]) maxy = y[j];
			}
			
		}
		
		// normalize the data
		for(int i = 0; i < widthx; i++)
		{
			for(int j = 0; j < widthx; j++)
			{
				data[j][i] = (data[j][i] - (max[i] + min[i])/2)/((max[i] - min[i])/2);
			}
		}
		
		/* normalize the classifications
		for(int i = 0; i < height; i++)
		{
			y[i] = (y[i] - (maxy + miny)/2)/((maxy - miny)/2);
		} */
		
	}
    
	private void computeKernel()
    {
        K = new float[height][height];
        
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < height; j++)
            {
                float dot = 0.0f;
                for(int k=0;k<widthx;k++)
                {
                    dot += data[i][k]*data[j][k];
                }
                
                K[j][i] = (float)Math.pow(1 + dot, widthx);
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
				for(int l = 0; l < widthy; l++)
				{
					if(sum*y[j][l] <= 0)
					{
						c[j] += y[j][l];
						misclassified = true;
						errCount++;
					}
				}
			}
		}
		
		return c;
	}
}

