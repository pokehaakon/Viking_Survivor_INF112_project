package Actors.Enemy;

import Actors.MockEnemyFactory;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Factories.EnemyFactory;
import GameObjects.Actors.Enemy.EnemyPool;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static GameObjects.Actors.Enemy.EnemyPool.ENEMY_TYPES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EnemyPoolTest {

    private static EnemyFactory mockEnemyFactory;

    private EnemyPool testPool;
    static World world;
    int poolSize;

    String enemy1;
    String enemy2;

    List<Queue<Enemy>> enemiesInPool;
    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {

            @Override
            public void create() {
                world =  new World(new Vector2(0,0), true);

            }

            @Override
            public void resize(int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void render() {
                // TODO Auto-generated method stub

            }

            @Override
            public void pause() {
                // TODO Auto-generated method stub

            }

            @Override
            public void resume() {
                // TODO Auto-generated method stub

            }

            @Override
            public void dispose() {
                // TODO Auto-generated method stub

            }};
        new HeadlessApplication(listener, config);


    }

    @BeforeEach
    void setup() {
        poolSize = 10;
        mockEnemyFactory = new MockEnemyFactory().get();
        testPool = new EnemyPool(world, poolSize,  mockEnemyFactory);

        enemiesInPool = new ArrayList<>();

        Map<String, Queue<Enemy>> poolMap = testPool.getEnemyPool();

        for(Map.Entry<String, Queue<Enemy>> entry : poolMap.entrySet() ) {
            Queue<Enemy> enemyList = entry.getValue();
            enemiesInPool.add(enemyList);
        }

        enemy1 = ENEMY_TYPES.get(0);
        enemy2 = ENEMY_TYPES.get(1);
    }

    @Test
    void bodyStateWhenInitialized() {
        for(Queue<Enemy> enemies : enemiesInPool) {
            for(int i = 0; i < enemies.size();i++) {
                // body should not be null
                assertNotNull(enemies.peek().getBody());
                // body should not be active
                assertFalse(enemies.poll().getBody().isActive());
            }
        }
    }

    @Test
    void activationAndDeactivation() {
        // activation
        List<Enemy> enemies = testPool.getEnemies(enemy1, 5);
        List<Enemy> randomEnemies = testPool.getRandomEnemies(4);
        for(Enemy enemy : enemies) {
            // body should be active
            assertTrue(enemy.getBody().isActive());
        }
        for(Enemy enemy : randomEnemies) {
            // body should be active
            assertTrue(enemy.getBody().isActive());
        }
        // destroying enemies
        for(Enemy enemy : enemies) {
            enemy.destroy();
        }
        for(Enemy enemy : randomEnemies) {
            enemy.destroy();
        }

        // deactivation
        for(Enemy enemy:enemies) {
            testPool.returnEnemy(enemy);
            // body should be inactive
            assertFalse(enemy.getBody().isActive());
            // enemy  should be revived
            assertFalse(enemy.isDestroyed());
        }
        // deactivation
        for(Enemy enemy:randomEnemies) {
            testPool.returnEnemy(enemy);
            // body should be inactive
            assertFalse(enemy.getBody().isActive());
            // enemy  should be revived
            assertFalse(enemy.isDestroyed());
        }
    }

    @Test
    void removingAndAdding() {
        int numberToRemove = 3;

        // removing enemies
        testPool.getEnemies(enemy1,numberToRemove);
        assertEquals(poolSize - numberToRemove, testPool.getEnemyPool().get(enemy1).size());

        // should remain unchanged for other enemy
        assertEquals(poolSize, testPool.getEnemyPool().get(enemy2).size());

        //adding the same number of enemies back
        for(int i = 0; i < numberToRemove; i++) {
            Enemy enemy = mockEnemyFactory.create(enemy1);
            enemy.addToWorld(world, new Vector2());
            testPool.returnEnemy(enemy);
        }

        assertEquals(poolSize, testPool.getEnemyPool().get(enemy1).size());


        // should remain unchanged for other enemy
        assertEquals(poolSize, testPool.getEnemyPool().get(enemy2).size());

    }

    @Test
    void correctInitialPoolSize() {
        for (Queue<Enemy> queue : enemiesInPool) {
            assertEquals(poolSize, queue.size());
        }
    }

    @Test
    void handleEmptyPool() {
        testPool.getEnemies(enemy1, poolSize);
        assertTrue(testPool.getEnemyPool().get(enemy1).isEmpty());

        // should still get active enemy bodies from empty pool
        for(Enemy enemy:testPool.getEnemies(enemy1,5)) {
            assertNotNull(enemy.getBody());
            assertTrue(enemy.getBody().isActive());
        }
    }


}