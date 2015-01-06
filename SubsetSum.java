//Katherine Chen subset sum problem

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class SubsetSum {

	public static void main(String[] args) {
		String filename = "subset.txt";		//input filename
		try {
			Scanner sc = new Scanner(new FileReader(filename));
			int numCases = sc.nextInt();	//read number of cases
			
			int targetSum;
			int[] listOfInts;
			for(int i = 0; i < numCases; i++)
			{
				//target sum
				targetSum = sc.nextInt();
				sc.nextLine();	//go to end of line
				
				//turn the list of integers line from a string into an array listOfInts
				String line = sc.nextLine();
				String[] arrayLine = line.split(" ");
				listOfInts = new int[arrayLine.length];
				for(int j = 0; j < arrayLine.length; j++)
					listOfInts[j] = Integer.parseInt(arrayLine[j]);
				
				//run the algorithm
				boolean hasTarget = subsetSum(listOfInts, targetSum);
				
				//print True or False
				if(hasTarget)
					System.out.println("True");
				else
					System.out.println("False");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static boolean subsetSum(int[] A, int S)
	{
		if(S == 0)	//if target sum is 0
			return true;
		if(A.length == 0 && S != 0)	//if array A is empty
			return false;
		
		//initialize T and T2 arrays with false
		boolean[] T = new boolean[S+1];
		boolean[] T2 = new boolean[S+1];
		for(int i = 0; i < S; i++)
		{
			T[i] = false;
			T2[i] = false;
		}
		T[0] = true;
		
		//subset algorithm
		for(int i = 0; i < A.length; i++)
		{
			for(int j = 0; j <= S-A[i]; j++)
			{
				//keep track of subsets that are less than or equal to S
				if(T[j] == true && j+A[i] <= S)
				{
					T2[j+A[i]] = true;
					if(j+A[i] == S)	//found a subset that equals S
						return true;
				}
			}
			
			//max(T[k], T2[k])
			for(int k = 0; k < T.length; k++)
			{
				if(T[k] == true || T2[k] == true)
					T[k] = true;
				else
					T[k] = false;
				
				T2[k] = false;
			}
		}
		
		return false;	//no subset equalling S :(
	}

}
