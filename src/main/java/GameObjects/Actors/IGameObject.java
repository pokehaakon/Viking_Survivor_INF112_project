package GameObjects.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Interface for all game objects
 * (all objects rendered on the screen in the game)
 */
public interface IGameObject {

    void destroy();
    void draw(SpriteBatch batch);

    Body getBody();
    boolean isDestroyed();

    void setSprite(Texture texture);

    void revive();

    void setType(String newType);


    String getType();
    void setPosition(Vector2 pos);





}
