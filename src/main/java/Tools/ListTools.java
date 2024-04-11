package Tools;

import java.util.List;

public abstract class ListTools {
    public static <T> void compactList(List<T> ls) {
        int j = 0;
        for (int i = 0; i < ls.size(); i++) {
            T v = ls.get(i);
            if (v == null) continue;
            ls.set(j++, v);
        }
    }
}
