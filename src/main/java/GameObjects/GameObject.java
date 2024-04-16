package GameObjects;

import GameObjects.Animations.AnimationRendering.AnimationLibrary;
import GameObjects.Animations.AnimationRendering.GIFRender;
import GameObjects.Animations.AnimationRendering.SpriteRender;
import GameObjects.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import GameObjects.Animations.AnimationRendering.AnimationRender;
import Tools.BodyTool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;
import java.util.Objects;

public abstract class GameObject<E extends Enum<E>> implements IGameObject<E> {

    protected Body body;

    protected float scale;

    public boolean isGif = true;

    protected boolean destroyed = false;

    protected E type;

    protected BodyFeatures bodyFeatures;

    protected AnimationState animationState;

    protected DirectionState directionState;



    protected AnimationRender animationRender;

    protected Map<AnimationState,String> animations;
    // for sprite
    public GameObject(E type,Map<AnimationState,String> animations, BodyFeatures bodyFeatures, float scale) {
        this.animations = animations;
        //this.render = render;
        this.bodyFeatures = bodyFeatures;
        this.scale = scale;
        this.type = type;
    }


    @Override
    public void destroy() {
        destroyed = true;
    }

    public void draw(SpriteBatch batch, float elapsedTime) {
        if(animationRender == null) {
            throw new NullPointerException("Remember to call renderAnimations()!");
        }
        animationRender.draw(batch, elapsedTime, this);
    }



    public void setAnimationState(AnimationState state){
        animationState = state;

    }


    public void setAnimation(AnimationState state) {
        animationRender.setAnimation(state);


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

    public AnimationState getAnimationState() {
        return animationState;
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
        if(Objects.isNull(animationRender)) {
            if(isGif) {
                this.animationRender = new GIFRender<>(animationLibrary,animations);
            }
            else {
                this.animationRender = new SpriteRender(animationLibrary,animations);
            }
            this.animationRender.setAnimation(animationState);
        }

    }

    public Map<AnimationState,String> getAnimations() {
        return animations;
    }




}
