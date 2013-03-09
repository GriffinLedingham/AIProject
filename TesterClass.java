
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
		
		String file1 = "testData1.txt";
		//String file1 = in.readLine();
		
		System.out.println("////////////Starting Kernel Perceptron 1 Test...////////////");
		for(int dimension=2; dimension<MAXDIMENSION; dimension++)
		{
			testError = kernelPerceptron.runPerceptron(file1, MAXITTERATIONS, dimension, 0.02f);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon  && dimension != 2)
			{
				System.out.println("Stopped at dimension: " + dimension + " with epsilon: " + epsilon + " with error:" + testError + " on data file:" + file1);
				System.out.println("");
				break;
			}
			
			System.out.println("At dimension: " + dimension + " with epsilon: " + epsilon + " With Error" + testError);
			
			testErrorPrev = testError;
		}
		
		String file2 = "testData2.txt";
		//String file2 = in.readLine();
		
		System.out.println("////////////Starting Kernel Perceptron 2 Test...////////////");
		for(int dimension=2; dimension<MAXDIMENSION; dimension++)
		{
			testError = kernelPerceptron.runPerceptron(file2, MAXITTERATIONS, dimension, 0.02f);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon  && dimension != 2)
			{
				System.out.println("Stopped at dimension: " + dimension + " with epsilon: " + epsilon + " with error:" + testError + " on data file:" + file2);
				System.out.println("");
				break;
			}
			
			System.out.println("At dimension: " + dimension + " with epsilon: " + epsilon + " With Error" + testError);
			
			testErrorPrev = testError;
		}
		
		return true;
	}
	
	private boolean testTwo()
	{
		float testError = 0.0f;
		
		String file1 = "testData1.txt";
		//String file1 = in.readLine();
		
		System.out.println("////////////Starting Decision Stumps 1 Test...////////////");
		for(int itterations=10; itterations<MAXITTERATIONS; itterations += 10)
		{
			testError = decisionStumps.runDecisionStumps(file1, itterations);
			if(testError == -1)
			{
				return false;
			}
			if((testErrorPrev-testError) < epsilon && itterations != 10)
			{
				System.out.println("Stopped at # itterations: " + itterations + " with epsilon: " + epsilon + " with error:" + testError +  " on data file:" + file1);
				System.out.println("");
				break;
			}
			System.out.println("At itteration: " + itterations + " with epsilon: " + epsilon + " With Error" + testError);
			
			testErrorPrev = testError;
		}
		
		String file2 = "testData2.txt";
		//String file2 = in.readLine();
		
		System.out.println("////////////Starting Decision Stump 2 Tests...////////////");
		for(int itterations=10; itterations<MAXITTERATIONS; itterations += 10)
		{
			testError = decisionStumps.runDecisionStumps(file2, itterations);
			if(testError == -1)
			{
				return false;
			}
			
			if((testErrorPrev-testError) < epsilon && itterations != 10)
			{
				System.out.println("Stopped at # itterations: " + itterations + " with epsilon: " + epsilon + "with error:" + testError +  " on data file:" + file2);
				System.out.println("");
				break;
			}
			
			System.out.println("At itteration: " + itterations + " with epsilon: " + epsilon + " With Error" + testError);
			
			testErrorPrev = testError;
		}
		return true;
	}
	
	
	
	public static void main(String[] args)
	{
		TesterClass tester = new TesterClass();
		
		tester.runAllTests();
        
		return;
	}
}
