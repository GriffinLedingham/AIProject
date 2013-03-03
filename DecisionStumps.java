import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class bestStump {
    int dim;
    float thresh;
    String ineq;
    
    public bestStump(int iIn,float threshValueIn,String inequalIn)
    {
        dim = iIn;
        thresh = threshValueIn;
        ineq = inequalIn;
    }
    
    public bestStump()
    {
        dim = 0;
        thresh = 0.0f;
        ineq = "";
    }
}

public class DecisionStumps {
	
	float[][] trainingSetXY;
	int[] classLabels;
	float[] weights;
	int trainingSetRowSize;
	int trainingSetColSize;
	
	public void runDecisionStumps(String filename, int numIterations)
	{
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
		
		initWeights();
		
		build(numIterations);
	}
	
	private bestStump build(int numIterations)
    {
		int m = trainingSetRowSize;
        int n = trainingSetColSize;
        float minErr = Float.POSITIVE_INFINITY;
        float numStep = 10.0f;
        float[] bestClassEst = new float[m];
        bestStump ret = new bestStump();
        for(int i=0; i<m; i++)
        {
            bestClassEst[i]=0.0f;
        }
        for(int i=0; i<n; i++) //iterate over each attribute x, y, etc..
        {
            float min = trainingSetXY[0][i];
            float max = trainingSetXY[0][i];
            for(int j=0;j<m;j++)
            {
                if(trainingSetXY[i][j] < min) //find min value
                    min = trainingSetXY[i][j];
                if(trainingSetXY[i][j] > max) //find max value
                    max = trainingSetXY[i][j];
            }
			float stepSize = (max-min)/numStep; //calculate step size
            for(int j=-1;j<(int)numStep+1;j++)
            {
                for(int k=0;k<2;k++) //0 = "lt", 1 = "gt"
                {
                    String inequal;
                    if(k==0){inequal = "lt";}
                    else{inequal = "gt";}
                    
                    float threshValue = min + (float)j * stepSize; //calculate threshold v
                    float[] predictedValues = stumpClassify(i,threshValue,inequal); //predict labels
                    int[] errArray = new int[m];
                    for(int l=0; l<m; l++) //initialize error array to all 1's
                    {
                        errArray[l] = 1;
                    }
                    for(int l=0; l<m; l++)
                    {
                        if(predictedValues[l] == classLabels[l]) //if predicted labels differs from real, flag a 1 in the error array
                        {
                            errArray[l]=0;
                        }
                    }
                    float weightedErr = 0.0f;
                    for(int l=0;l<errArray.length;l++)
                    {
                        weightedErr += weights[l] * errArray[l];
                    }
                    System.out.println("dim: " + i + ". thresh: " + threshValue + ". ineqal: " + inequal + ". The weighted error is " + weightedErr);
                    if(weightedErr<minErr)
                    {
                        minErr = weightedErr;
                        
                        bestClassEst = (float[])predictedValues.clone();
                        ret = new bestStump(i,threshValue,inequal);
                    }
                    
                    /*
                     bestStump['dim'] = i
                     bestStump['thresh'] = threshVal
                     bestStump['ineq'] = inequal*/
                    
                }
            }
		}
        return ret;
        //return bestStump, bestClasEst, minErr; The fuck. I think we'll have trouble returning 3 values.
		
	}
    
    private float[] stumpClassify(int dimension,float threshVal, String inequal)
    {
        float[] retArrary = new float[trainingSetRowSize];
        //Init with all ones
        for(int i=0;i<trainingSetRowSize;i++)
        {
        	retArrary[i] = 1.0f;
        }
        
        if(inequal == "lt")
        {
            for(int i=0;i<trainingSetRowSize;i++)
            {
                if(trainingSetXY[dimension][i] <= threshVal)
                {
                    retArrary[i] = -1.0f;
                }
            }
        }
        else
        {
            for(int i=0;i<trainingSetRowSize;i++)
            {
                if(trainingSetXY[dimension][i] > threshVal)
                {
                    retArrary[i] = 1.0f;
                }
            }
        }
        return retArrary;
    }
    
	private void parseInput(Scanner in)
	{
		int i = 0;
		trainingSetRowSize = in.nextInt();
		trainingSetColSize = 2;
		trainingSetXY = new float[2][trainingSetRowSize];
		classLabels = new int[trainingSetRowSize];
		
		while(in.hasNext()){
			trainingSetXY[0][i] = in.nextFloat();
			trainingSetXY[1][i]	= in.nextFloat();
			classLabels[i] = in.nextInt();
			
			i++;
		}
		
		return;
	}
	
	private void initWeights()
	{
		weights = new float[trainingSetRowSize];
		for(int i=0; i<trainingSetRowSize; i++)
		{
			weights[i] = (float) (1.0/trainingSetRowSize);
		}
	}
}
