package GameObjects;

import GameObjects.Actors.DirectionState;
import Rendering.Animations.AnimationState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Interface for all game objects
 * (all objects rendered on the screen in the game)
 */
public interface IGameObject {

    /**
     * Sets the destroyed-tag to true
     */
    void destroy();

    /**
     * Handles the sprite drawing
     * @param batch the current sprite batch
     */
    void draw(SpriteBatch batch, long frame);

    /**
     *
     * @return a Body object
     */
    Body getBody();

    /**
     * Returns the boolean value of the object's destroyed-tag
     * @return a boolean
     */
    boolean isDestroyed();

    /**
     * Changes the sprite to draw
     * @param texture the new sprite
     */
    //void setSprite(Texture texture);

    /**
     * Sets the destroyed-tag to false and gives object a new ID
     */
    void revive();

    /**
     *
     * @return the object type
     */
    String getType();

    /**
     * Changes the body position
     * @param pos the new position
     */
    void setPosition(Vector2 pos);

    /**
     * Creates body and add to world
     * @param world
     */
    void addToWorld(World world);

    void setAnimationState(AnimationState state);
    void setAnimation(AnimationState state);

    DirectionState getDirectionState();
    void setDirectionState(DirectionState directionState);

    /**
     * @return the ID of the object
     */
    int getID();

}
