package GameObjects.AnimationRendering;

import Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import GameObjects.AnimationRendering.AnimationRender;
import GameObjects.AnimationRendering.GifPair;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Map;

import static Animations.AnimationConstants.FRAME_DURATION;


public class GIFRender<E extends Enum<E>> implements AnimationRender {

    private final Map<AnimationState, GifPair> animationMovement;



    private GifPair currentGIF;


    public GIFRender(Map<AnimationState, GifPair> animationMovement) {
        this.animationMovement = animationMovement;
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime, GameObject object) {
        TextureRegion region;
        if(object.getDirectionState() == DirectionState.RIGHT) {
            region = currentGIF.right().getKeyFrame(elapsedTime);
        }
        else {
            region = currentGIF.left().getKeyFrame(elapsedTime);
        }
        batch.draw(
                region,
                object.getBody().getPosition().x - (float) region.getRegionWidth() /2*object.getScale(),
                object.getBody().getPosition().y - (float) region.getRegionHeight() /2*object.getScale(),
                region.getRegionWidth() * object.getScale(),
                region.getRegionHeight() * object.getScale()
        );
    }



    @Override
    public void setAnimation(AnimationState state) {
        currentGIF = animationMovement.get(state);
        currentGIF.right().setFrameDuration(FRAME_DURATION);
        currentGIF.left().setFrameDuration(FRAME_DURATION);


    }

    @Override
    public float getWidth(AnimationState state) {
        // gets width of first frame. assume that all frames have equal dimensions
        TextureRegion region  = animationMovement.get(state).right().getKeyFrame(0);
        return region.getRegionWidth();
    }

    @Override
    public float getHeight(AnimationState state) {
        // gets height of first frame. assume that all frames have equal dimensions
        TextureRegion region  = animationMovement.get(state).right().getKeyFrame(0);
        return region.getRegionHeight();
    }



}
