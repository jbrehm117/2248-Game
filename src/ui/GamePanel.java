package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import api.ShowDialogListener;
import api.ScoreUpdateListener;
import hw3.ConnectGame;

public class GamePanel extends JPanel implements ShowDialogListener, ScoreUpdateListener {
	private static final long serialVersionUID = 1L;
	private GridVis playGrid;
	private Box box;
	private ConnectGame game;
	private JLabel scoreLabel;

	public GamePanel(ConnectGame game) {
		this.game = game;
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		JPanel southPanel = new JPanel();
		southPanel.setOpaque(false);
		JButton saveButton = new JButton("Save");
		JButton loadButton = new JButton("Load");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		scoreLabel = new JLabel();
		updateScore(0);
		southPanel.add(saveButton);
		southPanel.add(loadButton);
		southPanel.add(scoreLabel);
		add(southPanel, BorderLayout.SOUTH);
		setBackground(new Color(0x444444));
		box = new Box(BoxLayout.Y_AXIS);
		add(box, BorderLayout.CENTER);
	}
	
	public void setPlayGrid(GridVis playGrid) {
		this.playGrid = playGrid;
		box.removeAll();
		box.add(Box.createVerticalGlue());
		box.add(playGrid);
		box.add(Box.createVerticalGlue());
		repaint();
	}
	
	public String fileChooser(boolean save) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result;
		if (save) {
			result = fileChooser.showSaveDialog(this);
		} else {
			result = fileChooser.showOpenDialog(this);
		}
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    return selectedFile.getAbsolutePath();
		}	
		return "";
	}
	
	public void save() {
		String filePath = fileChooser(true);
		game.save(filePath);
	}
	
	public void load() {
		String filePath = fileChooser(false);
		game.load(filePath);
		updateScore(game.getScore());
		playGrid = new GridVis(game, game.getGrid());
		setPlayGrid(playGrid);
	}

	@Override
	public void showDialog(String dialog) {
		JOptionPane.showMessageDialog(this, dialog);
	}

	@Override
	public void updateScore(long score) {
		scoreLabel.setFont(new Font("SanSerif", Font.BOLD, 18));
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setText("Score: " + score);
	}
}
