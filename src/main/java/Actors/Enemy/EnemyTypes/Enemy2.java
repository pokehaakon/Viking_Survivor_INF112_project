package Actors.Enemy.EnemyTypes;

import Actors.Coordinates;
import Actors.Enemy.Enemy;
import Actors.Stats;

public class Enemy2 extends Enemy {

    public Enemy2(int x, int y, Stats stats){
        super(stats);
        enemyType = EnemyType.ENEMY2;
        initialize("img_1.png", x, y);
    }

}
