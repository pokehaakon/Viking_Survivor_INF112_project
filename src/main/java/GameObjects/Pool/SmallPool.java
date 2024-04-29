package GameObjects.Pool;

import GameObjects.GameObject;
import com.badlogic.gdx.physics.box2d.World;

import java.util.*;
import java.util.function.Supplier;


public final class SmallPool<T extends GameObject> implements IPool<T> {
    private final World world;
    private final Supplier<T> factory;
    private final String name;
    private final Queue<T> pool = new ArrayDeque<>();

    public SmallPool(World world, Supplier<T> factory, String name) {
        this.world = world;
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
            obj.addToWorld(world);
        } else {
            obj = pool.poll();
        }
        obj.getBody().setActive(true);
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
        obj.getBody().setActive(false);
        obj.revive();
        pool.add(obj);
    }

    @Override
    public SmallPool<T> getSmallPool(String name) {
        if (!Objects.equals(name, this.name))
            throw new IllegalArgumentException("Tried to get a '" + name + "' smallPool, but this is a '" + this.name +"' smallPool!");

        return this;
    }
}
