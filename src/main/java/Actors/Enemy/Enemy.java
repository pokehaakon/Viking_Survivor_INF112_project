package Actors.Enemy;

import Actors.Actor;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy extends Actor implements IEnemy {

    public Enemy(String name) {
        super(name);
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




}
