package hw3;

import api.Tile;

import java.io.*;
import java.util.Scanner;

/**
 * Utility class with static methods for saving and loading game files.
 * @author Joseph Brehm
 */
public class GameFileUtil {
	/**
	 * Saves the current game state to a file at the given file path.
	 * <p>
	 * The format of the file is one line of game data followed by multiple lines of
	 * game grid. The first line contains the: width, height, minimum tile level,
	 * maximum tile level, and score. The grid is represented by tile levels. The
	 * conversion to tile values is 2^level, for example, 1 is 2, 2 is 4, 3 is 8, 4
	 * is 16, etc. The following is an example:
	 * 
	 * <pre>
	 * 5 8 1 4 100
	 * 1 1 2 3 1
	 * 2 3 3 1 3
	 * 3 3 1 2 2
	 * 3 1 1 3 1
	 * 2 1 3 1 2
	 * 2 1 1 3 1
	 * 4 1 3 1 1
	 * 1 3 3 3 3
	 * </pre>
	 * 
	 * @param filePath the path of the file to save
	 * @param game     the game to save
	 */
	public static void save(String filePath, ConnectGame game) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(String.format("%d %d %d %d %d", game.getGrid().getWidth(), game.getGrid().getHeight(), game.getMinTileLevel(), game.getMaxTileLevel(), game.getScore()));
			writer.newLine();
			int[][] data = new int[game.getGrid().getHeight()][game.getGrid().getWidth()];
			for (int i = 0; i < game.getGrid().getHeight(); i++) {
				for (int j = 0; j < game.getGrid().getWidth(); j++) {
					data[i][j] = game.getGrid().getTile(j, i).getLevel();
				}
			}
			for (int[] row : data) {
				for (int i = 0; i < row.length; i++) {
					writer.write(row[i] + "");
					if (i != row.length - 1) { // check if it's not the last element in the row
						writer.write(" ");
					}
				}
				if (row != data[data.length - 1]) { // check if it's not the last row
					writer.newLine();
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * <p>
	 * See the save() method for the specification of the file format.
	 * 
	 * @param filePath the path of the file to load
	 * @param game     the game to modify
	 */
	public static void load(String filePath, ConnectGame game) {
		int width;
		int height;
		int minTileLevel;
		int maxTileLevel;
		long score;
		File f = new File(filePath);
		int[][] loadData;
		try {
			Scanner lineScnr = new Scanner(f);
			width = lineScnr.nextInt();
			height = lineScnr.nextInt();
			minTileLevel = lineScnr.nextInt();
			maxTileLevel = lineScnr.nextInt();
			score = (long) lineScnr.nextInt();
			lineScnr.nextLine();
			loadData = new int[height][width];
			int currLine = 0;
			while (lineScnr.hasNextLine()) {
				currLine++;
				String line = lineScnr.nextLine();
				Scanner numScnr = new Scanner(line);
				int currData = 0;
				while (numScnr.hasNext()) {
					currData++;
					int currInt = Integer.parseInt(numScnr.next());
					loadData[currLine - 1][currData - 1] = currInt;
				}
			}
			Grid grid = new Grid(width, height);
			for (int i = 0; i < loadData.length; i++) {
				for (int j = 0; j < loadData[j].length; j++) {
					grid.setTile(new Tile(loadData[i][j]), j, i);
				}
			}
			game.setGrid(grid);
			game.setScore(score);
			game.setMaxTileLevel(maxTileLevel);
			game.setMinTileLevel(minTileLevel);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	}
