package de.lostmekka.gamejam.teamsharks.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.lostmekka.gamejam.teamsharks.GameJam8Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.width = 1920;
		config.height = 1080;
		config.resizable = false;
		new LwjglApplication(new GameJam8Game(), config);
	}
}
