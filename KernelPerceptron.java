import java.lang.Math.*;

public class KernelPerceptron {
	
	public static void main(String[] args)
	{
		int NUM_SAMPLES = 10;
		int d = 3;
		
		int[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] y = {-1, -1, -1, -1, 1, 1, 1, 1, 1, 1};
		int[] c = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[][] K = new int[NUM_SAMPLES][NUM_SAMPLES];
		
		int i, j, k;
		int sum;
		int count = 0;
		
		boolean misclassified = true;
		
		System.out.println("Kernel Matrix");
		// calculate kernel matrix
		for(i = 0; i < NUM_SAMPLES; i++)
		{
			for(j = 0; j < NUM_SAMPLES; j++)
			{
				K[i][j] = (int) Math.pow(1 + x[i]*y[j], d);
				System.out.print(K[i][j] + " ");
			}
			System.out.println();
		}
		
		while(misclassified && (count < 1000))
		{
			count++;
			misclassified = false;
			for(j=0; j < NUM_SAMPLES; j++)
			{
				sum = 0;
				for(k = 0; k < NUM_SAMPLES; k++)
				{
					sum += c[k]*K[k][j];	
				}
				sum *= y[j];
				
				if(sum <= 0)
				{
					c[j] += y[j];
					misclassified = true;
				}
			}
			
		}
		
		System.out.println();
		if(misclassified) System.out.println("did not converge after 1000 iterations");
		System.out.print("c = ");
		for(i = 0; i < NUM_SAMPLES; i++)
		{
			System.out.print(c[i] + " ");
		}
		System.out.println();
	}

	
}
