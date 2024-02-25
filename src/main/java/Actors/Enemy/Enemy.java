package Actors.Enemy;

import Actors.Actor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends Actor implements IEnemy {
    public int HP;
    public int speed;
    public int damage;
    public static final int DEFAULT_X = 200, DEFAULT_Y =  200;

    public Enemy(int x, int y) {
        super(x, y);
    }

    public Enemy() {
        super();
    }



    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }


    @Override
    public void attack() {

    }

    public void move() {

        spriteRect.x+=speed;
    }




}
