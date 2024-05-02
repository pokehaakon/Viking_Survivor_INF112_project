package GameObjects.ObjectActions;

import GameObjects.Actor;
import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import GameObjects.Actor.*;

import static com.badlogic.gdx.Graphics.GraphicsType.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WeaponActionsTest {
    Actor referenceActor;

    @BeforeEach
    void setUp() {

        referenceActor = Mockito.mock(Actor.class);
        when(referenceActor.getPosition()).thenReturn(new Vector2(0,0));
    }

    @Test
    void setSpeed() {
        System.out.println(referenceActor.getPosition());
    }

    @Test
    void orbitActor() {
    }

    @Test
    void getClosestActor() {
    }

    @Test
    void fireAtClosestEnemy() {
    }
}