package Actors.Enemy.Enemies;

import Actors.Enemy.Enemy;

public class Enemy1 extends Enemy {


    public Enemy1(int x, int y) {
        super(x,y);

        init("img_1.png", x,y);

        HP = 100;
        speed = 3;
        damage = 5;

    }
    public Enemy1() {
        super();
        init("img_1.png", Enemy.DEFAULT_X,Enemy.DEFAULT_Y);
    }

    @Override
    public void attack() {
        System.out.println("Enemy 1 attacks!");
    }
}
