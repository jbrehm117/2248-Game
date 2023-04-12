package ui;

import api.ScoreUpdateListener;
import api.ShowDialogListener;

/**
 * A console version of the game UI for testing purposes.
 */
public class GameConsole implements ShowDialogListener, ScoreUpdateListener {
	@Override
	public void updateScore(long score) {
		System.out.println("Score updated: " + score);
	}

	@Override
	public void showDialog(String dialog) {
		System.out.println("User dialog: " + dialog);
	}
}
