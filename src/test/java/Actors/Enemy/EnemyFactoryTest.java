package Actors.Enemy;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyFactoryTest {
    EnemyFactory factory;

    @BeforeEach
    void initialize() {
        factory = new EnemyFactory(null);
    }

    @Test
    void invalidEnemyType() {

        assertThrows(NullPointerException.class, () -> {
            factory.createEnemyType(null , new Vector2(),0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            factory.createEnemyType("hello", new Vector2(),0);
        });
    }

    //to test: correct enemytype, correct position, correct number of enemies etc
}