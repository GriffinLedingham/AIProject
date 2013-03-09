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
	
	public float runPerceptron(String filename, int max_iterations, int dimension, float minErr)
	{
		float testError;
		
		try {
			debugOut = new PrintWriter(new FileWriter("debug.txt"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
                //System.out.println(data[i][j]);
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
				
				//if (miny > y[j]) miny = y[j];
				//if (maxy < y[j]) maxy = y[j];
			}
			
		}
		System.out.print("Max X Un-normalized Value = " );
		printArray(max);
		
		System.out.print("Min X Un-normalized Value = ");
		printArray(min);
		
		System.out.println("Max Y Un-normalized Value = " + maxy);
		System.out.println("Min Y Un-normalized Value = " + miny);
		
		// normalize the data
		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < width; j++)
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
    
	private void computeKernel(int dimension)
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
                
                K[j][i] = (float)Math.pow(1 + dot, dimension);
            }
        }
        
       debugOut.println("Computed Kernel");
       print2DArrayFile(K);
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
                
				System.out.println("Itteration:" + count + " Sum:" + sum + " Missclassified:" + misclassified);
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
	
	private void print2DArray(float[][] data)
	{
		for(int i=0; i<data.length; i++)
		{
			for(int j=0; j<data[i].length; j++)
			{
				System.out.print(data[i][j] + " ");
			}
			System.out.println("");
		}
	}
	
	private void print2DArrayFile(float[][] data)
	{
		for(int i=0; i<data.length; i++)
		{
			for(int j=0; j<data[i].length; j++)
			{
				debugOut.print(data[i][j] + " ");
			}
			debugOut.println("");
		}
	}
	
	private void printArray(float[] data)
	{
		for(int i=0; i<data.length; i++)
		{
			System.out.print(data[i] + " ");	
		}
		System.out.println("");
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

