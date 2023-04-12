package ui;

import java.awt.Color;

public class ColorTheme {
	private final Color colors[] = { new Color(0x444444),
		new Color(0x8cb369), new Color(0xe4d275), new Color(0xf4a259),
		new Color(0x5b8e7d), new Color(0xbc4b51), new Color(0x6e44ff),
		new Color(0xb892ff), new Color(0xafd0bf), new Color(0x808f87),
		new Color(0x9b7e46), new Color(0xf4b266), new Color(0x7798ab),
		new Color(0xc3dbc5), new Color(0xe8dcb9), new Color(0xf2cee6)
	};

	public Color get(int level) {
		if (level >= colors.length) {
			return colors[0];
		}
		return colors[level];
	}
}
