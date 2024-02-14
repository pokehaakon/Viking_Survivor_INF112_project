package InputProcessing;

import com.badlogic.gdx.InputProcessor;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ContextualInputProccessor implements InputProcessor {
    private Context currentContext;
    private final Map<ContextName, Context> contexts;

    private ContextFactory contextFactory;


    public ContextualInputProccessor(ContextFactory contextFactory){
        this.contextFactory = contextFactory;
        contexts = new HashMap<>();
    };


    public void setContext(ContextName c) {
        currentContext = contextFactory.getContext(c);
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
}
