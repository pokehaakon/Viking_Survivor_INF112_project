package Simulation.SpawnHandler;

import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.Action;
import GameObjects.ObjectActions.PickupActions;
import GameObjects.ObjectActions.WeaponActions;
import Parsing.SpawnType;
import Simulation.ISpawnHandler;
import Tools.FilterTool;
import Tools.Pool.ObjectPool;

import java.util.ArrayList;
import java.util.List;

import static Contexts.GameContext.DE_SPAWN_RECT;
import static Contexts.GameContext.SPAWN_RECT;
import static GameObjects.ObjectActions.KilledAction.destroyIfDefeated;
import static GameObjects.ObjectActions.KilledAction.spawnPickupsIfKilled;
import static GameObjects.ObjectActions.MovementActions.chaseActor;
import static GameObjects.ObjectActions.OutOfBoundsActions.deSpawnIfOutOfBounds;
import static GameObjects.ObjectActions.PickupActions.giveHP;
import static GameObjects.ObjectActions.PickupActions.giveXP;
import static Simulation.Coordinates.SpawnCoordinates.randomPointOutsideScreenRect;
import static VikingSurvivor.app.HelloWorld.millisToFrames;

public class SpawnHandlerFactory {

    private final ObjectPool<Actor> actorPool;
//    private final ObjectPool<GameObject> terrainPool;

    private final List<Actor> activeActors;
//    private final List<GameObject> activeTerrain;
    private final Actor player;

    public SpawnHandlerFactory(Actor player,
                               ObjectPool<Actor> actorPool,
                               List<Actor> activeActors
        ) {

        this.player = player;
        this.actorPool = actorPool;
        this.activeActors = activeActors;
    }



    public ISpawnHandler create(String actorName, SpawnType spawnType, List<String> args) {
        //TODO do this better :)
        List<Action> dropActions = List.of(spawnPickupsIfKilled(0.1f,"HP_PICKUP", activeActors, actorPool, giveHP(player,10,1000)),
                spawnPickupsIfKilled(0.4f,"XP_PICKUP", activeActors, actorPool, giveXP(10)),
                spawnPickupsIfKilled(0.1f,"SKULL_PICKUP", activeActors,actorPool,
                        PickupActions.startTemporaryActionChange(
                                FilterTool.Category.WEAPON,
                                millisToFrames(5000),
                                activeActors,
                                WeaponActions.orbitActor(0.4f,10,  player, 0, 0)
                        )
            )
        );

        return switch (spawnType) {
            case SWARM -> new SwarmSpawnHandler(
                    args,
                    actorName,
                    e -> {
                        e.addAction(destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT.cpy().scl(2)));
                        e.addDieAction(dropActions);
                    },
                    () -> player.getBody().getPosition(),
                    actorPool,
                    activeActors
            );

            case BOSS -> new WaveSpawnHandler(
                    args,
                    actorName,
                    e -> {
                        e.addAction(chaseActor(player), destroyIfDefeated());
                        e.setPosition(randomPointOutsideScreenRect(player.getPosition()));

                        for(int i = 0; i < 6; i++) {
                            Actor weapon = actorPool.get("WEAPON_RAVEN");
                            //weapon.getAnimationHandler().rotate(10f);

                            weapon.addAction(
                                    WeaponActions.fireAtClosestActor(
                                            FilterTool.Category.PLAYER,
                                            e.getSpeed()+weapon.getSpeed(),
                                            e, millisToFrames(400*i),
                                            activeActors,
                                            SPAWN_RECT)
                                    );
                            activeActors.add(weapon);

                        }


                    },
                    actorPool,
                    activeActors
            );

            case WAVE -> new WaveSpawnHandler(
                    args,
                    actorName,
                    e -> {
                        e.addAction(chaseActor(player), destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT));
                        e.addDieAction(dropActions);
                        e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                    },
                    actorPool,
                    activeActors
            );
            case CONTINUOUS -> new ContinuousSpawnHandler(
                args,
                actorName,
                e -> {
                    e.addAction(chaseActor(player), destroyIfDefeated(), deSpawnIfOutOfBounds(player, DE_SPAWN_RECT));
                    e.addDieAction(dropActions);
                    e.setPosition(randomPointOutsideScreenRect(player.getBody().getPosition()));
                },
                actorPool,
                activeActors
            );
        };
    }
}
