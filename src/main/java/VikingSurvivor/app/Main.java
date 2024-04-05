package VikingSurvivor.app;

//import InputProcessing.Contexts.TrainingContext;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;

public class Main {

    public static int SCREEN_WIDTH = 1024, SCREEN_HEIGHT = 1024;
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Viking Survivor");
        //cfg.setWindowedMode(480, 320);
        cfg.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        new Lwjgl3Application(new HelloWorld(), cfg);

    }
}