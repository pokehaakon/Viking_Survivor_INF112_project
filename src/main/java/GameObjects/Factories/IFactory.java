package GameObjects.Factories;

import GameObjects.Animations.AnimationRendering.AnimationRender;
import GameObjects.GameObject;
import TextureHandling.TextureHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

//public interface IFactory<T extends GameObject<E>, E extends Enum<E>> {
public interface IFactory<T extends GameObject<E>, E extends Enum<E>> {


    /**
     * Creates a supplier - a set of instructions of creating the object
     * @param type object type
     * @return a custom object supplier
     */
     Supplier<T> build(E type);


    /**
     * Creates a game object of a desired type
     *
     * @param type enum object type
     * @return a GameObject object
     */
    T create (E type);

    /**
     * Create multiple game objects
     * @param type desired type
     * @param num number of instances
     * @return list of game objects
     */
    List<T> create (E type, int num);


    /**
     * Adds a supplier of every enum type to the factory register
     * @param types list of enum types to add to the factory inventory
     */
    void registerAll(E[] types);


    /**
     * Adds a type and its corresponding supplier to the factory inventory
     * @param type
     * @param supplier
     */
    void register(E type, Supplier<T> supplier);

}
