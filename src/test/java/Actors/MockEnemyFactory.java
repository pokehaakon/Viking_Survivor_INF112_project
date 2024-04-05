package Actors;

import GameObjects.Factories.EnemyFactory;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * An EnemyFactory object which uses mock textures
 */
public class MockEnemyFactory {
    EnemyFactory enemyFactory;

    public MockEnemyFactory() {
        enemyFactory = new EnemyFactory();
        Texture mockTexture = mock(Texture.class);
        when(mockTexture.getHeight()).thenReturn(10);
        when(mockTexture.getWidth()).thenReturn(10);
        TextureHandler mockTextureHandler = mock(GdxTextureHandler.class);
        when(mockTextureHandler.loadTexture(Mockito.anyString())).thenReturn(mockTexture);

        enemyFactory.setTextureHandler(mockTextureHandler);
    }

    public EnemyFactory get() {
        return enemyFactory;
    }
}
