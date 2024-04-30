package VikingSurvivor.app;

//import InputProcessing.Contexts.TrainingContext;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {

    public static int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 1024;
    //public static final int PPM = 30;
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Viking Survivor");
        //cfg.setWindowedMode(480, 320);
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        new Lwjgl3Application(new HelloWorld(), cfg);

    }
}