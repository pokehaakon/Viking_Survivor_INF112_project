package InputProcessing.Contexts;

import Actors.Enemy.EnemyTypes.EnemyState;
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

    private static final int SPAWN_TIME= 2000;
    private static final int SWARM_INTERVAL = 10000;


    public IngmarsContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        enemyFactory = new EnemyFactory();
        player = new PlayerExample("Bro", Stats.player());
        font = new BitmapFont();
        lastSpawnTime = 0;
        lastSwarmSpawnTime = 0;

        //enemyFactory.createSwarm(20, EnemyType.ENEMY1);
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
        handleEnemies();

        batch.end();
        handleInputs();

        if (TimeUtils.millis() - lastSpawnTime > SPAWN_TIME) {
            //enemyFactory.createRandomEnemies(5);
            enemyFactory.createSwarm(20,EnemyType.ENEMY1);
            lastSpawnTime = TimeUtils.millis();

        }
    }

    public void handleEnemies(){
        for(Enemy enemy: enemyFactory.getCreatedEnemies()) {
            enemy.draw(batch);
            enemy.attack(player);

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
        for(Enemy enemy:enemyFactory.getCreatedEnemies()) {

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                enemy.x -= (int) player.speed;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                enemy.x += (int) player.speed;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                enemy.y -= (int)player.speed;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                enemy.y += (int)player.speed;
            }
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
                "Currently "+ Integer.toString(enemyFactory.getEnemyTypeCount(EnemyType.ENEMY2))+" type 2 enemies alive",
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
