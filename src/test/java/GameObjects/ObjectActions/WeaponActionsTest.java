package GameObjects.ObjectActions;

import GameObjects.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static GameObjects.ObjectActions.WeaponActions.*;
import static GameObjects.TestTools.createTestActorCustomFilterCategory;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeaponActionsTest {
    static Actor testPlayer;

    Actor enemy1;
    Actor enemy2;
    Actor weapon;

    static List<Actor> actors;
    Action closestEnemyIntervalOne;
    Action getClosestEnemyIntervalTwo;

    static World world;



    private static Action customFireAtClosestEnemyAction(
            int frameInterval,
            Vector2 boundedSquare) {

        return fireAtClosestActor(
                FilterTool.Category.ENEMY,
                1,
                testPlayer,
                frameInterval,
                actors,
                boundedSquare);
    }

    static void step(World world) {
        world.step(1/(float) 60, 10, 10);
    }

    @BeforeEach
    void setup() {
        world = new World(new Vector2(0, 0), true);

        testPlayer = createTestActorCustomFilterCategory(world,FilterTool.Category.PLAYER);
        testPlayer.setPosition(new Vector2(0,0));

        enemy1 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        enemy2 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        enemy1.setPosition(new Vector2(4,0));
        enemy2.setPosition(new Vector2(5,0));

        weapon = createTestActorCustomFilterCategory(world,FilterTool.Category.WEAPON);
        weapon.setPosition(new Vector2(1,1));
        weapon.getBody().setLinearVelocity(0,0);

        actors = List.of(enemy1, enemy2, testPlayer,weapon);

    }

    @Test
    void getClosestActor_closestPosition() {
        Actor a1 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        a1.setPosition(new Vector2(1, 0));
        Actor a2 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        a2.setPosition(new Vector2(2, 0));

        List<Actor> list = List.of(a1, a2);


        Actor target = getClosestActor(testPlayer, list, FilterTool.Category.ENEMY);

        assertEquals(target, a1);

    }

    @Test
    void getClosestActor_noneOfCorrectFilterCategory() {
        Actor a1 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        a1.setPosition(new Vector2(1, 0));
        Actor a2 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        a2.setPosition(new Vector2(2, 0));

        List<Actor> list = List.of(a1, a2);



        Actor target = getClosestActor(testPlayer, list, FilterTool.Category.PLAYER);

        assertNull(target);
    }

    @Test
    void getClosestActor_closestActorIsOfWrongFilterCategory() {
        Actor a1 = createTestActorCustomFilterCategory(world,FilterTool.Category.PLAYER);
        a1.setPosition(new Vector2(1, 0));
        Actor a2 = createTestActorCustomFilterCategory(world,FilterTool.Category.ENEMY);
        a2.setPosition(new Vector2(2, 0));

        List<Actor> list = List.of(a1, a2);


        Actor target = getClosestActor(testPlayer, list, FilterTool.Category.ENEMY);

        assertEquals(target, a2);
    }

    @Test
    void fireAtClosestEnemy_startIntervalZero() {
        weapon.setPosition(testPlayer.getPosition());
        // frame interval 0
        weapon.addAction(customFireAtClosestEnemyAction(
                0,
                new Vector2(10,10)));

        // should leave player immediately
        weapon.doAction();
        step(world);
        assertNotEquals(weapon.getPosition(),testPlayer.getPosition());

    }
    @Test
    void fireAtClosestEnemy_startIntervalOne() {
        weapon.setPosition(testPlayer.getPosition());

        weapon.addAction(customFireAtClosestEnemyAction(
                1,
                new Vector2(10,10)));


        weapon.doAction();
        step(world);
        assertEquals(weapon.getPosition(),testPlayer.getPosition());

        weapon.doAction();
        step(world);
        assertNotEquals(weapon.getPosition(), testPlayer.getPosition());

    }

    @Test
    void fireAtClosestEnemy_whenOutOfBounds() {
        weapon.addAction(customFireAtClosestEnemyAction(1,new Vector2(2,2)));
        // setting position out of bounds
        weapon.setPosition(new Vector2(10,10));
        weapon.doAction();
        step(world);
        assertEquals(testPlayer.getPosition(),weapon.getPosition());
        assertFalse(weapon.getBody().isActive());
    }
    @Test
    void fireAtClosestEnemy_whenAttackEnemy() {
        weapon.addAction(customFireAtClosestEnemyAction(1,new Vector2(5,5)));
        weapon.setPosition(new Vector2(2,2));
        weapon.attack(enemy1);
        weapon.doAction();
        step(world);

        assertEquals(testPlayer.getPosition(),weapon.getPosition());
        assertFalse(weapon.getBody().isActive());
    }


    @Test
    void testOrbitActor() {
        Actor player = mock(GameObjects.Actor.class);
        Body playerBody = mock(Body.class);
        Vector2 playerPos = Vector2.Zero;

        when(player.getBody()).thenReturn(playerBody);
        when(playerBody.getPosition()).thenReturn(playerPos);

        Actor weapon = mock(GameObjects.Actor.class);
        Body weaponBody = mock(Body.class);
        Vector2 weaponPos = Vector2.Zero;

        when(weapon.getBody()).thenReturn(weaponBody);
        when(weaponBody.getPosition()).thenReturn(weaponPos);
        when(weaponBody.isActive()).thenReturn(false);


        Action orbit = orbitActor(1, 2, player, 1, 2);
        orbit.act(weapon);
        when(weaponBody.isActive()).thenReturn(true);
        int i = 0;
        while (i++ < 30)
            orbit.act(weapon);

        assertEquals(Vector2.Zero, playerPos);
    }

}
