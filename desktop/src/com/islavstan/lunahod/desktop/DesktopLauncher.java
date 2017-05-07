package com.islavstan.lunahod.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.islavstan.lunahod.Lunahod;
import com.islavstan.lunahod.utils.GameCallback;

public class DesktopLauncher {
	public DesktopLauncher(){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new Lunahod(callback), config);
	}

	private GameCallback callback = new GameCallback() {
		@Override
		public void sendMessage(int message) {
			System.out.println("DesktopLauncher sendMessage "+ message);
		}
	};

	public static void main (String[] arg) {
		new DesktopLauncher();
	}


}
