package Simulation;

import GameObjects.Actors.Actor;
import GameObjects.Factories.ExperimentalFactory;
import GameObjects.GameObject;
import GameObjects.Pool.ObjectPool;
import Parsing.MapParser;
import Parsing.ObjectDefineParser.Defines.ActorDefinition;
import Parsing.ObjectDefineParser.ObjectDefineParser;
import Parsing.SpawnFrame;
import Simulation.SpawnHandler.SpawnHandlerFactory;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final ObjectPool<Actor> actorPool;
    private final ObjectPool<GameObject> objectPool;

    private final List<Actor> activeActors;
    private final List<GameObject> activeTerrain;
    public final Actor player;

    public GameWorld(
            String worldDef,
            ObjectPool<Actor> actorPool,
            ObjectPool<GameObject> objectPool,
            List<Actor> activeActors,
            List<GameObject> activeTerrain
    ) {
        if (!worldDef.endsWith(".wdef")) {
            throw new RuntimeException("world definition file needs ending '.wdef', got : " + worldDef);
        }


        this.actorPool = actorPool;
        this.objectPool = objectPool;
        this.activeActors = activeActors;
        this.activeTerrain = activeTerrain;



        MapParser mapParser = new MapParser(worldDef);
        defines = mapParser.doParseDefines();
        ObjectDefineParser objectDefineParser = new ObjectDefineParser(defines.get("OBJECT_DEFINES"));
        objectDefineParser.parseDocument();

        for (var entry : objectDefineParser.variables.entrySet()) {
            //System.out.println(entry);
            if (entry.getValue().get() instanceof ActorDefinition actorDefinition) {
                ExperimentalFactory.registerActor(entry.getKey().substring(1), actorDefinition);
            }
        }

        timeFrames = mapParser.doParseTimeFrames();

        map = new TmxMapLoader().load(defines.get("MAP_PATH"));
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapScale);

        if (timeFrames != null) {
            nextFrame = timeFrames.get(0).getValue0();
        } else {
            nextFrame = Long.MAX_VALUE;
        }

        this.player = ExperimentalFactory.createActor(defines.get("PLAYER_NAME"));

        handlerFactory = new SpawnHandlerFactory(player, actorPool, objectPool, activeActors, activeTerrain);
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

        //System.out.println("Frame: " + frameIndex);
        for (var f : timeFrames.get(frameIndex++).getValue1()) {
            spawnHandlers.add(handlerFactory.create(f.spawnable(), f.spawnType(), f.args()));
            //System.out.println("\t" + f);
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
