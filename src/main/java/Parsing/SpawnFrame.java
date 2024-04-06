package Parsing;

import Actors.Enemy.EnemyType;

import java.util.List;

public record SpawnFrame(List<EnemyType> spawnable, SpawnType spawnType, List<String> args) {
}
