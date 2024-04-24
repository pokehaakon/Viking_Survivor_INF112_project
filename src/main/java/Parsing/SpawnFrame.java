package Parsing;

import GameObjects.ObjectTypes.EnemyType;

import java.util.List;

public record SpawnFrame(String spawnable, SpawnType spawnType, List<String> args) {
}
