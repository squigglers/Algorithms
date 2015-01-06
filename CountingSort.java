import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CountingSort {

	public static void main(String[] args)
	{
		//read numbers from unsorted file and store them into array A
		int[] A = new int[(int)Math.pow(2, 20)];
		String inputFile = args[0];
		int size = readNumsFromFile(A, inputFile);	//real size of array
		
		//read numbers from index file and store them into array indices
		int[] index = new int[1024];				//stores the indices from file
		String indexFile = args[1];
		readNumsFromFile(index, indexFile);
		
		//start timer
		long startTime = System.nanoTime();
		
		//sort array with CountingSort and store into B!
		int[] B = new int[size];
		int k = (int)Math.pow(2, 16);				//stores max value of an element in A
		CountSort(A, B, k, size);
		
		//timing for finding the 1st, 100th, and last element based on the index location
		int[] nthElement = {1, 100, index.length};	//stores which elements to find		
		long duration;
		for(int nth: nthElement)
		{
			duration = System.nanoTime() - startTime;
			System.out.println(B[index[nth-1]] + ", Time: " + duration);
		}
		
		//output integers into output file
		String outputFile = args[2];
		writeIndexNumsToFile(B, index, outputFile);
	}
	
	//CountSort sorts the array!
	public static void CountSort(int[] A, int[] B, int k, int size)
	{
		//initialize each C[i] with 0
		int[] C = new int[k+1];
		for(int i = 0; i <= k; i++)
			C[i] = 0;
		
		//make each C[i] contain the number of elements equal to i
		for(int j = 0; j < size; j++)
			C[A[j]] = C[A[j]] + 1;
		
		//make each C[i] contain the number of elements less than or equal to i
		for(int i = 1; i <= k; i++)
			C[i] = C[i] + C[i-1];
		
		//B is the sorted array
		for(int j = size-1; j >= 0; j--)
		{
			B[C[A[j]]-1] = A[j];
			C[A[j]] = C[A[j]] - 1;
		}
	}
	
	//reads ints from filename and stores them in array A
	public static int readNumsFromFile(int[] A, String filename)
	{
		int i = 0;
		File file = new File(filename);
		try
		{
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextInt())
			{
				A[i] = scanner.nextInt();
				i++;
			}
			if(scanner != null)
				scanner.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return i;
	}

	//write B[index] to output file
	public static void writeIndexNumsToFile(int[] B, int[] indices, String filename)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for(int index: indices)
				bw.write(B[index] + "\n");
			if(bw != null)
				bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}

