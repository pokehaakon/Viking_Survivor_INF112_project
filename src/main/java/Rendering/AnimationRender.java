package Rendering;

import Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface AnimationRender {

    void draw(SpriteBatch batch, float elapsedTime, GameObject object);

    void setAnimation(AnimationState state);




}
