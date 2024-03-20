package Actors;

import Actors.Enemy.Enemy;
import Actors.IGameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Actor implements IGameObject{

    protected float HP, speed, damage, armour;

    protected Body body;

    protected float scale;

    protected Texture sprite;

    protected ActorAction action;

    private boolean destroyed = false;

    protected Vector2 velocityVector;

    public Actor(Body body, Texture sprite, float scale) {
        this.body = body;
        this.scale = scale;
        this.sprite = sprite;
    }

    public void setAction(ActorAction action) {
        this.action = action;
    }

    public void step(){

        action.act(this);


    }

    @Override
    public void destroy() {
        destroyed = true;
    }


    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 p = body.getPosition();
        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

    public void resetVelocity(){
        velocityVector = new Vector2();
    }

    public void setVelocityVector(float x, float y) {
        velocityVector.x += x;
        velocityVector.y += y;
    }

    public void move(){
        velocityVector.setLength(speed);
        body.setLinearVelocity(velocityVector);
    }

    public void chase(Actor actor){
        Vector2 actorPos = actor.getBody().getWorldCenter();
        velocityVector.add(actorPos).sub(body.getWorldCenter());

        move();
    }

    public Vector2 getVelocityVector() {
        return velocityVector;
    }



}
