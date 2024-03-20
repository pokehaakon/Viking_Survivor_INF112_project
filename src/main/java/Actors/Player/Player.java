package Actors.Player;

import Actors.Actor;
import Actors.Stats.PlayerStats;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Actor {
    public Vector2 velocityVector;
    public int level;


    private final PlayerStats stats;

    public Player(Body body, Texture sprite, float scale, PlayerStats stats) {
        super(body, sprite, scale);
        this.stats = stats;
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        level = 1;
    }

    @Override
    public void update() {


    }


//    public void resetVelocity(){
//        velocityVector = new Vector2();
//    }

//    public void move(){
//        velocityVector.setLength(speed);
//        body.setLinearVelocity(velocityVector);
//    }
}
