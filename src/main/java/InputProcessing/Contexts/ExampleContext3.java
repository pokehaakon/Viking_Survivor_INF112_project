package InputProcessing.Contexts;

import Actors.Enemy.Enemy;
import Actors.Enemy.EnemyFactory;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyEvent;
import Actors.Player.PlayerExample;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;

public class ExampleContext3 extends Context{

    PlayerExample player;

    SpriteBatch batch;

    Enemy enemy1;
    Enemy enemy2;
    public ExampleContext3(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        //se setupInputListener i GameContext


        player = new PlayerExample("test1");

        enemy1 = EnemyFactory.create("enemy1");
        enemy2 = EnemyFactory.create("enemy2");
        this.addAction(Input.Keys.W, KeyEvent.KEYDOWN, (x) -> {
            player.spriteRect.y += 10;
        });
        this.addAction(Input.Keys.S, KeyEvent.KEYDOWN, (x) -> {
            player.spriteRect.y -= 10;
        });
        this.addAction(Input.Keys.A, KeyEvent.KEYDOWN, (x) -> {
            player.spriteRect.x -= 10;
        });
        this.addAction(Input.Keys.D, KeyEvent.KEYDOWN, (x) -> {
            player.spriteRect.x += 10;
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
        enemy1.draw(batch);
        enemy2.draw(batch);

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
