import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class BestStump {
	int dim;
	float thresh;
	String ineq;
	float[] classif;
	float weightedError;

	public BestStump(int iIn,float threshValueIn,String inequalIn, float[] bestClassif, float weightedError)
	{
		dim = iIn;
		thresh = threshValueIn;
		ineq = inequalIn;
		classif = bestClassif;
		this.weightedError = weightedError;
	}

	public BestStump()
	{
		dim = 0;
		thresh = 0.0f;
		ineq = "";
		classif = null;
		weightedError = 0;
	}

	public void printValues(){
		System.out.println("Best Stump:");
		System.out.println("Dimention: " + dim + ". ThresholdValue: " + thresh + ". Inequality: " + ineq + ". The weighted error is " + weightedError);
		System.out.println("Best Classifier");
		System.out.print(classif[0]);
		for(int i=1; i<classif.length; i++)
		{
			System.out.print(", " + classif[i]);
		}
		System.out.println("");
	}
}

public class DecisionStumps {

	float[][] trainingSetXY;
	int[] classLabels;
	float[] weights;
	int trainingWidth;
	int trainingHeight;

	public float runDecisionStumps(String dataFilename, int numIterations)
	{
		// read in the samples
		File infile = new File(dataFilename);
		Scanner inData;
		try {
			inData = new Scanner(infile);
			parseDataInput(inData);
		} catch (FileNotFoundException e) {
			System.out.println("error reading file");
			e.printStackTrace();
			return -1;
		}

		initWeights();

		BestStump stump = build(numIterations);
		stump.printValues();
		
		return stump.weightedError;
	}

	private BestStump build(int numIterations)
	{
		int n = trainingWidth;
		int m = trainingHeight;
		float minErr = Float.POSITIVE_INFINITY;
		float numStep = 10.0f;
		float[] bestClassEst = new float[m];
		BestStump bestStump = null;
		int bestDimention = 0;
		float bestThresh = 0;
		String bestIneq = null;
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
					//System.out.println("dim: " + i + ". thresh: " + threshValue + ". ineqal: " + inequal + ". The weighted error is " + weightedErr);
					if(weightedErr<minErr)
					{
						minErr = weightedErr;
						bestClassEst = (float[])predictedValues.clone();
						bestDimention = i;
						bestThresh = threshValue;
						bestIneq = inequal;
						
						bestStump = new BestStump(bestDimention,bestThresh,bestIneq, bestClassEst, minErr);
					}
				}
			}
		}
		
		return bestStump;
	}

	private float[] stumpClassify(int dimension,float threshVal, String inequal)
	{
		float[] retArrary = new float[trainingHeight];
		//Init with all ones
		for(int i=0;i<trainingHeight;i++)
		{
			retArrary[i] = 1.0f;
		}

		if(inequal == "lt")
		{
			for(int i=0;i<trainingHeight;i++)
			{
				if(trainingSetXY[i][dimension] <= threshVal)
				{
					retArrary[i] = -1.0f;
				}
			}
		}
		else
		{
			for(int i=0;i<trainingHeight;i++)
			{
				if(trainingSetXY[i][dimension] > threshVal)
				{
					retArrary[i] = -1.0f;
				}
			}
		}
		return retArrary;
	}

	private void parseDataInput(Scanner in)
	{
		int i = 0;
		trainingWidth = in.nextInt();
		int classifierRowSize = in.nextInt();
		trainingHeight = in.nextInt();
		trainingSetXY = new float[trainingHeight][trainingWidth];
		classLabels = new int[trainingHeight];

		while(in.hasNext()){
			for(int j=0; j<trainingWidth; j++)
			{
				trainingSetXY[i][j] = in.nextFloat();
			}
			classLabels[i] = in.nextInt();
			for(int k=1; k<classifierRowSize; k++)
			{
				in.nextInt();
			}
			
			i++;
		}

		return;
	}

	private void initWeights()
	{
		weights = new float[trainingHeight];
		for(int i=0; i<trainingHeight; i++)
		{
			weights[i] = (float) (1.0/trainingHeight);
		}
	}
}
