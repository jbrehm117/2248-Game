package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import api.Tile;
import hw3.Grid;

public class TileVis extends JButton {
	private static final long serialVersionUID = 1L;
	private GridVis gridVis;
	private int x;
	private int y;

	public TileVis(int x, int y, GridVis gridVis) {
		this.x = x;
		this.y = y;
		this.gridVis = gridVis;
		update();
		setBorderPainted(false);
		setOpaque(true);
		setForeground(Color.WHITE);
		setFont(new Font("SansSerif", Font.BOLD, 22));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				boolean firstSelect;
				if (gridVis.isFirstSelect()) {
					firstSelect = !gridVis.getGame().tryFirstSelect(x, y);
				} else {
					firstSelect = gridVis.getGame().tryFinishSelection(x, y);
				}
				gridVis.setIsFirstselect(firstSelect);
				gridVis.update();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				gridVis.getGame().tryContinueSelect(x, y);
				gridVis.update();
			}
		});
	}
	
	public void update() {
		Grid grid = gridVis.getGame().getGrid();
		Tile tile = grid.getTile(x, y);
		if (tile.getValue() < 100) {
			setFont(new Font("SansSerif", Font.BOLD, 22));
		} else if (tile.getValue() < 1000) {
			setFont(new Font("SansSerif", Font.BOLD, 16));
		} else {
			setFont(new Font("SansSerif", Font.BOLD, 12));
		}
		setText(Integer.toString(tile.getValue()));
		setBackground(gridVis.getTheme().get(tile.getLevel()));
		if (tile.isSelected()) {
			setBackground(gridVis.getTheme().get(0));
		} else {
			setBackground(gridVis.getTheme().get(tile.getLevel()));
		}
	}
}
