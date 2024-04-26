package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;
import GameObjects.Actors.DirectionState;
import GameObjects.GameObject;
import VikingSurvivor.app.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

import static VikingSurvivor.app.HelloWorld.SET_FPS;


public class GIFRender implements AnimationRender {

    private final Map<AnimationState, GifPair> animationMovement = new EnumMap<>(AnimationState.class);
//    private Map<AnimationState, String> animationMovementTemp;
    private GifPair currentGIF;

    private AnimationState state;
    private final float scale;

    protected GIFRender(Map<AnimationState, String> animationMovement, float scale) {
        //animationMovementTemp = animationMovement;
        getGifPairs(animationMovement);
        this.scale = scale;
    }


    @Override
    public void draw(SpriteBatch batch, long frame, GameObject object) {
        float elapsedTime = (float) frame / SET_FPS;
        currentGIF = animationMovement.get(state);
        if (currentGIF == null) return;
        TextureRegion region = object.getDirectionState() == DirectionState.RIGHT
                ? currentGIF.right().getKeyFrame(elapsedTime)
                : currentGIF.left().getKeyFrame(elapsedTime);
        Vector2 regionRect = new Vector2(region.getRegionWidth(), region.getRegionHeight());
        float max = Math.max(regionRect.x, regionRect.y);
        max = regionRect.y;
        regionRect.scl(1/max * scale * Main.PPM);
        batch.draw(
                region,
                object.getBody().getPosition().x - regionRect.x / 2,
                object.getBody().getPosition().y - regionRect.y / 2,
                regionRect.x,
                regionRect.y
        );
    }

    @Override
    public void setAnimation(AnimationState state) {
        this.state = state;
        currentGIF = animationMovement.get(state);
        if (currentGIF == null) return;
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

//    @Override
//    public void initAnimations(Map<AnimationState, String> animationMap) {
//        getGifPairs(animationMap);
//    }

    private void getGifPairs(Map<AnimationState, String> map) {
        //animationMovement.clear(); //?
        Map.Entry<AnimationState, String> lastentry = null;
        
        for(var entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            animationMovement.put(
                    state,
                    GIFS.getGIF(
                            filePath,
                            s -> animationMovement.put(state, s)
                    )
            );
            lastentry = entry;
        }
        
        currentGIF = animationMovement.get(lastentry.getKey());
    }



}
