package Actors.Enemy;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface IEnemyFactory {


    /**
     * Generate an Enemy of a wanted type and start position
     * @param type type of enemy to generate
     * @return an Enemy object
     */
    Enemy createEnemyType(String type);

    /**
     * Create multiple enemies of wanted type
     * @param count number of enemies to create
     * @param type type of enemy to generate
     * @return List of Enemy objects
     */
    List<Enemy> createEnemies(int count, String type);






}
