package GameObjects;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ObjectActions.Action;
import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import Tools.BodyTool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameObject<E extends Enum<E>> implements IGameObject<E> {

    protected Body body;

    protected float scale;
    protected Set<Action> actions;



    protected boolean destroyed = false;

    protected E type;

    protected BodyFeatures bodyFeatures;


    protected DirectionState directionState;

    protected AnimationHandler animationHandler;
    // for sprite
    public GameObject(E type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        this.animationHandler = animationHandler;
        this.bodyFeatures = bodyFeatures;
        this.scale = scale;
        this.type = type;

        actions = new HashSet<>();

    }
    /**
     * Defines an action for the actor to perform
     * @param action
     */
    public void setAction(Action action) {
        actions.add(action);

    }
    /**
     * The actor performs its actions
     */
    public void doAction(){
        for(Action action : actions) {
            action.act(this);
        }
    }


    @Override
    public void destroy() {
        destroyed = true;
    }

    public void draw(SpriteBatch batch, float elapsedTime) {
       animationHandler.getAnimationRenderer().draw(batch,elapsedTime,this);
    }



    public void setAnimationState(AnimationState state){
        animationHandler.setAnimationState(state);

    }


    public void setAnimation(AnimationState state) {
        animationHandler.getAnimationRenderer().setAnimation(state);


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
    public void revive() {
        destroyed = false;
    }

    @Override
    public void setType(E newType) {
        type = newType;
    }

    @Override
    public E getType() {
        return type;
    }

    @Override
    public void setPosition(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }

    @Override
    public void addToWorld(World world) {

        body = BodyTool.createBody(
                world,
                new Vector2(),
                bodyFeatures.shape(),
                bodyFeatures.filter(),
                bodyFeatures.density(),
                bodyFeatures.friction(),
                bodyFeatures.restitution(),
                bodyFeatures.isSensor(),
                bodyFeatures.type());
        body.setUserData(this);
    }

   @Override
    public void setBodyFeatures(BodyFeatures features) {
        bodyFeatures = features;
    }


    @Override
    public void setScale(float newScale) {
        scale = newScale;
    }

    public float getScale() {
        return scale;
    }


    public DirectionState getDirectionState() {
        return directionState;
    }



    public BodyFeatures getBodyFeatures() {
        return bodyFeatures;
    }


    @Override
    public void renderAnimations(AnimationLibrary animationLibrary) {
        // to avoid setting render to object which already has a render
        animationHandler.renderAnimations(animationLibrary);
    }

    public Map<AnimationState,String> getAnimations() {
        return animationHandler.getAnimationMap();
    }


    /**
     * Check if object is out of bounds - if the distance between a set position and object is over a certain threshold.
     * @param pos the position which acts as center of spawn circle
     */
    public boolean outOfBounds(Vector2 pos, double deSpawnRadius) {
        float dx = body.getPosition().x - pos.x;
        float dy =  body.getPosition().y - pos.y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return(dist > deSpawnRadius);
    }




}
