package Rendering.Animations.AnimationRendering;

import GameObjects.GameObject;
import Rendering.Animations.AnimationState;
import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.EnumMap;
import java.util.Map;

import static VikingSurvivor.app.HelloWorld.SET_FPS;

@ExcludeFromGeneratedCoverage
public class GIFRender implements AnimationRender {

    private final Map<AnimationState, GifPair> animationMovement = new EnumMap<>(AnimationState.class);
    private GifPair currentGIF;
    private float rotation = 0;
    private float rotationSpeed = 0;
    private AnimationState state;
    private final float scale;


    protected GIFRender(Map<AnimationState, String> animationMovement, float scale) {
        getGifPairs(animationMovement);
        this.scale = scale;
    }


    @Override
    public void draw(SpriteBatch batch, long frame, GameObject object) {

        float elapsedTime = (float) frame / SET_FPS;
        currentGIF = animationMovement.get(state);
        if (currentGIF == null) return;
        TextureRegion region = object.isMovingLeft()
                ? currentGIF.left().getKeyFrame(elapsedTime)
                : currentGIF.right().getKeyFrame(elapsedTime);
        Vector2 regionRect = new Vector2(region.getRegionWidth(), region.getRegionHeight());
        regionRect.scl(1/regionRect.y * scale ); //* Main.PPM);


        batch.draw(
                region,
                object.getBody().getPosition().x - regionRect.x/2,
                object.getBody().getPosition().y -regionRect.y/2,
                regionRect.x/2,
                regionRect.y/2,
                regionRect.x,
                regionRect.y,
                1,
                1,
                rotation
        );
        rotation += rotationSpeed;

        if(rotation >= 360) {
            rotation = 0;
        }
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



    @Override
    public void rotate(float rotationSpeed) {

        this.rotationSpeed = rotationSpeed;

    }

    @Override
    public void stopRotation() {
        rotation = 0;
        rotationSpeed = 0;

    }




}
