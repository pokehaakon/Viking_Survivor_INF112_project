package Actors.Enemy;

import Actors.Enemy.EnemyTypes.EnemyType;

import java.util.List;

public interface IEnemyFactory {

    /**
     *
     * @return list of enemies
     */
    List<Enemy> getCreatedEnemies();

    /**
     *
     * @param type the type of enemy to create
     * @return desired enemy object
     */
    Enemy createEnemyType(EnemyType type,int x, int y);


    /**
     * creates a desired number of random enemies
     * @param count
     */
    void createRandomEnemies(int count);

    /**
     * removing enemies who as isDestroyed = true
     */
    void removeDestroyedEnemies();

    /**
     * returns  number of active enemies of a specific type
     * @param type
     * @return
     */
    int getEnemyTypeCount(EnemyType type);


    /**
     *
     * @return totol number of active enemies
     */
    int getEnemyCount();

}
