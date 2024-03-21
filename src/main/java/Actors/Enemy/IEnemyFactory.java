package Actors.Enemy;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public interface IEnemyFactory {


    /**
     *
     * @param type
     * @return a enemy with a given start position
     */
    Enemy createEnemyType(String type, Vector2 pos);

    /**
     *
     * @param count
     * @param type
     * @return a desired amount of enemies
     */
    List<Enemy> createEnemies(int count, String type, List<Vector2> startPoints);

    /**
     *
     * @param count
     * @return desired number of random enemies
     */
    List<Enemy> createRandomEnemies(int count, List<Vector2> startPoints);


    /**
     *
     * @param numMembers
     * @param enemyType
     * @param swarmType
     * @param startPoints
     * @return a swarm: a list of enemies with the state SWARM_MEMBER
     */
    List<Enemy> createSwarm(int numMembers, String enemyType, SwarmType swarmType, List<Vector2> startPoints);


}
