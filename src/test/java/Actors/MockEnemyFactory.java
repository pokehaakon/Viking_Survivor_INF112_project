package Actors;

import Actors.Enemy.EnemyFactory;
import FileHandling.FileHandler;
import FileHandling.GdxFileHandler;
import com.badlogic.gdx.graphics.Texture;
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
        FileHandler mockFileHandler = mock(GdxFileHandler.class);
        when(mockFileHandler.loadTexture(Mockito.anyString())).thenReturn(mockTexture);

        enemyFactory.setFileHandler(mockFileHandler);
    }

    public EnemyFactory get() {
        return enemyFactory;
    }
}
