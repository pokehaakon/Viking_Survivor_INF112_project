package GameObjects.Factories;

import Rendering.Animations.AnimationRendering.AnimationRender;
import GameObjects.GameObject;

import java.util.List;

//public interface IFactory<T extends GameObject<E>, E extends Enum<E>> {
@FunctionalInterface
public interface IFactory<T extends GameObject> {

    /**
     * Creates a game object of a desired type
     * @param name
     * @return a GameObject object
     */
    T create(String name);

//    /**
//     * Creates a list of game objects
//     * @param n size of list
//     * @param name type of object
//     * @return a list of GameObject objects
//     */
//    List<T> create(int n, String name);


//    /**
//     * Changes the FileHandler. For testing purposes.
//     */
//    void setSpriteRender(AnimationRender spriteRender);
//
//    void setGifRender(AnimationRender gifRender);

}
