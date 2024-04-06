package TextureHandling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GdxTextureHandler implements TextureHandler {
    @Override
    public Texture loadTexture(String path) {
        return new Texture(Gdx.files.internal(path));
    }
}
