package InputProcessing;

import com.badlogic.gdx.Input;
import org.apache.maven.surefire.shared.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ContextFactory {
    private final Map<ContextName, Context> createdContexts;
    public ContextFactory() {
        createdContexts = new HashMap<>();
    }

    /**
     * Returns the 'Context' if it exists, else return 'null'
     * @param contextName    The 'ContextName' for the context
     * @return The context associated with 'contextName'
     *
    */
    public Context getContext(ContextName contextName) {
        if (createdContexts.containsKey(contextName)) {
            return createdContexts.get(contextName);
        }
        return null;
    }

    /**
     * Creates a new context or gives , with the given actions in consumerActions and biConsumerActions
     * @param contextName
     * @param consumerActions
     * @param biConsumerActions
     * @return the newly created context
     */
    private Context createContext(ContextName contextName, List<Triple<Integer, KeyEvent, Consumer<Integer>>> consumerActions, List<Triple<Integer, MouseEvent, BiConsumer<Integer, Integer>>> biConsumerActions) {
        if (createdContexts.containsKey(contextName)) {
            return createdContexts.get(contextName);
        }
        Context c = new Context(contextName);
        createdContexts.put(contextName, c);
        addActionsToContext(c, consumerActions, biConsumerActions);

        return createdContexts.get(contextName);
    }

    /**
     * Creates a new context, or gives the one already created
     * @param contextName
     * @return context with name 'contextName'
     */
    private Context createContext(ContextName contextName) {
        if (createdContexts.containsKey(contextName)) {
            return createdContexts.get(contextName);
        }
        Context c = new Context(contextName);
        createdContexts.put(contextName, c);
        return createdContexts.get(contextName);
    }

    private void addActionsToContext(Context c, List<Triple<Integer, KeyEvent, Consumer<Integer>>> consumerActions, List<Triple<Integer, MouseEvent, BiConsumer<Integer, Integer>>> biConsumerActions) {
        for (Triple<Integer, KeyEvent, Consumer<Integer>> a : consumerActions) {
            c.addAction(a.getLeft(), a.getMiddle(), a.getRight());
        }

        for (Triple<Integer, MouseEvent, BiConsumer<Integer, Integer>> a : biConsumerActions) {
            c.addAction(a.getLeft(), a.getMiddle(), a.getRight());
        }
    }


    public void setupGameContext(KeyStates keyStates) {
        //this should be private... Need to fix later
        Context c = createContext(ContextName.GAME);

        c.addAction(Input.Keys.W, KeyEvent.KEYDOWN, keyStates::setInputKey);
        c.addAction(Input.Keys.A, KeyEvent.KEYDOWN, keyStates::setInputKey);
        c.addAction(Input.Keys.S, KeyEvent.KEYDOWN, keyStates::setInputKey);
        c.addAction(Input.Keys.D, KeyEvent.KEYDOWN, keyStates::setInputKey);
        //c.addAction(Input.Keys.ESCAPE, KeyEvent.KEYDOWN, keyStates::setInputKey);
        c.addAction(Input.Keys.ESCAPE, KeyEvent.KEYDOWN, (x) -> {keyStates.setInputKey(Input.Keys.ESCAPE);System.exit(0);});

        c.addAction(Input.Keys.W, KeyEvent.KEYUP, keyStates::unsetInputKey);
        c.addAction(Input.Keys.A, KeyEvent.KEYUP, keyStates::unsetInputKey);
        c.addAction(Input.Keys.S, KeyEvent.KEYUP, keyStates::unsetInputKey);
        c.addAction(Input.Keys.D, KeyEvent.KEYUP, keyStates::unsetInputKey);

        c.addAction(Input.Buttons.LEFT, MouseEvent.MOUSE_CLICKED, (x, y) -> System.out.println("CLICKED -> " + x + ", " + y));
        c.addAction(Input.Buttons.LEFT, MouseEvent.MOUSE_UNCLICKED, (x, y) -> System.out.println("DROPPED -> " + x + ", " + y));
        c.addAction(0, MouseEvent.MOUSE_DRAGGED, (x, y) -> System.out.println("DRAGGED -> " + x + ", " + y));
        c.addAction(0, MouseEvent.MOUSE_MOVED, (x, y) -> System.out.println("MOVED -> " + x + ", " + y));
    }

}
