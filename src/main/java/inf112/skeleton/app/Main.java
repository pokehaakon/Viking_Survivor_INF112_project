package inf112.skeleton.app;

import InputProcessing.ContextualInputProccessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("hello-world");
        cfg.setWindowedMode(480, 320);
        new Lwjgl3Application(new HelloWorld(), cfg);

    }
}