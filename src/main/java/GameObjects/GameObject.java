package GameObjects;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.IGameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashSet;
import java.util.Set;

public abstract class GameObject implements IGameObject {

    protected Body body;

    protected float scale;

    private boolean destroyed = false;

    protected Texture currentSprite;

    protected String type;
    private Set<ActorAction> actions;

    public GameObject(Body body, float scale) {
        this.body = body;
        this.scale = scale;
        actions = new HashSet<>();

    }
    @Override
    public void destroy() {
        destroyed = true;
    }



    @Override
    public void draw(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        batch.draw(currentSprite,pos.x,pos.y, currentSprite.getWidth()*scale,  currentSprite.getHeight()*scale);

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
    public void setSprite(Texture texture) {
        currentSprite = texture;
    }

    @Override
    public void revive() {
        destroyed = false;
    }

    @Override
    public void setType(String newType) {
        type = newType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setPosition(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }
}
