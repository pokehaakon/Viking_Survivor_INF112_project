package InputProcessing;

import InputProcessing.Contexts.Context;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

public class ContextualInputProcessor implements InputProcessor {
    private Context currentContext;
    private ContextFactory contextFactory;


    public ContextualInputProcessor(SpriteBatch batch){
        contextFactory = new ContextFactory(batch, this);
    };

    public ContextFactory getContextFactory() {
        return contextFactory;
    }

    public Context getCurrentContext() {
        return currentContext;
    }

    /**
     * Pauses the current context (if there is one) and switches to the context with name 'c'.
     * Runs resume on the context at the end.
     * @param c the name of the context to switch to.
     */
    public void setContext(String c) {
        if (currentContext != null) {
            currentContext.pause();
        }
        currentContext = contextFactory.getContext(c);
        currentContext.resume();
    }

    private boolean keyEvent(int keycode, KeyEvent e) {
        Pair<Integer, KeyEvent> event = new ImmutablePair<>(keycode, e);
        if (!currentContext.hasAction(event)) return false;
        currentContext.doAction(event, keycode);
        return true;
    }
    @Override
    public boolean keyDown(int keycode) {
        return keyEvent(keycode, KeyEvent.KEYDOWN);
    }

    @Override
    public boolean keyUp(int keycode) {
        return keyEvent(keycode, KeyEvent.KEYUP);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    private boolean mouseEvent(int x, int y, int button, MouseEvent e) {
        Pair<Integer, MouseEvent> event = new ImmutablePair<>(button, e);
        if (!currentContext.hasAction(event)) return false;
        currentContext.doAction(event, x, y);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return mouseEvent(screenX, screenY, button, MouseEvent.MOUSE_CLICKED);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return mouseEvent(screenX, screenY, button, MouseEvent.MOUSE_UNCLICKED);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return mouseEvent(screenX, screenY, 0, MouseEvent.MOUSE_DRAGGED); //button is ignored
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return mouseEvent(screenX, screenY, 0, MouseEvent.MOUSE_MOVED); //button is ignored
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void dispose(){
        contextFactory.dispose();
    }
}
