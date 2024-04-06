package Parsing;

import GameObjects.Actors.ObjectTypes.EnemyType;

import java.util.List;

public record SpawnFrame(List<EnemyType> spawnable, SpawnType spawnType, List<String> args) {
}
