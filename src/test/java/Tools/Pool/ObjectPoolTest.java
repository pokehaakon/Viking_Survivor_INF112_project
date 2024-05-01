package Tools.Pool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PoolTest {
    static final private class PInt implements Poolable {
        public final int value;

        private PInt(int val) {
            this.value = val;
        }
        public static PInt of(int val) {return new PInt(val);}
        public static PInt of(String str) {return new PInt(Integer.parseInt(str));}

        @Override
        public String getName() {
            return Integer.toString(value);
        }
    }
    @Test
    void testObjectPool() {
        ObjectPool<PInt> pool = new ObjectPool<>(PInt::of);
        var subPool = pool.createSubPool(str -> PInt.of(Integer.parseInt(str) * 2));

        var one = pool.get("1");

        assertEquals(1, one.value);
        assertEquals("1", one.getName());
        pool.returnToPool(one);

        var sub_one = subPool.get("1");
        one = pool.get("1");

        assertEquals(2, sub_one.value);
        assertEquals(2, one.value);

        var many_twos = pool.get("2", 10);

        assertEquals(10, many_twos.size());
        assertTrue(many_twos.stream().map(pi -> pi.value == 2).reduce((a, b) -> a && b).get());


        var three = pool.get("3");
        pool.returnToPool(three);
        var new_three = pool.get("3");

        assertSame(three, new_three);

        pool.returnToPool(new_three);

        var fourSmallPool = pool.getSmallPool("4");
        var largePoolFour = pool.get("4");
        pool.returnToPool(largePoolFour);
        var smallPoolFour = fourSmallPool.get();

        assertSame(largePoolFour, smallPoolFour);

        assertThrowsExactly(IllegalArgumentException.class, () -> fourSmallPool.get("1"));
        assertThrowsExactly(IllegalArgumentException.class, () -> fourSmallPool.get("1", 1));
        assertThrowsExactly(IllegalArgumentException.class, () -> fourSmallPool.getSmallPool("1"));
    }

}