package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import api.Tile;
import hw3.ConnectGame;
import hw3.Grid;

public class GridVis extends JPanel {
	private static final long serialVersionUID = 1L;
	private ConnectGame game;
	private TileVis tiles[][];
	private ColorTheme theme;
	private boolean firstSelect;
	private JPanel tilePanel;

	public GridVis(ConnectGame game, Grid grid) {
		firstSelect = true;
		this.game = game;
		theme = new ColorTheme();
		tilePanel = new JPanel();
		tilePanel.setLayout(null);
		tiles = new TileVis[grid.getWidth()][grid.getHeight()];
		add(tilePanel);

		for (int y = 0; y < grid.getHeight(); y++) {
			for (int x = 0; x < grid.getWidth(); x++) {
				tiles[x][y] = new TileVis(x, y, this);
				tiles[x][y].setBounds(x * 100 + 10, y * 100 + 10, 85, 85);
				tilePanel.add(tiles[x][y]);
				tiles[x][y].update();
			}
		}

		Dimension dim = new Dimension(grid.getWidth() * 100 + 5, grid.getHeight() * 100 + 5);
		setBackground(new Color(0x444444));
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		tilePanel.setOpaque(false);
		tilePanel.setPreferredSize(dim);
		tilePanel.setMaximumSize(dim);
		tilePanel.setMinimumSize(dim);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Tile[] selected = game.getSelectedAsArray();
		Tile prev = null;
		for (Tile tile : selected) {
			if (prev != null) {
				int x1 = tile.getX();
				int y1 = tile.getY();
				int x2 = prev.getX();
				int y2 = prev.getY();
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.RED);
				g2.setStroke(new BasicStroke(10));
				g2.draw(new Line2D.Float(x1 * 100 + 50, y1 * 100 + 50, x2 * 100 + 50, y2 * 100 + 50));
			}
			prev = tile;
		}
	}

	public void update() {
		removeAll();
		for (int y = 0; y < tiles[0].length; y++) {
			for (int x = 0; x < tiles.length; x++) {
				tiles[x][y].update();
			}
		}
		add(tilePanel);
		revalidate();
		repaint();
	}

	public ConnectGame getGame() {
		return game;
	}

	public ColorTheme getTheme() {
		return theme;
	}

	public void setIsFirstselect(boolean firstSelect) {
		this.firstSelect = firstSelect;
	}

	public boolean isFirstSelect() {
		return firstSelect;
	}
}
