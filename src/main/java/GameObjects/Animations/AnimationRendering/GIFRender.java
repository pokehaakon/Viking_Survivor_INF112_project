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

    float rotation = 0;
    float rotationSpeed = 0;

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
        float originX = object.getBody().getPosition().x;
        float originY = object.getBody().getPosition().y;

        // Adjust origin to center of the TextureRegion
        originX -= region.getRegionWidth() * 0.5f;
        originY -= region.getRegionHeight() * 0.5f;

        batch.draw(
                region,
                originX,
                originY,
                region.getRegionWidth() * 0.5f,
                region.getRegionHeight() * 0.5f,
                region.getRegionWidth(),
                region.getRegionHeight(),
                object.getScale(),
                object.getScale(),
                rotation
        );

        rotation += rotationSpeed;

        if(rotation >= 360) {
            rotation = 0;
        }
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

    @Override
    public void rotate(float rotationSpeed) {

        this.rotationSpeed = rotationSpeed;

    }

    @Override
    public void stopRotation() {
        rotation = 0;
        rotationSpeed = 0;

    }

    private void getGifPairs(Map<AnimationState,String> map) {
        for(Map.Entry<AnimationState, String> entry : map.entrySet()) {
            AnimationState state = entry.getKey();
            String filePath = entry.getValue();
            animationMovement.put(state, animationLibrary.getGif(filePath));
        }
    }



}
