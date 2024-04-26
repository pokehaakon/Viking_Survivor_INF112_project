package Tools;

import GameObjects.GameObject;
import GameObjects.IGameObject;
import GameObjects.Pool.ObjectPool;
import GameObjects.Pool.SmallPool;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public abstract class ListTools {
    public static <T> void compactList(List<T> ls) {
        int j = 0;
        for (int i = 0; i < ls.size(); i++) {
            T v = ls.get(i);
            if (v == null) continue;
            ls.set(j++, v);
        }
    }

    public static <T extends GameObject> void removeDestroyed(List<T> ls, ObjectPool<T> pool, boolean compress) {
        int i = 0;
        for (var obj : ls) {
            if (obj.isDestroyed()) {
                pool.returnToPool(obj);
            } else {
                ls.set(i++, obj);
            }
        }
        if (compress) ls.subList(i, ls.size()).clear();
    }

    public static <T extends GameObject> void removeDestroyed(List<T> ls, SmallPool<T> pool, boolean compress) {
        int i = 0;
        for (var obj : ls) {
            if (obj.isDestroyed()) {
                pool.returnToPool(obj);
            } else {
                ls.set(i++, obj);
            }
        }
        if (compress) ls.subList(i, ls.size()).clear();
    }

    public static String findPrefix(String prefix, List<String> strings) {
        return findPrefixOptional(prefix, strings).orElseThrow(() -> new NoSuchElementException("No string in list has prefix '" + prefix + "', in list: " + strings));
    }

    public static Optional<String> findPrefixOptional(String prefix, List<String> strings) {
        for (String string : strings) {
            if (string.startsWith(prefix)) return Optional.of(string.replaceFirst(prefix, ""));
        }
        return Optional.empty();
    }

    public static <T> Optional<T> getFirst(List<T> ls, Function<T, Boolean> f) {
        for (var e : ls) {
            if (f.apply(e)) return Optional.of(e);
        }
        return Optional.empty();
    }

    public static <T> List<T> getAll(List<T> ls, Function<T, Boolean> f) {
        List<T> ret = new ArrayList<>();
        for (var e : ls) {
            if (f.apply(e)) ret.add(e);
        }
        return ret;
    }

}
