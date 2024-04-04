package Actors.Enemy;

import FileHandling.FileHandler;
import com.badlogic.gdx.graphics.Texture;

import static org.mockito.Mockito.mock;

public class MockFileHandler implements FileHandler {

    @Override
    public Texture loadTexture(String path) {
        return mock(Texture.class);
    }
}