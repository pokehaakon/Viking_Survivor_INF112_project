package TextureHandling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class GdxTextureHandler implements TextureHandler {
    Map<String, Texture> textureMap;

    public GdxTextureHandler() {
        textureMap = new HashMap<>();
    }
    @Override
    public Texture loadTexture(String path) {
        if (!textureMap.containsKey(path))
            textureMap.put(path, new Texture(Gdx.files.internal(path)));
        return textureMap.get(path);
    }
}
