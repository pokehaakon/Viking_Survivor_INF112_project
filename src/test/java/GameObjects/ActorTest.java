package GameObjects;

import GameObjects.ObjectActions.Action;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static GameObjects.ObjectActions.KilledAction.destroyIfDefeated;
import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.PickupActions.giveHP;
import static GameObjects.TestTools.createTestActor;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ActorTest {

    static World world;

    Actor a1;
    Actor a2;
    Actor a3;

    Action action1;
    Action action2;


    @BeforeEach
    void setup() {
        world = new World(new Vector2(0,0), true);
        a1 = createTestActor(world);
        a2 = createTestActor(world);
        a3 = createTestActor(world);

        a1.setID(1);
        a2.setID(2);
        a3.setID(3);

        action1 = mock(Action.class);
        action2 = mock(Action.class);

    }

    @Test
    void attack() {
        float startHP = a1.getHP();
        a2.attack(a1);
        assertEquals(a1.getHP(), startHP - a2.getDamage());

    }
    @Test
    void updateDirectionState_wrongDirection() {
        // move right
        a1.getBody().setLinearVelocity(1,0);
        a1.updateDirectionState();
        assertFalse(a1.isMovingLeft());
    }
    @Test
    void updateDirectionState_whenNotMoving() {
        // move right
        a1.getBody().setLinearVelocity(1,0);
        a1.updateDirectionState();
        // stop actor
        a1.getBody().setLinearVelocity(0,0);

        a1.updateDirectionState();
        // direction state should remain the same
        assertFalse(a1.isMovingLeft());
    }


    @Test
    void setTemporaryActionChange_multipleChanges() {

        a1.addAction(action1);
        a1.setTemporaryActionChange(10,action2);
        a1.setTemporaryActionChange(1, action2);

        // only duration of last change should count
        a1.doAction();
        a1.doAction();
        assertTrue(a1.getActions().contains(action1));
        assertFalse(a1.getActions().contains(action2));

    }

    @Test
    void setTemporaryActionChange_testCountDown() {
        a1.addAction(action1);

        a1.setTemporaryActionChange(1,action2);

        a1.doAction();
        assertFalse(a1.getActions().contains(action1));
        assertTrue(a1.getActions().contains(action2));

        a1.doAction();
        assertFalse(a1.getActions().contains(action2));
        assertTrue(a1.getActions().contains(action1));
    }

    @Test
    void dieAction() {
        float startHP = a2.getHP();
        float actionHP = 10;
        a1.addDieAction(giveHP(a2,actionHP,100));
        a1.kill();
        assertEquals(startHP + actionHP,a2.getHP());

        assertTrue(a1.getActions().isEmpty());


    }

    @Test
    void setTemporaryActionChange_actionListSizeRemainsTheSame() {
        a1.addAction(action1);
        int origListSize = a1.getActions().size();
        a1.setTemporaryActionChange(1,action1);
        a1.setTemporaryActionChange(1,action2);
        a1.doAction();
        a1.doAction();
        assertEquals(origListSize,a1.getActions().size());
    }

    @Test
    void setTemporaryActionChange_startConditions() {
        a1.addAction(action1);

        a1.setTemporaryActionChange(2,action2);
        assertFalse(a1.getActions().contains(action1));
        assertTrue(a1.getActions().contains(action2));

        a1.doAction();
        a1.doAction();
        a1.doAction();
        assertTrue(a1.getActions().contains(action1));



    }

    @Test
    void startCoolDown() {
        assertFalse(a1.isInCoolDown());

        a1.startCoolDown(1);
        assertTrue(a1.isInCoolDown());

        a1.doAction();

        assertTrue(a1.isInCoolDown());

        a1.doAction();

        assertFalse(a1.isInCoolDown());
    }

    @Test
    void attackedBy_generalCase() {
        a1.attack(a2);

        assertTrue(a2.attackedBy(a1));

    }

    @Test
    void attackedBy_whenDestroyed() {
        a1.attack(a2);
        a3.attack(a2);
        a1.destroy();
        assertTrue(a2.attackedBy(a1));

        a2.destroy();
        assertTrue(a2.attackedBy(a3));

    }

    @Test
    void attackedBy_whenRevived() {
        a1.attack(a2);

        a2.revive();
        assertFalse(a2.attackedBy(a1));


    }
}