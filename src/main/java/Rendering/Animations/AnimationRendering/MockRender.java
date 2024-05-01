package Rendering.Animations.AnimationRendering;

import GameObjects.GameObject;
import Rendering.Animations.AnimationState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MockRender implements AnimationRender {

    public MockRender() {

    }

    @Override
    public void draw(SpriteBatch batch, long frame, GameObject object) {

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
    public void rotate(float rotationSpeed){

    }

    @Override
    public void stopRotation() {

    }

    @Override
    public void setDrawColor(Color color) {

    }

//    @Override
//    public void initAnimations(Map<AnimationState, String> animationMap) {
//
//    }
}
