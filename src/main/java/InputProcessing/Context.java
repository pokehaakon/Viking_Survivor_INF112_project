package InputProcessing;

import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;
import org.apache.maven.surefire.shared.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Context {
    final private Map<Pair<Integer, KeyEvent>, Consumer<Integer>> keyActions;
    final private Map<Pair<Integer, MouseEvent>, BiConsumer<Integer, Integer>> mouseActions;
    final private ContextName contextName;

    public Context(ContextName name) {

        contextName = name;

        keyActions = new HashMap<>();
        mouseActions = new HashMap<>();
    }

    private <T, E> void addTAction(int keycode, E event, T action, Map<Pair<Integer, E>, T> map) {
        map.put(new ImmutablePair<>(keycode, event), action);
    }

    public void addAction(int keycode, KeyEvent event, Consumer<Integer> action) {
        addTAction(keycode, event, action, keyActions);
    }

    public void addAction(int keycode, MouseEvent event, BiConsumer<Integer, Integer> action) {
        if (event == MouseEvent.MOUSE_DRAGGED || event == MouseEvent.MOUSE_MOVED) {
            keycode = 0; //should be 0, as these events have no associated keycode
        }
        addTAction(keycode, event, action, mouseActions);
    }

    public ContextName getContextName() {
        return contextName;
    }


    public <E> boolean hasAction(Pair<Integer, E> e) {
        if (e.getRight() instanceof KeyEvent)
            return keyActions.containsKey(e);
        if (e.getRight() instanceof MouseEvent)
            return mouseActions.containsKey(e);

        return false;
    }

    public void doAction(Pair<Integer, KeyEvent> e, int p) {
        keyActions.get(e).accept(p);
    }

    public void doAction(Pair<Integer, MouseEvent> e, int p1, int p2) {
        mouseActions.get(e).accept(p1, p2);
    }

}
