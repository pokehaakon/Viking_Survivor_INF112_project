package Actors.Enemy.Enemies;

import Actors.Enemy.Enemy;

public class Enemy2 extends Enemy {


    public Enemy2(String name) {
        super(name);
        init("img.png", 300, 400);

    }

    public Enemy2() {
        super();
        init("img.png", 300, 400);
    }

    @Override
    public void attack() {
        System.out.println("Enemy 2 attacks!");
    }
}
