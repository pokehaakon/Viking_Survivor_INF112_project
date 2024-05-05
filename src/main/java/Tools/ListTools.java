package Tools;

import GameObjects.GameObject;
import Tools.Pool.IPool;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public abstract class ListTools {
    /**
     * Takes a list, and moves all values that are not null to the left,
     * so that only the last non-null value has a null to its right.
     */
    public static <T> void compactList(List<T> ls) {
        int j = 0;
        for (int i = 0; i < ls.size(); i++) {
            T v = ls.get(i);
            if (v == null) continue;
            ls.set(j++, v);
        }
        for (;j < ls.size(); j++) {
            ls.set(j, null);
        }
    }

    /**
     * Returns all destroyed GameObjects in 'ls' to the given pool.
     * shrinks the list if 'compress' is true
     */
    public static <T extends GameObject> void removeDestroyed(List<T> ls, IPool<T> pool, boolean compress) {
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

    /**
     * Tries to find a string in 'strings' with prefix 'prefix'
     * if none is found throws a NoSuchElementException
     */
    public static String findPrefix(String prefix, List<String> strings) {
        return findPrefixOptional(prefix, strings).orElseThrow(() -> new NoSuchElementException("No string in list has prefix '" + prefix + "', in list: " + strings));
    }

    /**
     * Tries to find a string in 'strings' with prefix 'prefix'
     * If any is found return an optional of the string, else returns an empty optional
     */
    public static Optional<String> findPrefixOptional(String prefix, List<String> strings) {
        for (String string : strings) {
            if (string.startsWith(prefix)) return Optional.of(string.replaceFirst(prefix, ""));
        }
        return Optional.empty();
    }

    /**
     * Returns the first element 'e' of 'ls' where 'f.apply(e)' is true
     * If an element is found returns an optional of the element, else returns an empty optional
     */
    public static <T> Optional<T> getFirst(List<T> ls, Function<T, Boolean> f) {
        for (var e : ls) {
            if (f.apply(e)) return Optional.of(e);
        }
        return Optional.empty();
    }

    /**
     * Return all elements 'e' of 'ls' where 'f.apply(e)' is true
     */
    public static <T> List<T> getAll(List<T> ls, Function<T, Boolean> f) {
        List<T> ret = new ArrayList<>();
        for (var e : ls) {
            if (f.apply(e)) ret.add(e);
        }
        return ret;
    }

}
