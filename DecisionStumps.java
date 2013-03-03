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
	int trainingSetSize;
	
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
		int m = trainingSetSize;
        int n = trainingSetXY[0].length;
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
            for(int j=0;j<trainingSetXY.length;j++)
            {
                if(trainingSetXY[j][i] < min) //find min value
                    min = trainingSetXY[j][i];
                if(trainingSetXY[j][i] > max) //find max value
                    max = trainingSetXY[j][i];
            }
			float stepSize = (max-min)/numStep; //calculate step size
            for(int j=-1;j<(int)numStep+1;j++)
            {
                for(int k=0;k<2;k++) //0 = "lt", 1 = "gt"
                {
                    String inequal;
                    if(k==0){inequal = "lt";}
                    else{inequal = "gt";}
                    
                    float threshValue = min+j * stepSize; //calculate threshold v
                    float[] predictedValues = stumpClassify(i,threshValue,inequal); //predict labels
                    int[] errArray = new int[m];
                    for(int l=0;l<m;l++) //initialize error array to all 1's
                    {
                        errArray[l] = 1;
                    }
                    for(int l=0;l<predictedValues.length;l++)
                    {
                        if(predictedValues[l] == classLabels[l]) //if predicted labels differs from real, flag a 1 in the error array
                        {
                            errArray[l]=0;
                        }
                    }
                    float weightedErr = 0.0f;
                    for(int l=0;l<errArray.length;l++)
                    {
                        weightedErr += errArray[l] * weights[l];
                    }
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
                    System.out.println("dim: " + i + ". thresh: " + threshValue + ". ineqal: " + inequal + ". The weighted error is " + weightedErr);
                }
            }
		}
        return ret;
        //return bestStump, bestClasEst, minErr; The fuck. I think we'll have trouble returning 3 values.
		
	}
    
    private float[] stumpClassify(int dimension,float threshVal, String inequal)
    {
        float[] retArrary = new float[trainingSetXY.length];
        if(inequal == "lt")
        {
            for(int i=0;i<trainingSetXY.length;i++)
            {
                if(trainingSetXY[i][dimension] <= threshVal)
                {
                    retArrary[i] = -1.0f;
                }
            }
        }
        else
        {
            for(int i=0;i<trainingSetXY.length;i++)
            {
                if(trainingSetXY[i][dimension] > threshVal)
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
		trainingSetSize = in.nextInt();
		trainingSetXY = new float[trainingSetSize][2];
		classLabels = new int[trainingSetSize];
		
		while(in.hasNext()){
			trainingSetXY[i][0] = in.nextFloat();
			trainingSetXY[i][1]	= in.nextFloat();
			classLabels[i] = in.nextInt();
			
			i++;
		}
		
		return;
	}
	
	private void initWeights()
	{
		weights = new float[trainingSetSize];
		for(int i=0; i<trainingSetSize; i++)
		{
			weights[i] = (float) (1.0/trainingSetSize);
		}
	}
}
