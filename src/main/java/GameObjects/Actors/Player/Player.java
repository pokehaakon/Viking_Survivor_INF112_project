package GameObjects.Actors.Player;

import GameObjects.Actors.Actor;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.BodyFeatures;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends Actor<PlayerType> {
    public int level;
    public float XP;

    private PlayerStats stats;


    public Player(String type,Texture texture, BodyFeatures bodyFeatures, float scale, PlayerStats stats) {
        super(type,texture,bodyFeatures, scale);
        this.stats = stats;

        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        XP = stats.XP();
        level = 1;

        idle = true;
    }

    public Player() {
        idle = true;
        level = 1;
    }

    public void setStats(PlayerStats newStats) {
        HP = newStats.HP();
        speed = newStats.speed();
        damage = newStats.damage();
        armour = newStats.armour();
        XP = newStats.XP();
    }




}
