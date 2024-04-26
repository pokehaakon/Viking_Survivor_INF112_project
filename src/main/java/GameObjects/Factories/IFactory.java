package GameObjects.Factories;

import GameObjects.GameObject;

@FunctionalInterface
public interface IFactory<T extends GameObject> {

    /**
     * Creates a game object of a desired type
     * @param name
     * @return a GameObject object
     */
    T create(String name);

}
