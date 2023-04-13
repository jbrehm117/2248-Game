package hw3;
import java.util.Random;
import java.util.ArrayList;

import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Tile;
/**
 * Class that models a game.
 * @author Joseph Brehm
 */
public class ConnectGame {
	/** given api */
	private ShowDialogListener dialogListener;
	/** given api */
	private ScoreUpdateListener scoreListener;
	/** The minimum tile level of a game.
	 *  Actual number on a tile is 2 raised to the power of the tile level.
	 */
	private int minTileLevel;
	/** The maximum tile level of a game.
	 *  Actual number on a tile is 2 raised to the power of the tile level.
	 */
	private int maxTileLevel;
	/** Variable that keeps track of the score.
	 *  The score is all tile face values from a selection added up and added to the total score
	 */
	private long score;
	/** Generates a random number */
	private Random rand;
	/** A new grid class that keeps track of the width and height of the game board*/
	private Grid grid;
	/** The tile that is first selected in the start of a selection*/
	private Tile startTile;
	/** The tile that is selected last in a selection, this tile changes depending on the size of the selection and
	 * is always the last tile
	 */
	private Tile lastSelectedTile;
	/** Keeps track of the selected tiles, tiles can be added or removed. */
	private ArrayList<Tile> selectedTiles;
	/** Is either true or false for if a selection has started or not*/
	private boolean startSelection;


	/**
	 * Constructs a new ConnectGame object with given grid dimensions and minimum
	 * and maximum tile levels.
	 * 
	 * @param width  grid width
	 * @param height grid height
	 * @param min    minimum tile level
	 * @param max    maximum tile level
	 * @param rand   random number generator
	 */
	public ConnectGame(int width, int height, int min, int max, Random rand) {
		grid = new Grid(width, height);
		minTileLevel = min;
		maxTileLevel = max;
		this.rand = rand;
		selectedTiles = new ArrayList<Tile>();
		startSelection = false;
	}

	/**
	 * Gets a random tile with level between minimum tile level inclusive and
	 * maximum tile level exclusive. For example, if minimum is 1 and maximum is 4,
	 * the random tile can be either 1, 2, or 3.
	 * <p>
	 * DO NOT RETURN TILES WITH MAXIMUM LEVEL
	 * 
	 * @return a tile with random level between minimum inclusive and maximum
	 *         exclusive
	 */
	public Tile getRandomTile() {
		int min = minTileLevel;
		int max = maxTileLevel;
		int tileValue = rand.nextInt(maxTileLevel - minTileLevel) + minTileLevel;
		return new Tile(tileValue);

	}

	/**
	 * Regenerates the grid with all random tiles produced by getRandomTile().
	 */
	public void radomizeTiles() {
		for (int i = 0; i < grid.getHeight(); i++) {
			for (int j = 0; j < grid.getWidth(); j++) {
				grid.setTile(getRandomTile(), j, i);
			}
		}
	}

	/**
	 * Determines if two tiles are adjacent to each other. The may be next to each
	 * other horizontally, vertically, or diagonally.
	 * 
	 * @param t1 one of the two tiles
	 * @param t2 one of the two tiles
	 * @return true if they are next to each other horizontally, vertically, or
	 *         diagonally on the grid, false otherwise
	 */
	public boolean isAdjacent(Tile t1, Tile t2) {
	    if (t1 == null || t2 == null) {
		return false;
	    }
	    int xDiff = Math.abs(t1.getX() - t2.getX());
	    int yDiff = Math.abs(t1.getY() - t2.getY());
	    return xDiff <= 1 && yDiff <= 1 && xDiff + yDiff != 0;
	}

	/**
	 * Indicates the user is trying to select (clicked on) a tile to start a new
	 * selection of tiles.
	 * <p>
	 * If a selection of tiles is already in progress, the method should do nothing
	 * and return false.
	 * <p>
	 * If a selection is not already in progress (this is the first tile selected),
	 * then start a new selection of tiles and return true.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return true if this is the first tile selected, otherwise false
	 */
	public boolean tryFirstSelect(int x, int y) {
		if (!startSelection) {
			startTile = grid.getTile(x, y);
			lastSelectedTile = startTile;
			// Sets tile selection boolean to true
			startTile.setSelect(true);
			// Adds the tile to an ArrayList of tiles
			selectedTiles.add(startTile);
			startSelection = true;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Indicates the user is trying to select (mouse over) a tile to add to the
	 * selected sequence of tiles. The rules of a sequence of tiles are:
	 * 
	 * <pre>
	 * 1. The first two tiles must have the same level.
	 * 2. After the first two, each tile must have the same level or one greater than the level of the previous tile.
	 * </pre>
	 * 
	 * For example, given the sequence: 1, 1, 2, 2, 2, 3. The next selected tile
	 * could be a 3 or a 4. If the use tries to select an invalid tile, the method
	 * should do nothing. If the user selects a valid tile, the tile should be added
	 * to the list of selected tiles.
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 */
	public void tryContinueSelect(int x, int y) {
		if (startSelection && isAdjacent(lastSelectedTile, grid.getTile(x, y))) {
			if (selectedTiles.indexOf(lastSelectedTile) - 1 == selectedTiles.indexOf(grid.getTile(x, y)) && grid.getTile(x, y).isSelected()){
				unselect(lastSelectedTile.getX(), lastSelectedTile.getY());
			} else if (selectedTiles.size() < 2 && (!grid.getTile(x, y).isSelected())) {
				if (startTile.getLevel() == grid.getTile(x, y).getLevel()) {
					lastSelectedTile = grid.getTile(x, y);
					lastSelectedTile.setSelect(true);
					selectedTiles.add(lastSelectedTile);
				}
			} else if ((lastSelectedTile.getLevel() == grid.getTile(x, y).getLevel() || lastSelectedTile.getLevel() == grid.getTile(x, y).getLevel() + 1 || lastSelectedTile.getLevel() == grid.getTile(x, y).getLevel() - 1) && (!grid.getTile(x, y).isSelected())) {
				lastSelectedTile = grid.getTile(x, y);
				lastSelectedTile.setSelect(true);
				selectedTiles.add(lastSelectedTile);
			}
		}
	}

	/**
	 * Indicates the user is trying to finish selecting (click on) a sequence of
	 * tiles. If the method is not called for the last selected tile, it should do
	 * nothing and return false. Otherwise it should do the following:
	 * 
	 * <pre>
	 * 1. When the selection contains only 1 tile reset the selection and make sure all tiles selected is set to false.
	 * 2. When the selection contains more than one block:
	 *     a. Upgrade the last selected tiles with upgradeLastSelectedTile().
	 *     b. Drop all other selected tiles with dropSelected().
	 *     c. Reset the selection and make sure all tiles selected is set to false.
	 * </pre>
	 * 
	 * @param x the column of the tile selected
	 * @param y the row of the tile selected
	 * @return return false if the tile was not selected, otherwise return true
	 */
	public boolean tryFinishSelection(int x, int y) {
		int addScore = 0;
		boolean tileLastSelected = false;
		lastSelectedTile.setLocation(x, y);
		if (lastSelectedTile.isSelected()) {
			if (lastSelectedTile.getX() == startTile.getX() && lastSelectedTile.getY() == startTile.getY()) {
				startSelection = false;
				startTile.setSelect(false);
				lastSelectedTile.setSelect(false);
				startTile = null;
				lastSelectedTile = null;
				selectedTiles.clear();
				tileLastSelected = true;
			} else {
				for (int i = 0; i < selectedTiles.size(); i++) {
					addScore += Math.pow(2, selectedTiles.get(i).getLevel());
					selectedTiles.get(i).setSelect(false);
				}
				upgradeLastSelectedTile();
				dropSelected();
				lastSelectedTile = null;
				selectedTiles.clear();
				tileLastSelected = true;
				startSelection = false;
				score += addScore;
				setScore(score);
			}
		}
		return tileLastSelected;
	}

	/**
	 * Increases the level of the last selected tile by 1 and removes that tile from
	 * the list of selected tiles. The tile itself should be set to unselected.
	 * <p>
	 * If the upgrade results in a tile that is greater than the current maximum
	 * tile level, both the minimum and maximum tile level are increased by 1. A
	 * message dialog should also be displayed with the message "New block 32,
	 * removing blocks 2". Not that the message shows tile values and not levels.
	 * Display a message is performed with dialogListener.showDialog("Hello,
	 * World!");
	 */
	public void upgradeLastSelectedTile() {
		lastSelectedTile.setLevel(lastSelectedTile.getLevel() + 1);
		selectedTiles.remove(lastSelectedTile);
		lastSelectedTile.setSelect(false);
		if (lastSelectedTile.getLevel() > maxTileLevel) {
			maxTileLevel += 1;
			minTileLevel += 1;
			dropLevel(minTileLevel - 1);
			dialogListener.showDialog("New block " + (int) Math.pow(2, maxTileLevel) + ", removing blocks " + (int) Math.pow(2, minTileLevel-1));
		}
	}

	/**
	 * Gets the selected tiles in the form of an array. This does not mean selected
	 * tiles must be stored in this class as an array.
	 * 
	 * @return the selected tiles in the form of an array
	 */
	public Tile[] getSelectedAsArray() {
		int n = selectedTiles.size();
		Tile[] selectedTilesArray = new Tile[n];
		for (int i = 0; i < n; i++) {
			selectedTilesArray[i] = selectedTiles.get(i);
		}
		return selectedTilesArray;
	}

	/**
	 * Removes all tiles of a particular level from the grid. When a tile is
	 * removed, the tiles above it drop down one spot and a new random tile is
	 * placed at the top of the grid.
	 * 
	 * @param level the level of tile to remove
	 */
	public void dropLevel(int level) {
		int width = grid.getWidth();
		int height = grid.getHeight();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Tile replace = grid.getTile(j, i);
				if (replace.getLevel() == level) {
					for (int m = i; m < height; m--) {
						try {
							Tile toReplace = grid.getTile(replace.getX(), m - 1);
							grid.setTile(toReplace, replace.getX(), m);
						} catch (ArrayIndexOutOfBoundsException e) {
							grid.setTile(getRandomTile(), replace.getX(), m);
							break;
						}
					}
				}

			}
		}
	}
	/**
	 * Removes all selected tiles from the grid. When a tile is removed, the tiles
	 * above it drop down one spot and a new random tile is placed at the top of the
	 * grid.
	 */
	public void dropSelected() {
		for (int i = 0; i < selectedTiles.size(); i++) {
			int x = selectedTiles.get(i).getX();
			int y = selectedTiles.get(i).getY();
			Tile replace = grid.getTile(x, y);
			for (int m = y; m < grid.getHeight(); m--) {
				try {
					Tile toReplace = grid.getTile(replace.getX(), m - 1);
					grid.setTile(toReplace, replace.getX(), m);
				} catch (ArrayIndexOutOfBoundsException e) {
					grid.setTile(getRandomTile(), replace.getX(), m);
					break;
				}
			}
		}
	}

	/**
	 * Remove the tile from the selected tiles.
	 * 
	 * @param x column of the tile
	 * @param y row of the tile
	 */
	public void unselect(int x, int y) {
		lastSelectedTile = selectedTiles.get(selectedTiles.indexOf(lastSelectedTile) - 1);
		selectedTiles.remove(grid.getTile(x, y));
		grid.getTile(x, y).setSelect(false);
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return the score
	 */
	public long getScore() {
		return this.score;
	}

	/**
	 * Gets the game grid.
	 * 
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Gets the minimum tile level.
	 * 
	 * @return the minimum tile level
	 */
	public int getMinTileLevel() {return minTileLevel;}

	/**
	 * Gets the maximum tile level.
	 * 
	 * @return the maximum tile level
	 */
	public int getMaxTileLevel() {return maxTileLevel;}

	/**
	 * Sets the player's score.
	 * 
	 * @param score number of points
	 */
	public void setScore(long score) {
		this.score = score;
		try {
			scoreListener.updateScore(this.score);
		} catch(NullPointerException e) {
			this.score = score;
		}
	}

	/**
	 * Sets the game's grid.
	 * 
	 * @param grid game's grid
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Sets the minimum tile level.
	 * 
	 * @param minTileLevel the lowest level tile
	 */
	public void setMinTileLevel(int minTileLevel) {
		this.minTileLevel = minTileLevel;
	}

	/**
	 * Sets the maximum tile level.
	 * 
	 * @param maxTileLevel the highest level tile
	 */
	public void setMaxTileLevel(int maxTileLevel) {
		this.maxTileLevel = maxTileLevel;
	}

	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Save the game to the given file path.
	 * 
	 * @param filePath location of file to save
	 */
	public void save(String filePath) {
		GameFileUtil.save(filePath, this);
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}
}
