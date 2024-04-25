package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import static GameObjects.Animations.AnimationRendering.GIFS.FRAME_DURATION;


public class GIFRender<E extends Enum<E>> implements AnimationRender {

    private final Map<AnimationState, GifPair> animationMovement = new EnumMap<>(AnimationState.class);



    private GifPair currentGIF;

    private final AnimationLibrary animationLibrary;



    public GIFRender(AnimationLibrary animationLibrary, Map<AnimationState, String> animationMovement) {
        this.animationLibrary = animationLibrary;
        getGifPairs(animationMovement);


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
        // gets width of first frame of gif. assume that all frames have equal dimensions
        TextureRegion region  = animationMovement.get(state).right().getKeyFrame(0);
        return region.getRegionWidth();
    }

    @Override
    public float getHeight(AnimationState state) {
        // gets height of first frame. assume that all frames have equal dimensions
        TextureRegion region  = animationMovement.get(state).right().getKeyFrame(0);
        return region.getRegionHeight();
    }

    @Override
    public void setAnimations(Map<AnimationState, String> animationMap) {
        getGifPairs(animationMap);

    }

    private void getGifPairs(Map<AnimationState,String> map) {
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            animationMovement.put(state, animationLibrary.getGif(filePath));
        }
    }



}
