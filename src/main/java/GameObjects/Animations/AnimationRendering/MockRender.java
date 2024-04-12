package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public class MockRender implements AnimationRender{

    public MockRender() {

    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime, GameObject object) {

    }

    @Override
    public void setAnimation(AnimationState state) {

    }

    @Override
    public float getWidth(AnimationState state) {
        return 1f;
    }

    @Override
    public float getHeight(AnimationState state) {
        return 1f;
    }

    @Override
    public void setAnimations(Map<AnimationState, String> animationMap) {

    }
}
