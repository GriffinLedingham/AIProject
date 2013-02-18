

public class KernelPerceptron {
	
	public static void main(String[] args)
	{
		int NUM_SAMPLES = 10;
		int d = 3;
		int[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] y = {-1, -1, -1, -1, 1, 1, 1, 1, 1, 1};
		int[][] K = new int[NUM_SAMPLES][NUM_SAMPLES];
		
		// calculate kernel matrix
		for(int i = 0; i < NUM_SAMPLES; i++)
		{
			for(int j = 0; j < NUM_SAMPLES; j++)
			{
				K[i][j] = (1 + x[i]*y[i])^d;
			}
		}
	}

	
}
