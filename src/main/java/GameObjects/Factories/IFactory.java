package GameObjects.Factories;

import GameObjects.GameObject;
import TextureHandling.TextureHandler;

import java.util.List;

public interface IFactory<T extends GameObject> {

    /**
     * Creates a game object of a desired type
     * @param type
     * @return a GameObject object
     */
    T create(String type);

    /**
     * Creates a list of game objects
     * @param n size of list
     * @param type type of object
     * @return a list of GameObject objects
     */
    List<T> create(int n, String type);


    /**
     * Changes the FileHandler. For testing purposes.
     * @param textureHandler the new texture handler
     */
    void setTextureHandler(TextureHandler textureHandler);

}
