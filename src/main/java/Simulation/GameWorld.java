package Simulation;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import GameObjects.Animations.AnimationRendering.AnimationLibrary;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import Parsing.MapParser;
import Parsing.SpawnFrame;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import org.javatuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static GameObjects.Actors.ActorAction.EnemyActions.destroyIfDefeated;
import static GameObjects.Actors.ActorAction.EnemyActions.destroyIfOutOfBounds;
import static Tools.ListTools.removeDestroyed;

public class GameWorld implements Disposable {
    private Map<String, String> defines;
    private List<Pair<Long, List<SpawnFrame>>> timeFrames;
    private TiledMapRenderer mapRenderer;
    private final TiledMap map;
    private final float mapScale = 4f;
    private long nextFrame;
    private int frameIndex = 0;
    private final List<ISpawnHandler> spawnHandlers = new ArrayList<>();

    private final SpawnHandlerFactory handlerFactory;

    private final ObjectPool<Enemy, EnemyType> enemyPool;
    private final ObjectPool<Terrain, TerrainType> terrainPool;

    private final List<Enemy> activeEnemies;
    private final List<Terrain> activeTerrain;
    private final Player player;
    private final AnimationLibrary animationLibrary;

    public GameWorld(
            String worldDef,
            Player player,
            ObjectPool<Enemy, EnemyType> enemyPool,
            ObjectPool<Terrain, TerrainType> terrainPool,
            List<Enemy> activeEnemies,
            List<Terrain> activeTerrain,
            AnimationLibrary animationLibrary
    ) {
        if (!worldDef.endsWith(".wdef")) {
            throw new RuntimeException("world definition file needs ending '.wdef', got : " + worldDef);
        }

        this.player = player;
        this.enemyPool = enemyPool;
        this.terrainPool = terrainPool;
        this.activeEnemies = activeEnemies;
        this.activeTerrain = activeTerrain;
        this.animationLibrary = animationLibrary;

        handlerFactory = new SpawnHandlerFactory(player, enemyPool, terrainPool, activeEnemies, activeTerrain, animationLibrary);

        MapParser parser = new MapParser(worldDef);

        parser.doParse();
        defines = parser.getDefines();
        timeFrames = parser.getTimeFrames();

        if (timeFrames != null) {
            nextFrame = timeFrames.get(0).getValue0();
        } else {
            nextFrame = Long.MAX_VALUE;
        }

        map = new TmxMapLoader().load(defines.get("MAP_PATH"));
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapScale);
    }
    public void act(Long frame) {
        if (frame == nextFrame) {
            setNextFrame(frame);
        }
        actThisFrame(frame);
    }

    private void actThisFrame(long frame) {
        for (ISpawnHandler handler : spawnHandlers) {
            handler.act(frame);
        }
    }

    private void setNextFrame(long frame) {
        spawnHandlers.clear();

        System.out.println("Frame: " + frameIndex);
        for (var f : timeFrames.get(frameIndex++).getValue1()) {
            spawnHandlers.add(handlerFactory.create(f.spawnable(), f.spawnType(), f.args()));
            System.out.println("\t" + f);
        }

        nextFrame = timeFrames.size() == frameIndex ? Long.MAX_VALUE : timeFrames.get(frameIndex).getValue0();
    }

    public void render(OrthographicCamera camera, float delta) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
