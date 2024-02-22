package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyEvent;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import static Rendering.Shapes.makeRectangle;

public class ExampleContext extends Context {
    private final SpriteBatch batch;
    public ExampleContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;

        this.addAction(Input.Keys.P, KeyEvent.KEYDOWN, (x) -> this.getInputProcessor().setContext("GAME"));

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
        ScreenUtils.clear(Color.WHITE);

        batch.draw(makeRectangle(100, 100,10, 10, 10, 10, Color.BLACK, Color.BLUE), 100, 100);

        /*
        //
        for (Drawable d : thingsToDraw) {
            d.draw(batch);
        }
        */

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
