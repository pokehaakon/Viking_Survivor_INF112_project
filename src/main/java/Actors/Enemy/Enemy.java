package Actors.Enemy;

import Actors.Actor;
import Actors.Enemy.EnemyTypes.EnemyState;
import Actors.Enemy.EnemyTypes.EnemyType;
import Actors.Stats;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.Objects;

public abstract class Enemy extends Actor {

    private float initDeltaX;
    private float initDeltaY;

    private boolean initMovement;
    public Stats stats;

    public EnemyType enemyType;
    public EnemyState state;

    public static final int SWARM_SPEED_MULTIPLIER = 2;

    public Enemy(Stats stats) {
        super(stats);
        state = EnemyState.SOLO;
        initMovement = false;
    }

    @Override
    public Body getBody() {
        return null;
    }


    @Override
    public void attack(Actor actor) {
        if (!initMovement) {
            initDeltaX = actor.x - x;
            initDeltaY = actor.y - y;
            float length = (float)Math.sqrt(initDeltaX * initDeltaX + initDeltaY * initDeltaY);
            if (length != 0) {
                initDeltaX /= length;
                initDeltaY /= length;
           }
            initMovement = true;
        }
        moveTowardsPosition(actor.x, actor.y);
    }



    //write swarm member test: when does it get the label

    public void moveTowardsPosition(int targetX, int targetY) {
        float deltaX = targetX - this.x;
        float deltaY = targetY - this.y;

        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (length != 0) {
            deltaX /= length;
            deltaY /= length;
        }

        if (state == EnemyState.SWARM_MEMBER) {
            x +=  (int)(initDeltaX * speedX);
            y +=  (int)(initDeltaY * speedY);
        }

        else {
            x += (int)(deltaX * speedX);
            y +=  (int)(deltaY * speedY);
        }
    }

    public void makeSwarmMember() {
        state = EnemyState.SWARM_MEMBER;
        speedX *= SWARM_SPEED_MULTIPLIER;
        speedY *= SWARM_SPEED_MULTIPLIER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enemy enemy = (Enemy) o;
        return Float.compare(initDeltaX, enemy.initDeltaX) == 0 && Float.compare(initDeltaY, enemy.initDeltaY) == 0 && initMovement == enemy.initMovement && Objects.equals(stats, enemy.stats) && enemyType == enemy.enemyType && state == enemy.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initDeltaX, initDeltaY, initMovement, stats, enemyType, state);
    }



    /**
     * Determined by inputs. Enemy moves so it appears as the player moves.
     * @param actor
     */
    public void move(Actor actor) {
        x -= actor.speedX;
        y -= actor.speedY;

    }
}
