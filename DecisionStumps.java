import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


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
	
	private void build(int numIterations) {
		for(int i=0; i<trainingSetSize; i++)
		{
			
		}
		
	}
    
    private float[] stumpClassify(float dimension,float threshVal, String inequal)
    {
        float[][] retArrary = new float[trainingSetXY[0].length][1];
        if(inequal == "lt")
        {
            for(int i=0;i<trainingSetXY.length;i++)
            {
                if(trainingSetXY[i][dimension] <= threshVal)
                {
                    retArrary[i] = -1.0;
                }
            }
        }
        else
        {
            for(int i=0;i<trainingSetXY.length;i++)
            {
                if(trainingSetXY[i][dimension] > threshVal)
                {
                    retArrary[i] = 1.0;
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
