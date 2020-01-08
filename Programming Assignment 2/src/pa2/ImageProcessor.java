package pa2;

import java.util.ArrayList;

/*
 * @Author: Josh Loftus, Emin Oick
 */

public class ImageProcessor {

	public static Picture reduceWidth(int x, String inputImage) {
		Picture in, out;
		int width, height, out_width, i, j, tmp_j;
		int[][] I, tmp;
		ArrayList<ArrayList<Boolean>> pixels;
		
		ArrayList<Boolean> current;
		int loc;
		
		in = new Picture(inputImage);
		pixels = new ArrayList<ArrayList<Boolean>>();		// keeps track of the pixels that were cut out
		width = in.width();
		height = in.height();
		out = new Picture(width - x, height);				// results dimensions are (W-x, H)
		out_width = out.width();
		I = new int[height][width];							// Importance Array
		for (i = 0; i < height; i++) {
			ArrayList<Boolean> layer = new ArrayList<Boolean>();		// initally all pixels are set to true
			for (j = 0; j < width; j++)
				layer.add(true);
			pixels.add(layer);
		}
			
		for (i = 0; i < height; i++) {
			for (j = 0; j < width; j++) {
				if (0 < j && j < out_width)															// checks Dist() in all 3 directions
					I[i][j] = ImageStitch.pixelDistance(in.get(j - 1, i), in.get(j + 1, i));
				else if (j == 0)
					I[i][j] = ImageStitch.pixelDistance(in.get(j, i), in.get(j + 1, i));
				else if (j == out_width)
					I[i][j] = ImageStitch.pixelDistance(in.get(j, i), in.get(j - 1, i));
			}
		}
		
		while (x > 0) {											// decreases image width 1 at a time
			ArrayList<Tuple> cut = MatrixCuts.widthCut(I);									// use widthCut from MatrixCuts class to find least cost path
			for (Tuple remove : cut) {
				if (!remove.equals(cut.get(0))) {
					I[remove.getX()][remove.getY()] = -1;
					current = pixels.get(remove.getX());										// mark the pixels that are cut
					loc = remove.getY();
					while (loc < current.size() && !current.get(loc))
						loc++;
					current.set(loc, false);
				}
			}
			tmp = new int[I.length][I[0].length - 1];
			
			for (i = 0; i < I.length; i++) {
				tmp_j = 0;
				for (j = 0; j < I[i].length; j++) {
					if (I[i][j] != -1) {
						tmp[i][tmp_j] = I[i][j];						// build revised importance array with out certain pixels
						tmp_j++;
					}
				}
			}
			I = new int[tmp.length][tmp[0].length];
			I = tmp;
			x --;
		}
		for (i = 0; i < height; i++) {
			current = pixels.get(i);
			tmp_j = 0;
			for (j = 0; j < width; j++) {
				if (current.get(j) && tmp_j < out_width) {
					out.set(tmp_j, i, in.get(j, i));			// set the new photo = to the reduced image
					tmp_j++;
				}
			}
		}
		return out;
	}
	
	// Utility method for understanding the 2D arrays
	public static void info(int[][] picture) {
		int x, y;
		String line;
		System.out.println("\n-----------------------");
		for (y = 0; y < picture.length; y++) {
			for (x = 0; x < picture[y].length; x++) {
				line = picture[y][x] + "";
				while (line.length() < 5)
					line += " ";
				System.out.print(line);
			}
			System.out.println("");
		}
		System.out.println("-----------------------\n");
	}
}
