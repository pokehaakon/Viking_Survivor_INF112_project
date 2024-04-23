package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import GameObjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.EnumMap;
import java.util.Map;

import static VikingSurvivor.app.HelloWorld.SET_FPS;


public class GIFRender implements AnimationRender {

    private final Map<AnimationState, GifPair> animationMovement = new EnumMap<>(AnimationState.class);
    private GifPair currentGIF;

    protected GIFRender(Map<AnimationState, String> animationMovement) {
        getGifPairs(animationMovement);
    }


    @Override
    public void draw(SpriteBatch batch, long frame, GameObject<?> object) {
        float elapsedTime = (float) frame / SET_FPS;
        TextureRegion region = object.getDirectionState() == DirectionState.RIGHT
                ? currentGIF.right().getKeyFrame(elapsedTime)
                : currentGIF.left().getKeyFrame(elapsedTime);

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
        currentGIF.right().setFrameDuration(GIFS.FRAME_DURATION);
        currentGIF.left().setFrameDuration(GIFS.FRAME_DURATION);
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

    @Override
    public void initAnimations(Map<AnimationState, String> animationMap) {
        getGifPairs(animationMap);
    }

    private void getGifPairs(Map<AnimationState, String> map) {
        //animationMovement.clear(); //?
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            animationMovement.put(state, GIFS.getGIF(filePath));
        }
    }



}
