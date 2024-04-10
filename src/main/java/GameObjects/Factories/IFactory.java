package GameObjects.Factories;

import GameObjects.GameObject;
import TextureHandling.TextureHandler;

import java.util.List;

//public interface IFactory<T extends GameObject<E>, E extends Enum<E>> {
public interface IFactory<T extends GameObject<E>, E extends Enum<E>> {

    /**
     * Creates a game object of a desired type
     * @param type
     * @return a GameObject object
     */
    T create(E type);

    /**
     * Creates a list of game objects
     * @param n size of list
     * @param type type of object
     * @return a list of GameObject objects
     */
    List<T> create(int n, E type);


    /**
     * Changes the FileHandler. For testing purposes.
     * @param textureHandler the new texture handler
     */
    void setTextureHandler(TextureHandler textureHandler);

}
