
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
		//testError = kernelPerceptron.runPerceptron("in.txt", 10, 2);
		decisionStumps.runDecisionStumps("ds.txt", 10);
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
