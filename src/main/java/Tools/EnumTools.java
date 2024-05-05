package Tools;

import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EnumTools {

    /**
     * Returns an array of all the string values of an enum
     */
    public static <E extends Enum<E>> String[] enumToStrings(Class<E> e) {
        E[] values = e.getEnumConstants();
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = values[i].toString();
        }
        return strings;
    }

}
