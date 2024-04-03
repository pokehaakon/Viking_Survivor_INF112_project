package FileHandling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GdxFileHandler implements FileHandler {
    @Override
    public FileHandle internal(String path) {
        return Gdx.files.internal(path);
    }
}
