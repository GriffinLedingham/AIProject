import java.io.*;
import java.util.*;

public class KernelPerceptron {
	private float[][] K;
    
    //[height][width]
    private float[][] data;
    private float[] y;
    private int height,width;
    
	public boolean debug = false;
	
	public float runPerceptron(String filename, int max_iterations, int dimension, float minErr)
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
		computeKernel(dimension);
        
		float[] c = new float[height];
        
        c= classify(max_iterations,minErr);
		
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
                //System.out.println(data[i][j]);
            }
            y[i] = in.nextFloat();
        }
	}
    
	private void computeKernel(int dimension)
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
                
                K[j][i] = (float)Math.pow(1 + dot, dimension);
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
    
    private float sign(float x)
    {
        if(x<0)
            return -1.0f;
        else if(x>0)
            return 1.0f;
        else
            return 0.0f;
    }
    
	private float[] classify(int max_iterations,float minErr)
	{
		int count = 0;
        
        int[] errArray = new int[height];
        for(int i=0; i<height; i++) //initialize error array to all 1's
        {
            errArray[i] = 1;
        }
        
		// the classifier
		float[] c = new float[height];
		Arrays.fill(c, 0);
        
		// loop control
		boolean misclassified = true;
        
        
        float lastErr = 0.0f;
		while(misclassified && (count < max_iterations))
		{
            int errCount=0;
			count++;
			misclassified = false;
			for(int j=0;j<height;j++)
			{
				float sum = 0.0f;
				for(int k=0;k<height;k++)
				{
					sum+= K[k][j]*c[k]*y[j];
				}
				if(sum <= 0)
				{
					c[j] += y[j];
					misclassified = true;
                    errCount++;
				}
                
                /*if(sign(sum) != sign(y[j]))
                {
                    //errArray[j]=0;
                    
                }*/
                
                
			}
            float currErr = (float)errCount/(float)height;
            System.out.println("Iteration: "+count+", Error:"+currErr);
            
            //if(((float)errCount/(float)height) < minErr){}
                //break;
            
            if(currErr <= minErr)
            {
                break;
            }
		}
        if(count >= max_iterations)
            System.out.println("broke early");
		
		return c;
	}
}

