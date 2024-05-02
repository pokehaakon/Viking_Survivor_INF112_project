package Camera;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;

public class TargetCamera {

    /**
     * sets camera to follow target. Camera stops when edge of the map is hit
     * @param target target which camera follows
     * @param cam OrthographicCamera
     * @param map gamemap
     * @param mapScale scale of the map
     */
    public static void updateCamera(Vector2 target, Camera cam, TiledMap map, float mapScale) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        float mapHeight = layer.getHeight() * layer.getTileHeight() * mapScale;
        float mapWidth = layer.getWidth() * layer.getTileWidth() * mapScale;

        if(target.x < cam.viewportWidth / 2) cam.position.x = cam.viewportWidth / 2;
        else if(target.x > mapWidth - cam.viewportWidth / 2) cam.position.x = mapWidth - cam.viewportWidth / 2;
        else cam.position.x = target.x;

        if(target.y < cam.viewportHeight / 2) cam.position.y = cam.viewportHeight / 2;
        else if(target.y > mapHeight - cam.viewportHeight / 2) cam.position.y = mapHeight - cam.viewportHeight / 2;
        else cam.position.y = target.y;

        cam.update();
    }
}
