package GameObjects.ObjectActions;

import GameObjects.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import Tools.Pool.ObjectPool;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static GameObjects.ObjectActions.WeaponActions.getClosestActor;
import static Tools.FilterTool.createFilter;
import static Tools.FilterTool.isInCategory;
import static Tools.ShapeTools.createCircleShape;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class WeaponActionsTest {
    Actor referenceActor;

    ObjectPool<GameObject> objectPool;
    ObjectPool<Actor> actorPool;
    ObjectFactory factory;

    FilterTool filterTool;
    static World world;

    private static Actor createTestActorCustomFilter(FilterTool.Category category, World world) {

        BodyFeatures bodyFeatures = new BodyFeatures(
                createCircleShape(1),
                createFilter(category,FilterTool.Category.ENEMY),
                1,
                1,
                1,
                false,
                BodyDef.BodyType.DynamicBody);
        Actor a = new Actor("TEST",mock(AnimationHandler.class),bodyFeatures, new StatsConstants.Stats(1,1,1,1));
        a.addToWorld(world);
        return a;
    }
    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {

            @Override
            public void create() {
                world =  new World(new Vector2(0,0), true);

            }

            @Override
            public void resize(int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void render() {


            }

            @Override
            public void pause() {
                // TODO Auto-generated method stub

            }

            @Override
            public void resume() {
                // TODO Auto-generated method stub

            }

            @Override
            public void dispose() {
                // TODO Auto-generated method stub

            }};
        new HeadlessApplication(listener, config);


    }

    @BeforeEach
    void setup() {
        world = new World(new Vector2(0,0), true);

       referenceActor = createTestActorCustomFilter(FilterTool.Category.ENEMY,world);




    }


    @Test
    void setSpeed() {
        //System.out.println(referenceActor.getPosition());
    }

    @Test
    void orbitActor() {
    }

    @Test
    void getClosestActor_closestPosition() {
        Actor a1 = createTestActorCustomFilter(FilterTool.Category.ENEMY, world);
        a1.setPosition(new Vector2(1,0));
        Actor a2 = createTestActorCustomFilter(FilterTool.Category.ENEMY, world);
        a2.setPosition(new Vector2(2,0));

        List<Actor> list = List.of(a1,a2);

        referenceActor.setPosition(new Vector2(0,0));

        Actor target = getClosestActor(referenceActor,list, FilterTool.Category.ENEMY);

        assertEquals(target, a1);

    }
    @Test
    void getClosestActor_noneOfCorrectFilterCategory() {
        Actor a1 = createTestActorCustomFilter(FilterTool.Category.ENEMY, world);
        a1.setPosition(new Vector2(1,0));
        Actor a2 = createTestActorCustomFilter(FilterTool.Category.ENEMY, world);
        a2.setPosition(new Vector2(2,0));

        List<Actor> list = List.of(a1,a2);

        referenceActor.setPosition(new Vector2(0,0));


        Actor target = getClosestActor(referenceActor,list, FilterTool.Category.PLAYER);

        assertNull(target);
    }

    @Test
    void getClosestActor_closestActorIsOfWrongFilterCategory() {
        Actor a1 = createTestActorCustomFilter(FilterTool.Category.PLAYER, world);
        a1.setPosition(new Vector2(1,0));
        Actor a2 = createTestActorCustomFilter(FilterTool.Category.ENEMY, world);
        a2.setPosition(new Vector2(2,0));

        List<Actor> list = List.of(a1,a2);

        referenceActor.setPosition(new Vector2(0,0));

        Actor target = getClosestActor(referenceActor,list, FilterTool.Category.ENEMY);

        assertEquals(target,a2);
    }

    @Test
    void fireAtClosestEnemy() {

    }
}