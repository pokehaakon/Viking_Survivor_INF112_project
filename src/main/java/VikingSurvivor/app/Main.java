package VikingSurvivor.app;

import Actors.Coordinates;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("hello-world");
        //cfg.setWindowedMode(480, 320);
        cfg.setWindowedMode(1920, 1080);
        new Lwjgl3Application(new HelloWorld(), cfg);

    }
}