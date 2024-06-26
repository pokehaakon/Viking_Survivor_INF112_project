package InputProcessing;

import Contexts.Context;
import Contexts.ContextFactory;
import com.badlogic.gdx.InputProcessor;

import java.util.function.Function;

public class ContextualInputProcessor implements InputProcessor {
    private Context currentContext;
    private InputProcessor currentInputProcessor;
    private final ContextFactory contextFactory;



    public ContextualInputProcessor(Function<ContextualInputProcessor, ContextFactory> factoryFactory) {
        contextFactory = factoryFactory.apply(this);
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
        currentInputProcessor = currentContext.getInputProcessor();
        currentContext.resume();
    }

    public void removeContext(String c) {
        contextFactory.deleteContext(c);
    }

    @Override
    public boolean keyDown(int keycode) {
        return currentInputProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return currentInputProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return currentInputProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return currentInputProcessor.touchDown(screenX, screenY, pointer, button);}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return currentInputProcessor.touchUp(screenX, screenY, pointer, button);}

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {return currentInputProcessor.touchCancelled(screenX, screenY, pointer, button);}

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return currentInputProcessor.touchDragged(screenX, screenY, pointer);}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {return currentInputProcessor.mouseMoved(screenX, screenY);}

    @Override
    public boolean scrolled(float amountX, float amountY) {return currentInputProcessor.scrolled(amountX, amountY);}

    public void dispose(){
        contextFactory.dispose();
    }
}
