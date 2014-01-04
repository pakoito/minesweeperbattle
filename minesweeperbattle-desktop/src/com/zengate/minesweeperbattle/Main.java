package com.zengate.minesweeperbattle;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "minesweeperbattle";
		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 512;
		
		new LwjglApplication(new MinesweeperBattle(), cfg);
	}
}
