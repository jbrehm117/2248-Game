import java.util.Arrays;
import java.util.Random;

import api.Tile;
import hw3.ConnectGame;
import hw3.GameFileUtil;
import hw3.Grid;
import ui.GameConsole;

/**
 * Examples of using the ConnectGame, GameFileUtil, and Grid classes. The main()
 * method in this class only displays to the console. For the full game GUI, run
 * the ui.GameMain class.
 */
public class SimpleTests {
	public static void main(String args[]) {
		// Example use of Grid
		Grid grid = new Grid(2, 2);
		grid.setTile(new Tile(1), 0, 0);
		grid.setTile(new Tile(2), 1, 0);
		grid.setTile(new Tile(1), 0, 1);
		grid.setTile(new Tile(1), 1, 1);
		System.out.println("Grid tests");
		System.out.println("Width: " + grid.getWidth());
		System.out.println("Height: " + grid.getHeight());
		System.out.println("Tile: " + grid.getTile(0, 1));
		System.out.println(grid);
		System.out.println();

		// Example use of ConnectGame
		ConnectGame game = new ConnectGame(2, 2, 1, 4, new Random(0));
		GameConsole gc = new GameConsole();
		game.setListeners(gc, gc);
		game.setGrid(grid);
		
		System.out.println("Random tests");
		Tile randomTile = game.getRandomTile();
		// the test will always produce the same result because Random has been given a seed
		System.out.println(randomTile);
		
		game.radomizeTiles();
		System.out.println(grid);
		System.out.println();
		
		System.out.println("Selection tests");
		System.out.println(game.isAdjacent(grid.getTile(0, 0), grid.getTile(1, 1))); // expected true
		System.out.println(game.tryFirstSelect(0, 0)); // expected true
		System.out.println(game.tryFirstSelect(0, 0)); // expected false
		
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // (0,0,4,true)
		game.tryContinueSelect(1, 0);
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // (0,0,4,true)
		game.tryContinueSelect(0, 1);
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // (0,0,4,true), (0,1,4,true)
		game.tryContinueSelect(1, 0);
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // (0,0,4,true), (0,1,4,true), (1,0,8,true)

		game.tryFinishSelection(0, 1);
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // (0,0,4,true), (0,1,4,true), (1,0,8,true)
		game.tryFinishSelection(1, 0);
		System.out.println(Arrays.toString(game.getSelectedAsArray())); // []
		System.out.println(game.getScore()); // 16
		
		// [   (0,0,8,false),  (1,0,16,false)]
		// [   (0,1,8,false),   (1,1,8,false)]
		System.out.println(game.getGrid());
		System.out.println();
		
		game.dropLevel(3);
		// [   (0,0,2,false),   (1,0,8,false)]
		// [   (0,1,2,false),  (1,1,16,false)]
		System.out.println(game.getGrid());
		System.out.println();
		
		System.out.println("Save and load tests");
		GameFileUtil.save("game.txt", game);
		GameFileUtil.load("game.txt", game);
		System.out.println(game.getGrid());
	}
}
