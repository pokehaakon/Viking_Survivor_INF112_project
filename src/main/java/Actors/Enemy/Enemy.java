package Actors.Enemy;

import Actors.Actor;
import Actors.Enemy.EnemyTypes.Sprites;
import Actors.Stats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Enemy extends Actor {

    public Enemy(Stats stats, String spriteName, float x, float y, float width, float height, Body body) {
        super(stats, body);
        initialize(spriteName, x, y, width, height);
    }
    public Enemy() {

    }


    @Override
    public void attack(Actor actor) {
        float deltaX = actor.x - this.x;
        float deltaY = actor.y - this.y;

        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (length != 0) {
            deltaX /= length;
            deltaY /= length;
        }
        moveTowardsPosition(deltaX, deltaY);
    }


    /**
     * Moves towards a x and y coordinate
     * @param deltaX
     * @param deltaY
     */
    public void moveTowardsPosition(float deltaX, float deltaY) {
        x += (deltaX * speedX);
        y +=  (deltaY * speedY);
    }

    /**
     * Moves enemy based on input. Makes it appears as though the actor is moving.
     * @param actor
     */
    public void moveInRelationTo(Actor actor) {
        x -= actor.speedX;
        y -= actor.speedY;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 p = body.getPosition();
        hitBox.x = p.x;
        hitBox.y = p.y;
        super.draw(batch);
    }
}
