package Tools;

import com.badlogic.gdx.physics.box2d.Filter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static Tools.FilterTool.*;

class FilterToolTest {

    @Test
    void combineMaskEnums() {
        short mask = FilterTool.combineMaskEnums(new Category[] {Category.PLAYER, Category.ENEMY});
        assertEquals(0b0011, mask);
    }

    @Test
    void testcreateFilter1() {
        Filter f = FilterTool.createFilter(Category.PLAYER, new Category[]{Category.ENEMY, Category.WALL});
        assertEquals(0b0001, f.categoryBits);
        assertEquals(0b0110, f.maskBits);
    }

    @Test
    void testCreateFilter2() {
        Filter f = FilterTool.createFilter(new Category[]{Category.PLAYER, Category.ENEMY}, new Category[]{Category.ENEMY, Category.WALL});
        assertEquals(0b0011, f.categoryBits);
        assertEquals(0b0110, f.maskBits);
    }
}