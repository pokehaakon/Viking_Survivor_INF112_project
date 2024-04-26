package GameObjects.Pool;

import java.util.List;

public interface IPool<T> {
    T get(String name);
    List<T> get(String name, int num);
    void returnToPool(T object);
}
