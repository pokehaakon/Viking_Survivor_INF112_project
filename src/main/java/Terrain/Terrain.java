package Terrain;

import Actors.IGameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Terrain implements IGameObject {

    private boolean isDestroyed = false;

    private final Texture sprite;

    private final Body body;

    private final float scale;
    public Terrain(Body body, String spritePath, float scale) {
        this.body = body;
        this.scale = scale;
        sprite = new Texture(Gdx.files.internal(spritePath));
    }
    @Override
    public void destroy() {
        isDestroyed = true;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 p = body.getPosition();
        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
