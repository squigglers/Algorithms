//Katherine Chen - HW3 closest pairs

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ClosestPairOfPoints {

	public static void main(String[] args) {

		//get points from file
		String inputFile = "points.txt";
		Scanner in;
		try {
			in = new Scanner(new FileReader(inputFile));
			int numCases = in.nextInt();	//total number of cases
			
			for (int i = 0; i < numCases; i++) {
				int numCoordinates = in.nextInt();	//total number of points
				
				//store points into P array
				ArrayList<Point> P = new ArrayList<Point>();
				for (int j = 0; j < numCoordinates; j++) {
					double x = in.nextDouble();
					double y = in.nextDouble();
					P.add(new Point(x, y));
				}
				
				// sort by X value and rename P to X for fun!
				ArrayList<Point> X = P;
				Collections.sort(X, sortByX);

				// get points sorted by Y value by copying from x
				ArrayList<Point> Y = new ArrayList<Point>();
				for (Point p : X) {
					Point tmp = new Point(p.x, p.y);
					Y.add(tmp);
				}
				Collections.sort(Y, sortByY);

				// find closest pair
				ArrayList<Point> closestPair = findClosestPair(X, Y);

				// if only one point then error message
				if (closestPair == null)
					System.out.println("Not enough points");

				// print out closest pair
				else
					System.out.println(closestPair.get(0) + ", "
							+ closestPair.get(1));
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// find the closest pair of points
	public static ArrayList<Point> findClosestPair(ArrayList<Point> X,
			ArrayList<Point> Y) {
		// return null if there's only one point
		if (X.size() <= 1)
			return null;

		// return closest pair of points by brute forcing if 3 or less points
		// left
		if (X.size() <= 3)
			return bruteForceFindClosestPair(X);

		// find the dividing line to divide the arrays
		int dividingIndex = (int) Math.ceil((double) X.size() / 2);
		double dividingX = X.get(dividingIndex).x;

		// divide X into left and right arrays
		ArrayList<Point> Xleft = new ArrayList<Point>(dividingIndex);
		ArrayList<Point> Xright = new ArrayList<Point>(X.size() - dividingIndex);
		int i = 0;
		for (Point p : X) {
			if (i++ < dividingIndex)
				Xleft.add(p);
			else
				Xright.add(p);
		}

		// divide Y into left and right
		ArrayList<Point> Yleft = new ArrayList<Point>(Xleft.size());
		ArrayList<Point> Yright = new ArrayList<Point>(Xright.size());
		for (Point p : Y) {
			if (Xleft.contains(p))
				Yleft.add(p);
			else
				Yright.add(p);
		}

		// recursively call findClosestPair with the left arrays and the right
		// arrays
		ArrayList<Point> left = findClosestPair(Xleft, Yleft);
		double leftDistance = getDistance(left);
		ArrayList<Point> right = findClosestPair(Xright, Yright);
		double rightDistance = getDistance(right);

		// find closestPair out of the left and right arrays
		ArrayList<Point> closestPair;
		double minDistance;
		if (leftDistance <= rightDistance) {
			closestPair = left;
			minDistance = leftDistance;
		} else {
			closestPair = right;
			minDistance = rightDistance;
		}

		// add the points that are located within minDistance of dividingX to
		// strip array
		ArrayList<Point> strip = new ArrayList<Point>();
		double startX = dividingX - minDistance;
		double endX = dividingX + minDistance;
		for (Point p : Y) {
			if (p.x >= startX && p.x <= endX)
				strip.add(p);
		}

		// find the closest pair in the strip
		for (i = 0; i < strip.size() - 1; i++) {
			for (int j = i + 1, k = 0; j < strip.size() && k < 7; j++, k++) {
				double distance = getDistance(strip.get(i), strip.get(j));
				if (distance < minDistance) {
					ArrayList<Point> tmp = new ArrayList<Point>(2);
					tmp.add(strip.get(i));
					tmp.add(strip.get(j));

					closestPair = tmp;
					minDistance = distance;
				}
			}
		}

		return closestPair;
	}

	// brute force way to find and return closest pair of points
	public static ArrayList<Point> bruteForceFindClosestPair(ArrayList<Point> P) {
		// variables keep track of closest pairs
		Point a = P.get(0);
		Point b = P.get(1);
		double minDistance = getDistance(a, b);

		// go through all the combinations of the points in P
		for (int i = 0; i < P.size() - 1; i++) {
			for (int j = i + 1; j < P.size(); j++) {
				// get the distance between the pair of points
				double distance = getDistance(P.get(i), P.get(j));

				// store the new closest pair of points
				if (distance < minDistance) {
					minDistance = distance;
					a = P.get(i);
					b = P.get(j);
				}
			}
		}

		ArrayList<Point> closestPair = new ArrayList<Point>(2);
		closestPair.add(a);
		closestPair.add(b);

		return closestPair;
	}

	// returns distance between pairs of points: (x1-x2)^2 + (y1-y2)^2
	public static double getDistance(Point a, Point b) {
		return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
	}

	// returns distance between points in an arraylist
	public static double getDistance(ArrayList<Point> p) {
		return getDistance(p.get(0), p.get(1));
	}

	// sorts points by X value
	static Comparator<Point> sortByX = new Comparator<Point>() {
		@Override
		public int compare(Point a, Point b) {
			if (a.x < b.x)
				return -1;
			else if (a.x == b.x)
				return 0;
			else
				return 1;
		}
	};

	// sorts points by Y value
	static Comparator<Point> sortByY = new Comparator<Point>() {
		@Override
		public int compare(Point a, Point b) {
			if (a.y < b.y)
				return -1;
			else if (a.y == b.y)
				return 0;
			else
				return 1;
		}
	};

	public static class Point {
		public double x;
		public double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
}
