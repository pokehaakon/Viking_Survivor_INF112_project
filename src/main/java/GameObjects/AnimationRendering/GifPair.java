package GameObjects.AnimationRendering;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Generates a pair of gifs. The pair is identical, except that one is facing left and the other right.
 *
 * @param left left facing gif
 * @param right right facing gif
 */
public record GifPair(Animation<TextureRegion> left, Animation<TextureRegion> right){}
