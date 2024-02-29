package VikingSurvivor.app;

import Tools.Config.Config;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Config path = new Config();
        //System.out.println(path.getFilePath());
        Config config = new Config("config2.properties");
        System.out.printf("map name: %s, width: %d, height: %d", config.getMapName(), config.getWidth(), config.getHeight());

        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle(config.getMapName());
        //cfg.setWindowedMode(480, 320);
        cfg.setWindowedMode(config.getWidth(), config.getHeight());
        new Lwjgl3Application(new HelloWorld(), cfg);

    }
}