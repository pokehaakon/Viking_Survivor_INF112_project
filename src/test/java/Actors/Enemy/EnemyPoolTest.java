package Actors.Enemy;

import Actors.MockEnemyFactory;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.ObjectTypes.EnemyType;
import GameObjects.Factories.EnemyFactory;
import GameObjects.ObjectPool;
import GameObjects.SmallPool;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EnemyPoolTest {

    private static EnemyFactory mockEnemyFactory;

    private ObjectPool<Enemy, EnemyType> testPool;

    List<EnemyType> objectTypes = List.of(EnemyType.values());
    static World world;
    int poolSize;

    EnemyType enemy1;
    EnemyType enemy2;

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
        testPool = new ObjectPool<>(world, mockEnemyFactory, objectTypes, poolSize);

        enemiesInPool = new ArrayList<>();

        Map<EnemyType, SmallPool<Enemy>> poolMap = testPool.getObjectPool();

        for(Map.Entry<EnemyType, SmallPool<Enemy>> entry : poolMap.entrySet() ) {
            SmallPool<Enemy> enemyList = entry.getValue();
            enemiesInPool.add(enemyList.getPool());
        }

        enemy1 = objectTypes.get(0);
        enemy2 = objectTypes.get(1);
    }

    @Test
    void bodyStateWhenInitialized() {
        for(Queue<Enemy> enemies : enemiesInPool) {
            for(int i = 0; i < enemies.size();i++) {
                // body should not be null
                assertNotNull(enemies.peek().getBody());
                // body should not be active
                assertFalse(enemies.peek().getBody().isActive());
            }
        }
    }

    @Test
    void activationAndDeactivation() {
        // activation
        List<Enemy> enemies = testPool.get(enemy1, 5);
        List<Enemy> randomEnemies = testPool.getRandom(4);
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
            testPool.returnToPool(enemy);
            // body should be inactive
            assertFalse(enemy.getBody().isActive());
            // enemy  should be revived
            assertFalse(enemy.isDestroyed());
        }
        // deactivation
        for(Enemy enemy:randomEnemies) {
            testPool.returnToPool(enemy);
            // body should be inactive
            assertFalse(enemy.getBody().isActive());
            // enemy  should be revived
            assertFalse(enemy.isDestroyed());
        }
    }

    @Test
    void removingAndAdding() {
        int numberToRemove = 3;
        //System.out.println(testPool.getObjectPool().get(enemy1).size());
        // removing enemies
        testPool.get(enemy1,numberToRemove);
        //System.out.println(testPool.getObjectPool().get(enemy1).size());
        assertEquals(poolSize - numberToRemove, testPool.getObjectPool().get(enemy1).size());

        // should remain unchanged for other enemy
        assertEquals(poolSize, testPool.getObjectPool().get(enemy2).size());

        //adding the same number of enemies back
        for(int i = 0; i < numberToRemove; i++) {
            Enemy enemy = mockEnemyFactory.create(enemy1);
            enemy.addToWorld(world);
            testPool.returnToPool(enemy);

        }

        assertEquals(poolSize, testPool.getObjectPool().get(enemy1).size());


        // should remain unchanged for other enemy
        assertEquals(poolSize, testPool.getObjectPool().get(enemy2).size());

    }

    @Test
    void correctInitialPoolSize() {
        for (Queue<Enemy> queue : enemiesInPool) {
            assertEquals(poolSize, queue.size());
        }
    }

    @Test
    void handleEmptyPool() {
        testPool.get(enemy1, poolSize);
        assertTrue(testPool.getObjectPool().get(enemy1).isEmpty());

        // should still get active enemy bodies from empty pool
        for(Enemy enemy:testPool.get(enemy1,5)) {
            assertNotNull(enemy.getBody());
            assertTrue(enemy.getBody().isActive());
        }
    }


}