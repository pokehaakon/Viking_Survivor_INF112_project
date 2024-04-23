package GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Interface for all game objects
 * (all objects rendered on the screen in the game)
 */
public interface IGameObject<E> {

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
     * Sets the destroyed-tag to false
     */
    void revive();

    /**
     * Sets the object type
     * @param newType the object type. A string value which tells us which type of enemy, terrain etc
     */
    void setType(E newType);

    /**
     *
     * @return the object type
     */
    E getType();

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


    /**
     * Sets body featues for body creation
     * @param features BodyFeatures record
     */
   void setBodyFeatures(BodyFeatures features);

    /**
     * Sets scale for sprite size handling
     * @param newScale
     */
    void setScale(float newScale);

//    /**
//     * This assigns animations to objects (gif or sprites), depending on the animation map of the object
//     * @param animationLibrary the library which contains the gif and sprites
//     */
//    void renderAnimations(AnimationLibrary animationLibrary);






}
