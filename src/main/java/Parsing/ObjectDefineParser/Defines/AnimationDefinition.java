package Parsing.ObjectDefineParser.Defines;

import Rendering.Animations.AnimationState;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnimationDefinition {
    public static final Set<String> legalDefines = Set.of("State", "state", "initial");
    public final Map<AnimationState, String> stateStringMap;
    public final AnimationState initial;

    private AnimationDefinition(Map<AnimationState, String> stateStringMap, AnimationState initial) {
        this.stateStringMap = stateStringMap;
        this.initial = initial;
    }

    public static AnimationDefinition of(Map<AnimationState, String> stateStringMap, AnimationState initial) {
        return new AnimationDefinition(stateStringMap, initial);
    }

    @Override
    public String toString() {
        return "AnimationDefinition(initial: " + initial + ", StateMap: { " + stateStringMap.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(", ")) + " } )";
    }
}
