package Tools.Pool;

import java.util.List;

public sealed interface IPool<T extends Poolable > permits ObjectPool, SmallPool {

    /**
     * Get an object with 'name'
     * @param name the name of the object
     * @return the wanted object
     */
    T get(String name);

    /**
     * Get a list of 'num' objects with 'name'
     * @param name the name of the objects
     * @param num the number of objects
     * @return a list of objects
     */
    List<T> get(String name, int num);

    /**
     * returns an object to its pool. The object is reset.
     * @param object the object to return
     */
    void returnToPool(T object);

    /**
     * get a {@link SmallPool} with objects 'name'
     * @param name the name of the objects
     * @return A {@link SmallPool}
     */
    SmallPool<T> getSmallPool(String name);
}
