package ui;

import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;

import hw3.ConnectGame;

public class GameMain extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameMain frame = new GameMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameMain() {
		Random rand = new Random();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 900);
		
		ConnectGame game = new ConnectGame(5, 8, 1, 4, rand);
		game.radomizeTiles();
		
		GridVis playGrid = new GridVis(game, game.getGrid());
		GamePanel gamePanel = new GamePanel(game);
		setContentPane(gamePanel);
	
		gamePanel.setPlayGrid(playGrid);
		game.setListeners(gamePanel, gamePanel);
	}
}
