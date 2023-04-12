package api;

public class Tile {
	private int x;
	private int y;
	private int level;
	private boolean isSelected;

	public Tile(int level) {
		this.level = level;
		isSelected = false;
	}
	
	public void setSelect(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
	
	public int getValue() {
		return (int) Math.pow(2, level);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.format("%16s", "(" + x + ","+ y + "," + getValue() + "," + isSelected + ")");
	}
}
