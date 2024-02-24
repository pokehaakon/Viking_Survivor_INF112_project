package Actors.Enemy;

import Actors.Enemy.Enemies.Enemy1;
import Actors.Enemy.Enemies.Enemy2;

public class EnemyFactory{

    public static Enemy create(String type) {
        if(type == null) {
            return null;
        }
        return switch (type.toUpperCase()) {
            case "ENEMY1" -> new Enemy1();
            case "ENEMY2" -> new Enemy2();
            default -> throw new IllegalArgumentException("Invalid enemy type");
        };
    }
}
