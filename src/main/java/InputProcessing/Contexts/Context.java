package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.Screen;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Context implements Screen {
    final private Map<Pair<Integer, ContextualInputProcessor.KeyEvent>, Consumer<Integer>> keyActions;
    final private Map<Pair<Integer, ContextualInputProcessor.MouseEvent>, BiConsumer<Integer, Integer>> mouseActions;
    final private String contextName;
    final private ContextualInputProcessor iProc;

    public Context(String name, ContextualInputProcessor iProc) {
        contextName = name;
        this.iProc = iProc;
        keyActions = new HashMap<>();
        mouseActions = new HashMap<>();
    }

    protected ContextualInputProcessor getInputProcessor() {
        return iProc;
    }

    private <T, E> void addTAction(int keycode, E event, T action, Map<Pair<Integer, E>, T> map) {
        map.put(new ImmutablePair<>(keycode, event), action);
    }

    /**
     * Add an action for a Keyboard event (KeyEvent) for the pair keyCode 'keycode' and KeyEvent 'event'.
     * When the event happens the action.accept(keycode) is run.
     * @param keycode the keycode of the keyboard key
     * @param event the type of event
     * @param action the action to be performed when a 'keycode, event' event is encountered.
     */
    public void addAction(int keycode, ContextualInputProcessor.KeyEvent event, Consumer<Integer> action) {
        addTAction(keycode, event, action, keyActions);
    }

    /**
     * Add an action for a Mouse event (MouseEvent) for the pair keyCode 'keycode' and MouseEvent 'event'
     * When the event happens the action.accept(x, y) is run, where (x,y) are the coordinates where the event happened.
     * @param keycode the keycode of the mouse button
     * @param event the type of event
     * @param action the action to be performed when a 'keycode, event' event is encountered.
     */
    public void addAction(int keycode, ContextualInputProcessor.MouseEvent event, BiConsumer<Integer, Integer> action) {
        if (event == ContextualInputProcessor.MouseEvent.MOUSE_DRAGGED || event == ContextualInputProcessor.MouseEvent.MOUSE_MOVED || event == ContextualInputProcessor.MouseEvent.MOUSE_SCROLLED) {
            keycode = 0; //should be 0, as these events have no associated keycode
        }
        addTAction(keycode, event, action, mouseActions);
    }

    /**
     *
     * @return Name of the context
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * Checks if the pair is bound to an action
     * @param e event pair to check
     * @return true if there exists an action for the given pair
     * @param <E> Either KeyEvent or MouseEvent
     */
    public <E> boolean hasAction(Pair<Integer, E> e) {
        if (e.getRight() instanceof ContextualInputProcessor.KeyEvent)
            return keyActions.containsKey(e);
        if (e.getRight() instanceof ContextualInputProcessor.MouseEvent)
            return mouseActions.containsKey(e);

        return false;
    }

    /**
     * executes the action bound to the (keycode, keyevent) event, with p as the parameter
     * @param e (keycode, keyevent) pair
     * @param p the input for the action to accept
     */
    public void doAction(Pair<Integer, ContextualInputProcessor.KeyEvent> e, int p) {
        keyActions.get(e).accept(p);
    }

    /**
     * executes the action bound to the (keycode, mouseevent) event, with p1 & p2 as the parameters
     * @param e (keycode, mouseevent) pair
     * @param p1 the first input for the action to accept
     * @param p2 the second input for the action to accept
     */
    public void doAction(Pair<Integer, ContextualInputProcessor.MouseEvent> e, int p1, int p2) {
        mouseActions.get(e).accept(p1, p2);
    }
}
