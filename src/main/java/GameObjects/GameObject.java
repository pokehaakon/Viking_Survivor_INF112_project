package GameObjects;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import Tools.BodyTool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;

public abstract class GameObject implements IGameObject {

    protected Body body;

    protected float scale;


    protected boolean destroyed = false;

    protected String name;

    protected BodyFeatures bodyFeatures;


    protected DirectionState directionState;

    protected AnimationHandler animationHandler;
    // for sprite
    public GameObject(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        this.animationHandler = animationHandler;
        this.bodyFeatures = bodyFeatures;
        this.scale = scale;
        this.name = name;
    }


    @Override
    public void destroy() {
        destroyed = true;
    }

    @Override
    public void draw(SpriteBatch batch, long frame) {
       animationHandler.getAnimationRenderer().draw(batch, frame,this);
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

//    @Override
//    public void setType(E newType) {
//        type = newType;
//    }

    @Override
    public String getType() {
        return name;
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

//    public Map<AnimationState,String> getAnimations() {
//        return animationHandler.getAnimationMap();
//    }




}
