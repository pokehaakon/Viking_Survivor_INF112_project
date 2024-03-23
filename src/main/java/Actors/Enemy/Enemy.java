package Actors.Enemy;

import Actors.Actor;
import Actors.Stats.EnemyStats;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends Actor{

    public float knockBackResistance;

    private EnemyStats stats;


    public LocationState locationState;


    public Enemy(Body body, Texture sprite, float scale, EnemyStats stats) {
        super(body, sprite, scale);
        this.stats = stats;

        // stats
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();

        velocityVector = new Vector2();

    }




    public enum LocationState {
        RIGHT_OF_CENTER, LEFT_OF_CENTER
    };




}
