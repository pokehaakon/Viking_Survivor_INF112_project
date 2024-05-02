package Tools;

import GameObjects.GameObject;
import Tools.Pool.IPool;
import Tools.Pool.ObjectPool;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static Tools.ListTools.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListToolsTest {


    @Test
    void compactListTest() {
        List<Integer> ints = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        compactList(ints);
        assertEquals(10, ints.size());

        ints.set(6, null);
        compactList(ints);
        assertEquals(10, ints.size());
        assertEquals(7, ints.get(6));
        assertNull(ints.get(9));
    }

    @Test
    void removeDestroyedTest() {
        List<GameObject> ls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var go = mock(GameObject.class);
            if (i % 3 == 0) {
                when(go.isDestroyed()).thenReturn(true);
            } else {
                when(go.isDestroyed()).thenReturn(false);
            }
            ls.add(go);
        }

        assertEquals(10, ls.size());
        removeDestroyed(ls, mock(ObjectPool.class), true);
        assertEquals(6, ls.size());
    }

    @Test
    void findPrefixOptionalAndFindPrefixTest() {
        var ls = List.of("xhello", "yhello", "123other");
        Optional<String> a = findPrefixOptional("x", ls);
        Optional<String> b = findPrefixOptional("2", ls);

        assertDoesNotThrow(a::get);
        assertEquals("hello", a.get());

        assertThrowsExactly(NoSuchElementException.class, b::get);

        assertEquals("hello", findPrefix("y", ls));
        assertThrowsExactly(NoSuchElementException.class, () -> findPrefix("t", ls));
    }

    @Test
    void getFirstAndGetAllTest() {
        List<Integer> ints = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

        Optional<Integer> digitLargerThan7 = getFirst(ints, x -> x > 7);
        Optional<Integer> digitLargerThan19 = getFirst(ints, x -> x > 19);

        assertTrue(digitLargerThan7.isPresent());
        assertEquals(8, digitLargerThan7.get());

        assertTrue(digitLargerThan19.isEmpty());

        List<Integer> odds = getAll(ints, x -> x % 2 == 1);

        assertEquals(5, odds.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i*2+1, odds.get(i));
        }
    }
}