import java.io.*;
import java.util.*;

public class KernelPerceptron {
	//private int[] x;
	//private int[] y;
	//private float[][] K;
	//private float[] K;
    
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
		//computeKernel();
        
		float[] c = new float[height];
		printClassification(c);
        
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
		//num_samples = in.nextInt();
        width = in.nextInt();
        //System.out.println(width);
        height = in.nextInt();
        //System.out.println(height);
        data = new float[height][width];
        y = new float[height];
		//x = new int[num_samples];
		//y = new int[num_samples];
        
		//if(debug) System.out.println("num samples: " + num_samples);
        for(int i = 0;i<height;i++)
        {
            for(int j = 0;j<width;j++)
            {
                data[i][j] = in.nextFloat();
                System.out.println(data[i][j]);
            }
            y[i] = in.nextFloat();
            
        }
		/*for(int i = 0; i < num_samples; i++)
         {
         x[i] = in.nextInt();
         y[i] = in.nextInt();
         }*/
	}
    
	/*private void computeKernel()
     {
     //if(debug) System.out.println("Kernel Matrix");
     //K = new float[height][width];
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
     
     K[j][i] = (float)Math.pow(1 + dot, 3);
     //if(debug) System.out.print(K[i][j] + " ");
     }
     //if(debug) System.out.println();
     }
     
     }*/
	
	private float sign(float x)
	{
		if(x<0.0f)
			return -1.0f;
		else if(x>0.0f)
			return 1.0f;
		else
			return 0.0f;
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
		//int j, k;
		//int sum;
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
					float dot = 0.0f;
	                
					for(int g=0;g<width;g++)
	                {
	                    dot += data[j][g]*data[k][g];
	                }
					
	                sum += (float)Math.pow(1 + dot, 3) * c[k];
					//sum+= K[k][j]*c[k];
                    
				}
				if(sum*y[j] <= 0)
				{
					c[j] += y[j];
					misclassified = true;
				}
			}
			/*for(j=0; j < height; j++)
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
             }*/
            
		}
        
		//System.out.println();
		
		return c;
	}
}

