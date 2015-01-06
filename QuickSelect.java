import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;*/

public class QuickSelect
{
	public static void main(String[] args)
	{
		//read numbers from unsorted file and store them into array A
		int[] A = new int[(int)Math.pow(2, 20)];
		String inputFile = args[0];
		int size = readNumsFromFile(A, inputFile);
		
		//read numbers from index file and store them into array indices
		int[] index = new int[1024];
		String indexFile = args[1];
		readNumsFromFile(index, indexFile);
		
		//start timer
		long startTime = System.nanoTime();
				
		//find index k in array A by using QuickSelect
		int k = Integer.parseInt(args[2]);
		for(int i = 0; i < k; i++)
		{
			Select(A, 0, size-1, index[i]);
			//System.out.println(A[index[i]]);
		}
		
		//end timer
		long duration = System.nanoTime() - startTime;
		System.out.println("Time: " + duration);
		
		/*//output integers into output file
		String outputFile = args[2];
		writeIndexNumsToFile(A, indices, outputFile);*/
	}
	
	public static void Select(int[] A, int left, int right, int n)
	{
		if(left != right)
		{
			int pivotIndex = (int) (left + Math.floor(Math.random() * (right - left + 1)));
			pivotIndex = Partition(A, left, right, pivotIndex);
			if(n < pivotIndex)
				Select(A, left, pivotIndex - 1, n);
			if(n > pivotIndex)
				Select(A, pivotIndex + 1, right, n);
		}
	}
	
	public static int Partition(int[] A, int left, int right, int pivotIndex)
	{
		int pivotValue = A[pivotIndex];
		Swap(A, pivotIndex, right);
		int storeIndex = left;
		for(int i = left; i <= right-1; i++)
		{
			if(A[i] < pivotValue)
			{
				Swap(A, storeIndex, i);
				storeIndex++;
			}
		}
		Swap(A, right, storeIndex);
		return storeIndex;
	}

	//swaps two elements in an array
	public static void Swap(int[] A, int i, int j)
	{
		int tmp = A[i];
		A[i] = A[j];
		A[j] = tmp;
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
/*
	//write A[index] to output file
	public static void writeIndexNumsToFile(int[] A, int[] indices, String filename)
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for(int index: indices)
				bw.write(A[index] + "\n");
			if(bw != null)
				bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
*/
}