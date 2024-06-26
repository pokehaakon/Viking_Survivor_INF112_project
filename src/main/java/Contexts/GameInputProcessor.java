package Contexts;

import InputProcessing.DefaultInputProcessor;
import InputProcessing.KeyStates;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;

import java.util.function.Function;

public class GameInputProcessor extends DefaultInputProcessor {
    final private Context context;
    final private Camera camera;
    final private KeyStates keyStates;
    final private Function<Float, Float> zoomHandler;
    private float zoom = 0;

    public GameInputProcessor(Context context, Camera camera, KeyStates keyStates, Function<Float, Float> zoomHandler) {
        this.context = context;
        this.camera = camera;
        this.keyStates = keyStates;
        this.zoomHandler = zoomHandler;
        zoom = zoomHandler.apply(zoom);
        zoomHandler.apply(zoom);
    }

    @Override
    public boolean keyDown(int keycode) {
        return switch (keycode) {
            case Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D -> keyStates.setInputKey(keycode);
            case Input.Keys.ESCAPE -> {keyStates.setInputKey(keycode); System.exit(0); yield true;}
            case Input.Keys.P -> {context.getContextualInputProcessor().setContext("PAUSEMENU"); yield true;}
            default -> false;
        };
    }

    @Override
    public boolean keyUp(int keycode) {

        return switch (keycode) {
            case Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D -> keyStates.unsetInputKey(keycode);
            default -> false;
        };
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.MIDDLE) {
            zoom = 1f;
            zoomHandler.apply(zoom);
            camera.viewportHeight = Gdx.graphics.getHeight() * zoom;
            camera.viewportWidth = Gdx.graphics.getWidth() * zoom;
            return true;
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        if (amountY > 0 && zoom < 2) {
            zoom *= 1.25f;
        }
        if (amountY < 0 && zoom > 0.01f) {
            zoom /= 1.25f;
        }
        zoomHandler.apply(zoom);
        camera.viewportHeight = Gdx.graphics.getHeight() * zoom;
        camera.viewportWidth = Gdx.graphics.getWidth() * zoom;

        return true;
    }
}
