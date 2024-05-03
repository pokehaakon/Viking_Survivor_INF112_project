package Parsing;

import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationState;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

public class DefinesTest {

    @Test
    void testAnimationDefinition() {
        var def = AnimationDefinition.of(
            Map.of(AnimationState.MOVING, "moving.png", AnimationState.IDLE, "idle.png"),
            AnimationState.IDLE,
            10
        );
        String expected = "AnimationDefinition(initial: IDLE, 10.0, StateMap: { IDLE: 'idle.png', MOVING: 'moving.png' })";
        assertEquals(expected, def.toString());
    }

    @Test
    void testCircleShapeDefinition() {
        var def = CircleShapeDefinition.of(1);
        String expected = "CircleShapeDefinition(Radius: 1.0)";
        assertEquals(expected, def.toString());
    }

    @Test
    void testPolygonShapeDefinition() {
        float[] points = {0,0, 1,0, 1,1};
        var def = PolygonShapeDefinition.of(points);
        String expected = "PolygonShapeDefinition(points: [0.0, 0.0, 1.0, 0.0, 1.0, 1.0])";
        assertEquals(expected, def.toString());
    }

    @Test
    void testSquareShapeDefinition() {
        var def = SquareShapeDefinition.of(10, 20);
        String expected = "SquareShapeDefinition(Width: 10.0, Height: 20.0)";
        assertEquals(expected, def.toString());
    }

    @Test
    void testStatsDefinition() {
        var def = StatsDefinition.of(1,2,3,4);
        String expected = "StatsDefinition(HP: 1.0, Speed: 2.0, Damage: 3.0, resistance: 4.0)";
        assertEquals(expected, def.toString());
    }

    @Test
    void testStructureDefinition() {
        var shape = CircleShapeDefinition.of(1);

        var def = StructureDefinition.of(
                FilterTool.createFilter(FilterTool.Category.PLAYER, FilterTool.Category.ENEMY),
                shape,
                1,
                2,
                false,
                BodyDef.BodyType.DynamicBody
        );

        String expected = "StructureDefinition(Filter: {C:1 M:2}, ShapeDefinition: CircleShapeDefinition(Radius: 1.0), density:1.0, friction:2.0)";
        assertEquals(expected, def.toString());
    }

    @Test
    void testActorDefinition() {
        var animation = AnimationDefinition.of(
                Map.of(AnimationState.MOVING, "moving.png", AnimationState.IDLE, "idle.png"),
                AnimationState.IDLE,
                10
        );

        var stats = StatsDefinition.of(1,2,3,4);

        var shape = CircleShapeDefinition.of(1);

        var struct = StructureDefinition.of(
                FilterTool.createFilter(FilterTool.Category.PLAYER, FilterTool.Category.ENEMY),
                shape,
                1,
                2,
                false,
                BodyDef.BodyType.DynamicBody
        );

        var def = ActorDefinition.of(animation, stats, struct);

        String expected = """
                ActorDefinition(
                \tAnimationDefinition: AnimationDefinition(initial: IDLE, 10.0, StateMap: { IDLE: 'idle.png', MOVING: 'moving.png' })
                \tStatsDefinitionStatsDefinition(HP: 1.0, Speed: 2.0, Damage: 3.0, resistance: 4.0)
                \tStructureDefinitionStructureDefinition(Filter: {C:1 M:2}, ShapeDefinition: CircleShapeDefinition(Radius: 1.0), density:1.0, friction:2.0)
                )""";
        assertEquals(expected, def.toString());


    }

    @Test
    void testObjectDefinition() {
        var animation = AnimationDefinition.of(
                Map.of(AnimationState.MOVING, "moving.png", AnimationState.IDLE, "idle.png"),
                AnimationState.IDLE,
                10
        );

        var shape = CircleShapeDefinition.of(1);

        var struct = StructureDefinition.of(
                FilterTool.createFilter(FilterTool.Category.PLAYER, FilterTool.Category.ENEMY),
                shape,
                1,
                2,
                false,
                BodyDef.BodyType.DynamicBody
        );

        var def = ObjectDefinition.of(animation, struct);
        String expected = """
                ObjectDefinition(
                \tAnimationDefinition: AnimationDefinition(initial: IDLE, 10.0, StateMap: { IDLE: 'idle.png', MOVING: 'moving.png' })
                \tStructureDefinitionStructureDefinition(Filter: {C:1 M:2}, ShapeDefinition: CircleShapeDefinition(Radius: 1.0), density:1.0, friction:2.0)
                )""";
        assertEquals(expected, def.toString());
    }
}
