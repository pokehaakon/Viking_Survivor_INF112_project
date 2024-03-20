package Actors.Enemy;

import Actors.Actor;
import Actors.Stats.EnemyStats;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends Actor implements IEnemy{

    public float knockBackResistance;

    private EnemyStats stats;

    private EnemyState enemyState;



    // for swarm members
    private Vector2 swarmCenter;

    private Vector2 swarmDirection;

    private boolean movementStarted;



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
        swarmDirection = new Vector2();
        enemyState = EnemyState.SOLO;

        movementStarted = false;

    }

    public EnemyState getEnemyState() {
        return enemyState;
    }
    public void setEnemyState(EnemyState newState) {
        enemyState = newState;
    }



    @Override
    public void chase(Actor player) {
        Vector2 playerPos = player.getBody().getPosition();
        velocityVector.add(playerPos).sub(body.getWorldCenter());
        move();
    }

    @Override
    public void swarmStrike(Actor actor) {
        if(!movementStarted) {
            Vector2 playerPos = actor.getBody().getPosition();
            swarmDirection.add(playerPos).sub(swarmCenter);

            movementStarted = true;
        }
        resetVelocity();
        setVelocityVector(swarmDirection.x, swarmDirection.y);

        move();

    }


    public void setSwarmCenter(Vector2 pos) {swarmCenter = pos;
    }
    public Vector2 getSwarmCenter() {
        return swarmCenter;
    }



    public Vector2 getSwarmDirection() {
        return swarmDirection;
    }





}
