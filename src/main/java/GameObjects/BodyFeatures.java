package GameObjects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

public record BodyFeatures(Shape shape,
                           Filter filter,
                           float density,
                           float friction,
                           float restitution,
                           boolean isSensor,
                           BodyDef.BodyType type) {
}
