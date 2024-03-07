package Actors.Enemy;

import Actors.Coordinates;
import Actors.Enemy.EnemyTypes.SwarmType;
import Actors.Player.PlayerExample;
import Actors.Stats;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IEnemyFactory {


    /**
     *
     * @param type
     * @param x
     * @param y
     * @return a enemy with a given start position
     */
    Enemy createEnemyType(String type, float x, float y);

    /**
     *
     * @param count
     * @param type
     * @return a desired amount of enemies
     */
    List<Enemy> createEnemies(int count, String type);

    /**
     *
     * @param count
     * @return desired number of random enemies
     */
    List<Enemy> createRandomEnemies(int count);

    /**
     *
     * @param numMembers
     * @param enemyType
     * @param swarmType
     * @return creates a swarm of enemies
     */
    Swarm createSwarm(int numMembers, String enemyType, SwarmType swarmType);


}
