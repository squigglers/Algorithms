//Katherine Chen
//Johnson's algorithm
//note that it reads input from "graph.txt"

import java.io.*;
import java.util.*;

public class JohnsonsAlgorithm {

	public static void main(String[] args) throws FileNotFoundException {
		
		//input from file "graph.txt"
		String filename = "graph.txt";
		Scanner in = new Scanner(new FileReader(filename));
		
		int numCases = in.nextInt();
		
		for(int c = 0; c < numCases; c++)
		{
			//get graph from file
			int numVertices = in.nextInt();
			String graph[][] = new String[numVertices+1][numVertices+1];
			for(int u = 0; u < numVertices; u++)
			{
				for(int v = 0; v < numVertices; v++)
					graph[u][v] = in.next();
			}
			
			//add vertex s where w(s,v) = 0 and w(v,s) = *
			for(int s = 0; s < numVertices+1; s++)
			{
				graph[s][numVertices] = "*";
				graph[numVertices][s] = "0";
			}
			
			//run Bellman Ford algorithm
			Vertex vertex[] = new Vertex[numVertices+1];
			boolean noNegWeightCycle = BellmanFord(graph, vertex, numVertices);
			if(!noNegWeightCycle)
				System.out.println("The input graph contains a negative-weight cycle");
			
			else
			{
				//calculate new weight for each edge
				for(int u = 0; u < numVertices; u++)
				{
					for(int v = 0; v < numVertices; v++)
					{ 
						if(!graph[u][v].equals("*"))
						{
							int weight = Integer.parseInt(graph[u][v]);
							int newWeight = weight + vertex[u].distance - vertex[v].distance;
							graph[u][v] = String.valueOf(newWeight);
						}
					}
				}
				
				//create matrix to store shortest paths
				int D[][] = new int[numVertices][numVertices];
				
				//run Dijkstra for each vertex
				Vertex vertexx[] = new Vertex[numVertices];
				for(int u = 0; u < numVertices; u++)
				{
					Dijkstra(graph, vertexx, u, numVertices);
					for(int v = 0; v < numVertices; v++)
						D[u][v] = vertexx[v].distance + vertex[v].distance - vertex[u].distance;
				}
				
				//print matrix
				printMatrix(D, numVertices);
			}
			
			System.out.println();
		}
		in.close();
	}
	
	public static boolean BellmanFord(String graph[][], Vertex vertex[], int numVertices)
	{
		//initialize single source
		initializeSingleSource(vertex, numVertices);
	
		//go through V-1 passes over the edges of the graph
		for(int i = 1; i <= numVertices+1-1; i++)
		{
			//relax edges
			for(int u = 0; u < numVertices+1; u++)
			{
				for(int v = 0; v < numVertices+1; v++)
				{
					if(!graph[u][v].equals("*"))
						relax(vertex, graph, u, v);
				}
			}
		}
		
		//check for negative cycles
		for(int u = 0; u < numVertices+1; u++)
		{
			for(int v = 0; v < numVertices+1; v++)
			{
				if(!graph[u][v].equals("*"))
				{
					int weight = Integer.parseInt(graph[u][v]);
					if(vertex[v].distance > vertex[u].distance + weight)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public static void Dijkstra(String graph[][], Vertex vertex[], int source, int numVertices)
	{
		//initialize single source
		initializeSingleSource(vertex, source);
		
		//S is empty
		ArrayList<Vertex> S = new ArrayList<Vertex>(numVertices);
		
		//Q = G.V as a min-priority queue
		LinkedList<Vertex> Q = new LinkedList<Vertex>();
		for(Vertex v: vertex)
			Q.add(v);
		
		while(!Q.isEmpty())
		{
			Collections.sort(Q);
			Vertex u = Q.poll();
			S.add(u);
			
			for(int v = 0; v < numVertices; v++)
			{
				if(!graph[u.index][v].equals("*"))
					relax(vertex, graph, u.index, v);
			}
		}
	}
	
	public static void initializeSingleSource(Vertex vertex[], int source)
	{
		for(int i = 0; i < vertex.length; i++)
			vertex[i] = new Vertex(i);
		vertex[source].distance = 0;
	}
	
	public static void relax(Vertex vertex[], String graph[][], int u, int v)
	{
		int weight = Integer.parseInt(graph[u][v]);
		if(vertex[u].distance != Integer.MAX_VALUE && vertex[v].distance > vertex[u].distance + weight)
		{
			vertex[v].distance = vertex[u].distance + weight;
			vertex[v].predecessor = vertex[u].index;
		}
	}

	public static void printMatrix(int graph[][], int numVertices)
	{
		for(int i = 0; i < numVertices; i++)
		{
			for(int j = 0; j < numVertices; j++)
			{
				if(graph[i][j] == Integer.MAX_VALUE)
					System.out.print("* ");
				else
					System.out.print(graph[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	//vertex class
	public static class Vertex implements Comparable<Vertex>
	{
		public int index;
		public int distance; 
		public int predecessor;
		
		public Vertex(int index)
		{
			this.index = index;
			distance = Integer.MAX_VALUE;	//distance as "infinity"
			predecessor = -1;				//predecessor as "NIL"
		}
		
		@Override
		public int compareTo(Vertex other)
		{
			if(this.distance < other.distance)
				return -1;
			else if(this.distance == other.distance)
				return 0;
			else
				return 1;
		}
	}
}
