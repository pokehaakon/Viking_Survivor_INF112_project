package GameObjects.Factories;

import Rendering.Animations.AnimationRendering.AnimationRender;
import GameObjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFactory<T extends GameObject<E>, E extends Enum<E>> implements IFactory<T, E> {

    protected AnimationRender  spriteRender;
    protected AnimationRender gifRender;

    /**
     * Create multiple enemies
     * @param n number of enemies to create
     * @param type the desired enemy type
     * @return a list of Enemy objects
     */
    @Override
    public List<T> create(int n, E type) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of Objects must be greater than zero");
        }

        List<T> objects = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            T newEnemy = create(type);
            objects.add(newEnemy);
        }
        return objects;
    }

    @Override
    public void setSpriteRender(AnimationRender spriteRender) {
        this.spriteRender = spriteRender;
    }

    @Override
    public void setGifRender(AnimationRender gifRender) {
        this.gifRender = gifRender;
    }


}
