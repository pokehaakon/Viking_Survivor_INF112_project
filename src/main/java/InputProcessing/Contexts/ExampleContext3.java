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
    private final EnemyFactory enemyFactory;

    public ExampleContext3(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        //se setupInputListener i GameContext

        enemyFactory = new EnemyFactory();

        player = new PlayerExample("test1", 100,100);

        for(int i = 0; i < 5; i++) {
            enemyFactory.createRandom();
        }


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
        for(Enemy enemy: enemyFactory.getEnemyList()) {
            enemy.draw(batch);
            enemy.move();
        }
        batch.end();

        System.out.println(enemyFactory.getEnemyList().size());

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
