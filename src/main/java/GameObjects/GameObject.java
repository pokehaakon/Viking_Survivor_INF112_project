package GameObjects;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import Tools.BodyTool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject implements IGameObject {
    private int ID;
    private Body body;
    private boolean destroyed = false;
    private boolean isMovingLeft;
    private BodyFeatures bodyFeatures;

    protected final AnimationHandler animationHandler;

    public final String name;

    public GameObject(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures) {
        this.animationHandler = animationHandler;
        this.bodyFeatures = bodyFeatures;
        this.name = name;

        isMovingLeft = false;
        ID = ObjectFactory.getUID();
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
        ID = ObjectFactory.getUID();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPosition(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }

    @Override
    public void addToWorld(World world) {
        if (body != null)
            throw new RuntimeException("Tried to create body for GameObject " + name + " when it already has a body");
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
        bodyFeatures = null;
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
    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    @Override
    public void setMovingLeft(boolean movingLeft) {
        this.isMovingLeft = movingLeft;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void put() {
        this.getBody().setActive(false);
        this.revive();
    }

    @Override
    public void get() {
        this.getBody().setActive(true);
    }

    public boolean outOfBounds(Actor centerActor, Vector2 boundSquare) {
        Vector2 dxdy = centerActor
                .getBody()
                .getPosition()
                .cpy()
                .sub(body.getPosition());

        return (Math.abs(dxdy.x) > boundSquare.x / 2 || Math.abs(dxdy.y) > boundSquare.y / 2);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

}
