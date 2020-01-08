package pa2;

import java.util.ArrayList;

/*
 * @Author: Josh Loftus, Emin Oick
 */

public class MatrixCuts {
	
	public static ArrayList<Tuple> widthCut(int[][] M) { return mcwc(wcSolution(M)); }
	
	public static ArrayList<Tuple> stitchCut(int[][] M) { return mcsc(scSolution(M)); }
	
	
	// Width Cut Bottom-up
	private static int[][] wcSolution(int[][] M) {
		int i, j, chosen;
		int[][] solution;
		
		solution = new int[M.length][M[0].length];
		solution[0][0] = M[0][0];
		
		for (i = 1; i < M[0].length; i++)				// fill first row
			solution[0][i] = M[0][i];
		for (i = 1; i < M.length; i++)					// fill first column
			solution[i][0] = M[i][0] + solution[i - 1][0];
		for (i = 1; i < M.length; i++) {
			for (j = 1; j < M[i].length; j++) {
				chosen = Math.min(solution[i - 1][j], solution[i-1][j - 1]);									// Altered step here, multiple choices not just solution[i-1][j]
				if (j + 1 < M[i].length) 
					chosen = Math.min(chosen, solution[i-1][j + 1]);
				solution[i][j] = M[i][j] + chosen;
			}
		}
		return solution;
	}
	
	// Min-Cost Width Cut
	private static ArrayList<Tuple> mcwc(int[][] in) {
		int i, j, min, min_j;
		ArrayList<Tuple> out, sol;
		out = new ArrayList<Tuple>();
		sol = new ArrayList<Tuple>();
		
		min = Integer.MAX_VALUE;
		j = 0;
		min_j = 0;
		for (int a : in[in.length - 1]) {				// find starting j at min
			if (a < min) {
				min = a;
				min_j = j;
			}
			j++;
		}

		sol.add(new Tuple(min, -1));						// add total, -1 to solution list
		out.add(new Tuple(in.length - 1, min_j));		// add first entry
		i = in.length - 2;
		j = min_j;
		while (i > -1) {
			min = in[i][j];
			min_j = j;
			if (j + 1 < in[i].length && in[i][j + 1] < min) {			// checks for min in all 3 directions
				min = in[i][j + 1];
				min_j = j + 1;
			}
			if (j - 1 > 0 && in[i][j - 1] < min) {
				min = in[i][j - 1];
				min_j = j - 1;
			}
			j = min_j;
			out.add(new Tuple(i, j));
			i--;
		}
		for (int a = out.size() - 1; a > -1; a--)			// reverse out
			sol.add(out.get(a));
		return sol;
	}
	
	// Stitch Cut Bottom-up
	private static int[][] scSolution(int[][] M) {
		int i, j, chosen;
		int[][] solution;
		
		solution = new int[M.length][M[0].length];
		solution[0][0] = M[0][0];
		
		for (i = 1; i < M[0].length; i++)				// fill first row
			solution[0][i] = M[0][i];
		for (i = 1; i < M.length; i++)					// fill first column
			solution[i][0] = M[i][0] + solution[i - 1][0];
		for (i = 1; i < M.length; i++) {
			for (j = 1; j < M[i].length; j++) {
				chosen = solution[i - 1][j];									// Altered step here, multiple choices not just solution[i-1][j]
				chosen = Math.min(chosen, solution[i-1][j - 1]);
				chosen = Math.min(chosen, solution[i][j - 1]);
				solution[i][j] = M[i][j] + chosen;
			}
		}
		return solution;
	}

	// Min-Cost Stitch Cut
	private static ArrayList<Tuple> mcsc(int[][] in) {
		int i, j, min, min_j, old_i;
		ArrayList<Tuple> out, sol;
		
		out = new ArrayList<Tuple>();
		sol = new ArrayList<Tuple>();
		min = Integer.MAX_VALUE;
		j = 0;
		min_j = 0;
		for (int a : in[in.length - 1]) {		// find starting j
			if (a < min) {
				min = a;
				min_j = j;
			}
			j++;
		}

		sol.add(new Tuple(min, -1));					// add total, -1 to solution
		out.add(new Tuple(in.length - 1, min_j));	// add first entry	
		i = in.length - 2;
		j = min_j;
		
		while (i != 0) {
			min = Integer.MAX_VALUE;
			old_i = i;
			if (j - 1 > - 1) {
				min = in[i][j - 1];
				min_j = j - 1;
			}
			if (i - 1 > -1 && j - 1 > -1 && in[i - 1][j - 1] < min) {			// find min for all 3 possible moves
				min = in[i - 1][j - 1];
				min_j = j - 1;
				i--;
			}
			if (i - 1 > -1 && in[i - 1][j] < min) {
				min = in[i - 1][j];
				min_j = j;
				if (i == old_i) i--;
			}
			j = min_j;
			out.add(new Tuple(i, j));
		}
		for (int a = out.size() - 1; a > -1; a--)							// need to reverse the order in out
			sol.add(out.get(a));
		return sol;
	}
}

















































