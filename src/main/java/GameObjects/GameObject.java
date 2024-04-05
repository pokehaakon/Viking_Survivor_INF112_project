package GameObjects;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.IGameObject;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.BodyTool;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashSet;
import java.util.Set;

public abstract class GameObject implements IGameObject {

    protected Body body;

    protected float scale;

    protected boolean destroyed = false;

    protected Texture currentSprite;

    protected String type;

    protected BodyFeatures bodyFeatures;




    public GameObject(String spritePath, BodyFeatures bodyFeatures, float scale) {
        currentSprite = new Texture(Gdx.files.internal(spritePath));
        this.bodyFeatures = bodyFeatures;
        this.scale = scale;
    }

    public GameObject() {
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
    }

   @Override
    public void setBodyFeatures(BodyFeatures features) {
        bodyFeatures = features;
    }


    @Override
    public void setScale(float newScale) {
        scale = newScale;
    }


}
