package InputProcessing.Contexts;

import Actors.Enemy.Enemy;
import Actors.Enemy.EnemyTypes.SwarmType;
import Actors.Enemy.Swarm;
import Actors.Stats;
import Actors.Enemy.EnemyFactory;
import InputProcessing.ContextualInputProcessor;
import Actors.Player.PlayerExample;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class IngmarsContext extends Context{

    private BitmapFont font;

    PlayerExample player;

    SpriteBatch batch;

    private long lastSpawnTime;

    private static final int SPAWN_TIME= 5000;
    private static final int SWARM_INTERVAL = 10000;

    private List<Enemy> enemies;
    private EnemyFactory enemyFactory;

    private List<Swarm> swarms;
    public IngmarsContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        //enemyFactory = new EnemyFactory();
        player = new PlayerExample("Bro", Stats.player());
        font = new BitmapFont();
        lastSpawnTime = 0;

        enemies = new ArrayList<>();
        swarms = new ArrayList<>();
        enemyFactory = new EnemyFactory();

    }

        @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.GREEN);
        batch.begin();

        player.draw(batch);

        for(Enemy enemy:enemies) {
            enemy.draw(batch);
        }
        batch.end();

        if (TimeUtils.millis() - lastSpawnTime > SPAWN_TIME) {
            enemies.addAll(enemyFactory.createRandomEnemies(10));

            enemies.add(enemyFactory.createSwarm(20,"enemy1", SwarmType.SQUARE));
            //enemies.add(enemyFactory.createSwarm(20,"enemy1", SwarmType.LINE));
            //enemies.addAll(EnemyFactory.createLineSwarm(10, "enemy1"));
            lastSpawnTime = TimeUtils.millis();
        }
        System.out.println(enemies.size());
        handleEnemies();
        handleInputs();
        System.out.println(player.speedX);
        System.out.println(player.speedY);

    }



    public void handleEnemies() {
        for (Enemy enemy : enemies) {
            enemy.attack(player);
            enemy.moveInRelationTo(player);
        }
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

    public void handleInputs() {


        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setSpeedX(Stats.player().speed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setSpeedX(-Stats.player().speed);
        } else {
            player.setSpeedX(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setSpeedY(Stats.player().speed);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setSpeedY(-Stats.player().speed);
        } else {
            player.setSpeedY(0);
        }
    }




}
