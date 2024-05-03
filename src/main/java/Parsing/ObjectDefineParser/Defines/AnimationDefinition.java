package Parsing.ObjectDefineParser.Defines;

import Rendering.Animations.AnimationState;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnimationDefinition {
    public static final Set<String> legalDefines = Set.of("State", "Scale", "state", "initial");
    public final Map<AnimationState, String> stateStringMap;
    public final AnimationState initial;
    public final float scale;

    private AnimationDefinition(Map<AnimationState, String> stateStringMap, AnimationState initial, float scale) {
        this.stateStringMap = stateStringMap;
        this.initial = initial;
        this.scale = scale;
    }

    public static AnimationDefinition of(Map<AnimationState, String> stateStringMap, AnimationState initial, float scale) {
        return new AnimationDefinition(stateStringMap, initial, scale);
    }

    @Override
    public String toString() {
        return "AnimationDefinition(initial: " + initial + ", " + scale + ", StateMap: { " + stateStringMap.entrySet().stream().map(e -> e.getKey() + ": '" + e.getValue() + "'").sorted().collect(Collectors.joining(", ")) + " })";
    }
}
