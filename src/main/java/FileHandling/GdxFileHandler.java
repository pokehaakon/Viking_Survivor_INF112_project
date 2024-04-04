package FileHandling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class GdxFileHandler implements FileHandler {
    @Override
    public Texture loadTexture(String path) {
        return new Texture(Gdx.files.internal(path));
    }
}
