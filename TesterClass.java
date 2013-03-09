

public class TesterClass {
	KernelPerceptron kernelPerceptron = new KernelPerceptron();
	DecisionStumps decisionStumps = new DecisionStumps();
	int MAXDIMENSION = 10;
	int MAXITTERATIONS = 1000;
	float testErrorPrev = 0.0f;
	float epsilon = 0.01f;
	
	
	public void runAllTests()
	{
        
		boolean result = testOne();
		if(!result)
		{
			System.out.println("Something broke :(");
			return;
		}
		
		result = testTwo();
		if(!result)
		{
			System.out.println("Something broke :(");
			return;
		}
	}
	
	private boolean testOne()
	{	
		float testError = 0.0f;
		
		String file1 = "iris.txt";
		//String file1 = in.readLine();
		
		System.out.println("Starting Kernel Perceptron Tests...");
		for(int dimension=2; dimension<MAXDIMENSION; dimension++)
		{
			testError = kernelPerceptron.runPerceptron(file1, MAXITTERATIONS, dimension, 0.02f);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon)
			{
				System.out.println("Stopped at dimension: " + dimension + " with epsilon: " + epsilon + " on data file:" + file1);
				break;
			}
		}
		
		String file2 = "fertility.txt";
		//String file2 = in.readLine();
		
		System.out.println("Starting Kernel Perceptron Tests...");
		for(int dimension=2; dimension<MAXDIMENSION; dimension++)
		{
			testError = kernelPerceptron.runPerceptron(file2, MAXITTERATIONS, dimension, 0.02f);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon)
			{
				System.out.println("Stopped at dimension: " + dimension + " with epsilon: " + epsilon + " on data file:" + file1);
				break;
			}
		}
		
		return true;
	}
	
	private boolean testTwo()
	{
		float testError = 0.0f;
		
		String file1 = "iris.txt";
		//String file1 = in.readLine();
		
		System.out.println("Starting Decision Stumps Tests...");
		for(int itterations=10; itterations<MAXDIMENSION; itterations += 10)
		{
			testError = decisionStumps.runDecisionStumps(file1, itterations);
			if(testError == -1)
			{
				return false;
			}
			if((testErrorPrev-testError) < epsilon)
			{
				System.out.println("Stopped at # itterations: " + itterations + " with epsilon: " + epsilon + " on data file:" + file1);
				break;
			}
		}
		
		String file2 = "fertility.txt";
		//String file2 = in.readLine();
		
		System.out.println("Starting Decision Stumps Tests...");
		for(int itterations=10; itterations<MAXDIMENSION; itterations += 10)
		{
			testError = decisionStumps.runDecisionStumps(file2, itterations);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon)
			{
				System.out.println("Stopped at # itterations: " + itterations + " with epsilon: " + epsilon + " on data file:" + file1);
				break;
			}
		}
		return true;
	}
	
	
	
	public static void main(String[] args)
	{
        FileFormat ff = new FileFormat();
		TesterClass tester = new TesterClass();
		ff.format();
		tester.runAllTests();
        
		return;
	}
}
