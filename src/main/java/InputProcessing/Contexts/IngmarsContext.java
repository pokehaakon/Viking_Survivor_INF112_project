package InputProcessing.Contexts;

import Actors.Enemy.EnemyTypes.EnemyType;
import Actors.Stats;
import Actors.Enemy.Enemy;
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

public class IngmarsContext extends Context{

    private BitmapFont font;

    PlayerExample player;

    SpriteBatch batch;
    private final EnemyFactory enemyFactory;
    private long lastSpawnTime;
    private long lastSwarmSpawnTime;

    private static final int SPAWN_TIME= 5000;
    private static final int SWARM_INTERVAL = 10000;


    public IngmarsContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        enemyFactory = new EnemyFactory();
        player = new PlayerExample("Bro", Stats.player());
        font = new BitmapFont();
        lastSpawnTime = 0;
        lastSwarmSpawnTime = 0;

    }

        @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.GREEN);
        batch.begin();

        drawInfo();

        player.draw(batch);
        for(Enemy enemy: enemyFactory.getCreatedEnemies()) {
            enemy.draw(batch);
        }

        batch.end();


        if (TimeUtils.millis() - lastSpawnTime > SPAWN_TIME) {
            enemyFactory.createRandomEnemies(5);
            //enemyFactory.createSwarm(20,EnemyType.ENEMY1);
            lastSpawnTime = TimeUtils.millis();

        }
        handleEnemies();
        handleInputs();
        System.out.println(player.speedX);
        System.out.println(player.speedY);

    }

    public void handleEnemies() {

        for (Enemy enemy : enemyFactory.getCreatedEnemies()) {
            enemy.attack(player);
            enemy.move(player);
            if (enemy.collision(player)) {
                player.HP -= enemy.damage;
                enemy.destroy();
            }

        }
        enemyFactory.removeDestroyedEnemies();
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
            player.setSpeedX(Stats.player().speedX);
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setSpeedX(-Stats.player().speedX);
        } else {
            player.setSpeedX(0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setSpeedY(Stats.player().speedY);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setSpeedY(-Stats.player().speedX);
        } else {
            player.setSpeedY(0);
        }
    }

    public void drawInfo() {
        font.getData().setScale(2);
        font.setColor(Color.BLACK);
        font.draw(batch,"Player HP: "+ Integer.toString(player.HP), 10,Gdx.graphics.getHeight()-10);
        font.draw(
                batch,
                "Currently "+ Integer.toString(enemyFactory.getEnemyTypeCount(EnemyType.ENEMY1))+" type 1 enemies alive",
                10,Gdx.graphics.getHeight()-50
        );
        font.draw(
                batch,
                "Currently total "+ enemyFactory.getCreatedEnemies().size(),
                10,Gdx.graphics.getHeight()-90
        );

        if(player.HP <= 0)
            font.draw(
                    batch,
                    "Player is dead!",
                    10,
                    Gdx.graphics.getHeight()-130
            );



    }


}
