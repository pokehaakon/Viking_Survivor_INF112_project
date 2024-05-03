package GameObjects.ObjectActions;

import GameObjects.Actor;
import GameObjects.BodyFeatures;
import GameObjects.StatsConstants;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static GameObjects.TestTools.createTestActorCustomFilterCategory;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PickupActionsTest {

    Actor a1;

    World world;

    Action action1;

    Action action2;

    List<Actor> actors;


    @BeforeEach
    void setUp() {
        world = new World(new Vector2(0,0),true);
        a1 = createTestActorCustomFilterCategory(world,FilterTool.Category.PLAYER);

        action1 = mock(Action.class);
        action2 = mock(Action.class);


    }



    @Test
    void startTemporaryActionChange_wrongCategory() {
        a1.addAction(action1);
        a1.addAction(PickupActions.startTemporaryActionChange(FilterTool.Category.ENEMY,
                1,
                List.of(a1),
                action2
                ));
        a1.doAction();
        assertFalse(a1.getActions().contains(action2));

    }
    @Test
    void startTemporaryActionChange_correctCategory() {
        a1.addAction(action1);
        a1.addAction(PickupActions.startTemporaryActionChange(FilterTool.Category.PLAYER,
                1,
                List.of(a1),
                action2
        ));
        a1.doAction();
        assertTrue(a1.getActions().contains(action2));

    }



}