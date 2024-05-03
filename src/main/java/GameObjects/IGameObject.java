package GameObjects;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import Tools.Pool.Poolable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Interface for all game objects
 * (all objects rendered on the screen in the game)
 */
public interface IGameObject extends Poolable {

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
     * Sets the destroyed-tag to false and gives object a new ID
     */
    void revive();

    /**
     * Changes the body position
     * @param pos the new position
     */
    void setPosition(Vector2 pos);

    /**
     * Creates body and add to world
     * @param world the world in which to create the body
     */
    void addToWorld(World world);

    void setAnimationState(AnimationState state);
    void setAnimation(AnimationState state);

    boolean isMovingLeft();
    void setMovingLeft(boolean movingLeft);

    /**
     * @return the ID of the object
     */
    int getID();

    /**
     * Checks if object is out of bounds, i.e. if object's position is beyond the bounded square
     * @param centerObject reference points, center of bounded squre
     * @param boundSquare a vector2 objects which sets the bounds
     * @return true if out of bounds, false otherwise
     */
    boolean outOfBounds(GameObject centerObject, Vector2 boundSquare);


    /**
     *
     * @return the position of the objects body
     */
    Vector2 getPosition();


    /**
     * The animationhandler is responsible for handling everything that has to do with animation rendering.
     * That is, which animation type to render, the object's animation state and the scale
     * @return the object's animation handler
     */
    AnimationHandler getAnimationHandler();


}
