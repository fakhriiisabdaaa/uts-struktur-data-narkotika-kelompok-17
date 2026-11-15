package id.ac.umm.strukturdata.struktur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {

    @Test
    void testConstructor() {
        Stack<Integer> stack = new Stack<>();

        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
        assertEquals(26, stack.kapasitas());

        stack.validateInvariant();
    }

    @Test
    void testConstructorDenganKapasitas() {
        Stack<Integer> stack = new Stack<>(5);

        assertEquals(5, stack.kapasitas());
        assertEquals(0, stack.size());
    }

    @Test
    void testPushDanPeek() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());
        assertEquals("C", stack.get(0));
        assertEquals("B", stack.get(1));
        assertEquals("A", stack.get(2));

        stack.validateInvariant();
    }

    @Test
    void testPop() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assertEquals("C", stack.pop());
        assertEquals("B", stack.peek());
        assertEquals(2, stack.size());

        assertEquals("B", stack.pop());
        assertEquals("A", stack.pop());

        assertTrue(stack.isEmpty());

        stack.validateInvariant();
    }

    @Test
    void testLIFO() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(10, stack.pop());

        assertTrue(stack.isEmpty());
    }

    @Test
    void testSearchPreserveDataDitemukan() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        int hasil = stack.searchPreserve("B");

        assertEquals(1, hasil);
        assertEquals(2, stack.getLangkahTerakhir());

        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());
        assertEquals("C", stack.get(0));
        assertEquals("B", stack.get(1));
        assertEquals("A", stack.get(2));

        stack.validateInvariant();
    }

    @Test
    void testSearchPreserveDataTidakDitemukan() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        int hasil = stack.searchPreserve("D");

        assertEquals(-1, hasil);
        assertEquals(3, stack.getLangkahTerakhir());

        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());

        stack.validateInvariant();
    }

    @Test
    void testSearchPreserveElemenPalingAtas() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assertEquals(0, stack.searchPreserve("C"));
        assertEquals(1, stack.getLangkahTerakhir());

        assertEquals("C", stack.peek());
        assertEquals(3, stack.size());

        stack.validateInvariant();
    }

    @Test
    void testContains() {
        Stack<String> stack = new Stack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assertTrue(stack.contains("B"));
        assertFalse(stack.contains("D"));

        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());

        stack.validateInvariant();
    }

    @Test
    void testAddDanGet() {
        Stack<Integer> stack = new Stack<>();

        stack.add(10);
        stack.add(20);
        stack.add(30);

        assertEquals(3, stack.size());
        assertEquals(30, stack.get(0));
        assertEquals(20, stack.get(1));
        assertEquals(10, stack.get(2));

        stack.validateInvariant();
    }

    @Test
    void testRemovePuncak() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        int hasil = stack.remove(0);

        assertEquals(30, hasil);
        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());

        stack.validateInvariant();
    }

    @Test
    void testRemoveSelainPuncak() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);

        assertThrows(UnsupportedOperationException.class,
                () -> stack.remove(1));

        assertEquals(2, stack.size());
        assertEquals(20, stack.peek());

        stack.validateInvariant();
    }

    @Test
    void testResize() {
        Stack<Integer> stack = new Stack<>(2);

        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(3, stack.size());
        assertEquals(4, stack.kapasitas());

        assertEquals(30, stack.peek());
        assertEquals(20, stack.get(1));
        assertEquals(10, stack.get(2));

        stack.validateInvariant();
    }

    @Test
    void testClear() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);
        stack.push(20);
        stack.push(30);

        stack.clear();

        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());

        stack.validateInvariant();
    }

    @Test
    void testPushNull() {
        Stack<String> stack = new Stack<>();

        assertThrows(IllegalArgumentException.class,
                () -> stack.push(null));

        assertTrue(stack.isEmpty());
        stack.validateInvariant();
    }

    @Test
    void testPopStackKosong() {
        Stack<Integer> stack = new Stack<>();

        assertThrows(IllegalStateException.class,
                () -> stack.pop());
    }

    @Test
    void testPeekStackKosong() {
        Stack<Integer> stack = new Stack<>();

        assertThrows(IllegalStateException.class,
                () -> stack.peek());
    }

    @Test
    void testGetIndexTidakValid() {
        Stack<Integer> stack = new Stack<>();

        stack.push(10);

        assertThrows(IndexOutOfBoundsException.class,
                () -> stack.get(-1));

        assertThrows(IndexOutOfBoundsException.class,
                () -> stack.get(1));
    }

    @Test
    void testKapasitasTidakValid() {
        assertThrows(IllegalArgumentException.class,
                () -> new Stack<Integer>(0));

        assertThrows(IllegalArgumentException.class,
                () -> new Stack<Integer>(-1));
    }

    @Test
    void testInvariantSetelahBerbagaiOperasi() {
        Stack<Integer> stack = new Stack<>(2);

        stack.push(10);
        stack.push(20);
        stack.validateInvariant();

        stack.push(30);
        stack.validateInvariant();

        stack.pop();
        stack.validateInvariant();

        stack.searchPreserve(10);
        stack.validateInvariant();

        stack.clear();
        stack.validateInvariant();
    }
}