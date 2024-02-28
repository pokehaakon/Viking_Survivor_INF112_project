package Actors.Enemy.EnemyTypes;

import Actors.Coordinates;
import Actors.Enemy.Enemy;
import Actors.Stats;

public class Enemy1 extends Enemy {

    public Enemy1(int x, int y, Stats stats) {
        super(stats);
        enemyType = EnemyType.ENEMY1;
        initialize("img.png", x, y);
    }



}
