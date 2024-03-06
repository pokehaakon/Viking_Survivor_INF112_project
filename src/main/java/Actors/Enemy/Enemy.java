package Actors.Enemy;

import Actors.Actor;
import Actors.Enemy.EnemyTypes.EnemyState;
import Actors.Stats;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends Actor {
    private float initDeltaX;
    private float initDeltaY;

    private boolean initMovement;
    public Stats stats;

    public EnemyState state;

    public static final int SWARM_SPEED_MULTIPLIER = 2;

    public Enemy(Stats stats, String spriteName, int x, int y) {
        super(stats);
        initialize(spriteName, x, y);
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

    private void moveTowardsPosition(int targetX, int targetY) {
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

}
