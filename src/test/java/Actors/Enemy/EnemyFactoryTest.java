package Actors.Enemy;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class EnemyFactoryTest {


    @Test
    void invalidEnemyType() {
        assertEquals(5, EnemyFactory.create(5,"Enemy1"));

        assertThrows(NullPointerException.class, () -> {
            EnemyFactory.create(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            EnemyFactory.create("hello");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            EnemyFactory.create(0, "Enemy1");
        });
    }

    //to test: correct enemytype, correct position, correct number of enemies etc
}