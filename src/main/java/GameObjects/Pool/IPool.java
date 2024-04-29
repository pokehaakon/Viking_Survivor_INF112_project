package GameObjects.Pool;

import GameObjects.GameObject;

import java.util.List;

public sealed interface IPool<T extends GameObject> permits ObjectPool, SmallPool {
    T get(String name);
    List<T> get(String name, int num);
    void returnToPool(T object);
    SmallPool<T> getSmallPool(String name);
}
