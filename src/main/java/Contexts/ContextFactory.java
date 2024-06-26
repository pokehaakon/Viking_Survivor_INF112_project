package Contexts;

import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class ContextFactory implements Disposable {
    private final Map<String, Context> createdContexts;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final ContextualInputProcessor iProc;
    public ContextFactory(SpriteBatch batch, OrthographicCamera camera, ContextualInputProcessor iProc) {
        this.batch = batch;
        this.iProc = iProc;
        this.camera = camera;
        createdContexts = new HashMap<>();
    }

    /**
     * Returns the 'Context' if it exists, else return 'null'
     * @param contextName    The 'ContextName' for the context
     * @return The context associated with 'contextName'
     *
    */
    public Context getContext(String contextName) {
        if (createdContexts.containsKey(contextName)) {
            return createdContexts.get(contextName);
        }
        Context c = spawnContext(contextName);
        createdContexts.put(contextName, c);
        return c;
    }

    private Context spawnContext(String contextName) { //this is the only place where context instances are bound to contextNames!
        return switch (contextName) {
            case "GAME" -> new GameContext(batch, camera, iProc);
            case "MAINMENU" -> new MainMenuContext(contextName, batch, iProc);
            case "SETTINGS" -> new SettingsContext(contextName, batch, iProc);
            default -> throw new RuntimeException("ContextFactory cannot make context: " + contextName);
        };
    }

    @Override
    public void dispose() {
        for (Context c : createdContexts.values()) {
            c.dispose();
        }
    }

    /**
     * Deletes the context with the name 'contextName' if it exists.
     * Also runs .dispose() on the context.
     * @param contextName the name of context to delete
     */
    public void deleteContext(String contextName) {
        if (!createdContexts.containsKey(contextName)) return;
        Context c = createdContexts.get(contextName);
        c.dispose();
        createdContexts.remove(contextName);
    }
}
