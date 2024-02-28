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

    void createRandomEnemies(int count);

    void removeDestroyedEnemies();

    int getEnemyTypeCount(EnemyType type);

    int getEnemyCount();

}
