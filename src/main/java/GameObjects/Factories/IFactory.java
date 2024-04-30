package GameObjects.Factories;


import GameObjects.GameObject;


import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@FunctionalInterface
public interface IFactory<T extends GameObject> {

    /**
     * Creates a game object of a desired type
     *
     * @param type enum object type
     * @return a GameObject object
     */
    T create (String type);
}
