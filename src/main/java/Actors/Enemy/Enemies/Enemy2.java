package Actors.Enemy.Enemies;

import Actors.Enemy.Enemy;

public class Enemy2 extends Enemy {


    public Enemy2(int x, int y) {
        super(x, y);
        init("img.png", x, y);

        HP = 100;
        speed = 1;
        damage = 6;

    }

    public Enemy2() {
        super();
        init("img.png", Enemy.DEFAULT_X, Enemy.DEFAULT_Y);
    }

    @Override
    public void attack() {
        System.out.println("Enemy 2 attacks!");
    }
}
