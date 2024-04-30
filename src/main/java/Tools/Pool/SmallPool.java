package Tools.Pool;

import java.util.*;
import java.util.function.Supplier;


public final class SmallPool<T extends Poolable> implements IPool<T> {
    private final Supplier<T> factory;
    private final String name;
    private final Queue<T> pool = new ArrayDeque<>();

    public SmallPool(Supplier<T> factory, String name) {
        this.factory = factory;
        this.name = name;
    }

    /**
     * See {@link #get(String)}
     */
    public T get() {
        T obj;
        if (pool.isEmpty()) {
            obj = factory.get();
        } else {
            obj = pool.poll();
        }
        obj.get();
        return obj;
    }

    /**
     * See {@link #get(String, int)}
     */
    public List<T> get(int num) {
        List<T> ls = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            ls.add(get());
        }
        return ls;
    }

    @Override
    public T get(String name) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' from a '" + this.name +"' smallPool!");
        return get();
    }

    @Override
    public List<T> get(String name, int num) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' from a '" + this.name +"' smallPool!");
        return get(num);
    }

    @Override
    public void returnToPool(T obj) {
        obj.put();
        pool.add(obj);
    }

    @Override
    public SmallPool<T> getSmallPool(String name) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' smallPool, but this is a '" + this.name +"' smallPool!");

        return this;
    }
}
