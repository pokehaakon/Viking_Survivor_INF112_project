package GameObjects;

import GameObjects.Actor;
import GameObjects.BodyFeatures;
import GameObjects.StatsConstants;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static org.mockito.Mockito.mock;


    public abstract class TestTools {

         static Actor createTestActor(World world) {

            BodyFeatures bodyFeatures = new BodyFeatures(
                    createCircleShape(1),
                    mock(Filter.class),
                    1,
                    1,
                    1,
                    false,
                    BodyDef.BodyType.DynamicBody);
            Actor a = new Actor("TEST",mock(AnimationHandler.class),bodyFeatures, new StatsConstants.Stats(1,1,1,1));
            a.addToWorld(world);
            return a;
        }

        public static Actor createTestActorCustomFilterCategory(World world, FilterTool.Category category) {

            BodyFeatures bodyFeatures = new BodyFeatures(
                    createCircleShape(1),
                    createFilter(category, FilterTool.Category.ENEMY),
                    1,
                    1,
                    1,
                    false,
                    BodyDef.BodyType.DynamicBody);
            GameObjects.Actor a = new GameObjects.Actor("TEST",mock(AnimationHandler.class),bodyFeatures, new StatsConstants.Stats(1,1,1,1));
            a.addToWorld(world);
            return a;
        }
    }


