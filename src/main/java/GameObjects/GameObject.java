package GameObjects;

import GameObjects.Factories.ExperimentalFactory;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import Tools.BodyTool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Map;

public class GameObject implements IGameObject {
    private int ID;
    private Body body;
    private boolean destroyed = false;
    private DirectionState directionState;

    protected final AnimationHandler animationHandler;

    public final BodyFeatures bodyFeatures;
    public final String name;

    public GameObject(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures) {
        this.animationHandler = animationHandler;
        this.bodyFeatures = bodyFeatures;
        this.name = name;

        setDirectionState(DirectionState.RIGHT);
        ID = ExperimentalFactory.getUID();
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
    public void revive() {
        destroyed = false;
        ID = ExperimentalFactory.getUID();
    }

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
                bodyFeatures.type()
        );
        body.setUserData(this);
    }

    @Override
    public void draw(SpriteBatch batch, long frame) {
       animationHandler.getAnimationRenderer().draw(batch, frame,this);
    }

    @Override
    public void setAnimationState(AnimationState state){
        animationHandler.setAnimationState(state);
    }

    @Override
    public void setAnimation(AnimationState state) {
        animationHandler.getAnimationRenderer().setAnimation(state);
    }

    @Override
    public DirectionState getDirectionState() {
        return directionState;
    }

    @Override
    public void setDirectionState(DirectionState directionState) {
        this.directionState = directionState;
    }

    @Override
    public int getID() {
        return ID;
    }

}
