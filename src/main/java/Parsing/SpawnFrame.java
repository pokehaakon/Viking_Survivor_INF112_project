package Parsing;

import GameObjects.ObjectTypes.EnemyType;

import java.util.List;

public record SpawnFrame(EnemyType spawnable, SpawnType spawnType, List<String> args) {
}
