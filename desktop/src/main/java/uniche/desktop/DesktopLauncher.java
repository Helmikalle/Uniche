package main.java.uniche.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import main.java.uniche.MainLauncher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//Ikkunan confeja määritelty
		config.title = "UNICHE";
		config.width = 800;
		config.height = 480;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		new LwjglApplication(new MainLauncher(), config);
	}
}
