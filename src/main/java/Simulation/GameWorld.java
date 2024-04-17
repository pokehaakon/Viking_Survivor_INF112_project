package Simulation;

import Contexts.ReleaseCandidateContext;
import Parsing.MapParser;
import Parsing.SpawnFrame;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

public class GameWorld implements Disposable {
    private Map<String, String> defines;
    private List<Pair<Long, List<SpawnFrame>>> timeFrames;
    private TiledMapRenderer mapRenderer;
    private final TiledMap map;
    private final float mapScale = 4f;
    private long nextFrame;
    private int frameIndex = 0;
    public GameWorld(String worldDef) {
        if (!worldDef.endsWith(".wdef")) {
            throw new RuntimeException("world definition file needs ending '.wdef', got : " + worldDef);
        }

        MapParser parser = new MapParser(worldDef);
        parser.doParse();
        defines = parser.getDefines();
        timeFrames = parser.getTimeFrames();

        if (timeFrames != null) {
            nextFrame = timeFrames.get(0).getValue0();
        } else {
            nextFrame = Long.MAX_VALUE;
        }

        //map = new TmxMapLoader().load("assets/damaged_roads_map.tmx");
        map = new TmxMapLoader().load(defines.get("MAP_PATH"));
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapScale);
    }
    public void act(Long frame) {
        if (frame == nextFrame) {
            System.out.println("NEXT FRAME!");
            for (var f : timeFrames.get(frameIndex++).getValue1()) {
                System.out.println("\t" + f.toString());
            }
            if (timeFrames.size() == frameIndex) {
                nextFrame = Long.MAX_VALUE;
            } else {
                nextFrame = timeFrames.get(frameIndex).getValue0();
            }
        }
    }

    private void summon() {

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
