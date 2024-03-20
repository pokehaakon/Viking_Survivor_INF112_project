package Actors.Enemy;

import Actors.Actor;
import Actors.ActorAction;
import Actors.Stats.EnemyStats;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Map;

public class Enemy extends Actor {

    public float knockBackResistance;

    private final EnemyStats stats;



    public Enemy(Body body, Texture sprite, float scale, EnemyStats stats) {
        super(body, sprite, scale);
        this.stats = stats;
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();

        velocityVector = new Vector2();
    }



    @Override
    public void update(){


    }

//    public void chase(Actor player){
//        Vector2 playerPos = player.getBody().getWorldCenter();
//        velocityVector.add(playerPos).sub(body.getWorldCenter());
//
//        move();
//
//    }
}
