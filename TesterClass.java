
public class TesterClass {
	
	KernelPerceptron kernelPerceptron = new KernelPerceptron();
	DecisionStumps decisionStumps = new DecisionStumps();
	
	
	public void runAllTests()
	{
		testOne();
		testTwo();
	}
	
	private void testOne()
	{
		float testError;
		testError = kernelPerceptron.runPerceptron("iris.txt", 1000, 2, 0.02f);
		//decisionStumps.runDecisionStumps("data.txt", "class.txt", 10);
		//TODO
	}
	
	private void testTwo()
	{
		//TODO
	}
	
	
	
	public static void main(String[] args)
	{
		TesterClass tester = new TesterClass();
		
		tester.runAllTests();
        
		return;
	}
}
