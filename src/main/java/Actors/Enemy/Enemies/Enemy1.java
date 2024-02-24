package Actors.Enemy.Enemies;

import Actors.Enemy.Enemy;

public class Enemy1 extends Enemy {

    public Enemy1(String name) {
        super(name);
        init("img_1.png", 500,100);

    }
    public Enemy1() {
        super();
        init("img_1.png", 500,100);

    }

    @Override
    public void attack() {
        System.out.println("Enemy 1 attacks!");
    }
}
