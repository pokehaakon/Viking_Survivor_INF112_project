package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyEvent;
import Actors.Player.PlayerExample;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ExampleContext2 extends Context {
    PlayerExample player;
    SpriteBatch batch;

    public ExampleContext2(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        //se setupInputListener i GameContext


       // player = new PlayerExample("test",100,100);

        this.addAction(Input.Keys.W, KeyEvent.KEYDOWN, (x) -> {
            player.hitBox.y += 10;
        });
        this.addAction(Input.Keys.S, KeyEvent.KEYDOWN, (x) -> {
            player.hitBox.y -= 10;
        });
        this.addAction(Input.Keys.A, KeyEvent.KEYDOWN, (x) -> {
            player.hitBox.x -= 10;
        });
        this.addAction(Input.Keys.D, KeyEvent.KEYDOWN, (x) -> {
            player.hitBox.x += 10;
        });


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        ScreenUtils.clear(Color.WHITE);
        batch.begin();

        player.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

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
