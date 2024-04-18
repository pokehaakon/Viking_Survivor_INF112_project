package Tools;

import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EnumTools {
    public static <E extends Enum<E>> String[] enumToStrings(Class<E> e) {
        E[] values = e.getEnumConstants();
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = values[i].toString();
        }
        return strings;
    }

//    public static String[] stringValues() {
//        int len = EnemyType.values().length;
//        String[] strings = new String[len];
//        int i = 0;
//        for (EnemyType e : EnemyType.values()) {
//            strings[i++] = e.toString();
//        }
//        return strings;
//    }

    public abstract static class HashMapTool {

        public static <V, K> Map<V, K> mapFromPairs(List<Pair<V, K>> pairs) {
            return pairs.stream().collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));
        }
    }
}
