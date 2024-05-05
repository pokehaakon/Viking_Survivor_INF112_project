package Tools;

import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class HashMapTool {

    /**
     * Takes a list of pairs and creates a map using the first value of each pair as the key, and the second value as the value
     */
    public static <V, K> Map<V, K> mapFromPairs(List<Pair<V, K>> pairs) {
        return pairs.stream().collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));
    }
}
