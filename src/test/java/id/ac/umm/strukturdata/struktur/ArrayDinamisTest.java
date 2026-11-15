package id.ac.umm.strukturdata.struktur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayDinamisTest {

    @Test
    void testConstructor() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>();
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
        assertEquals(26, array.kapasitas());
        array.validateInvariant();
    }

    @Test
    void testConstructorDenganKapasitas() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>(5);
        assertEquals(5, array.kapasitas());
        assertEquals(0, array.size());
    }

    @Test
    void testAddDanGet() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("Data A");
        array.add("Data B");
        array.add("Data C");
        assertEquals(3, array.size());
        assertEquals("Data A", array.get(0));
        assertEquals("Data B", array.get(1));
        assertEquals("Data C", array.get(2));
        array.validateInvariant();
    }

    @Test
    void testInsert() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("A");
        array.add("C");
        array.insert(1, "B");
        assertEquals(3, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
        array.insert(0, "Awal");
        array.insert(array.size(), "Akhir");
        assertEquals("Awal", array.get(0));
        assertEquals("Akhir", array.get(array.size() - 1));
        array.validateInvariant();
    }

    @Test
    void testSet() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("Data Lama");
        array.add("Data B");
        String hasil = array.set(0, "Data Baru");
        assertEquals("Data Lama", hasil);
        assertEquals("Data Baru", array.get(0));
        assertEquals("Data B", array.get(1));
    }

    @Test
    void testRemove() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("A");
        array.add("B");
        array.add("C");
        String hasil = array.remove(1);
        assertEquals("B", hasil);
        assertEquals(2, array.size());
        assertEquals("A", array.get(0));
        assertEquals("C", array.get(1));
        array.validateInvariant();
    }

    @Test
    void testSearch() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("Andi");
        array.add("Budi");
        array.add("Citra");
        assertEquals(1, array.search("Budi"));
        assertEquals(-1, array.search("Doni"));
        assertEquals(3, array.getLangkahTerakhir());
        assertTrue(array.contains("Andi"));
        assertFalse(array.contains("Doni"));
    }

    @Test
    void testSearchDenganNull() {
        ArrayDinamis<String> array = new ArrayDinamis<>();
        array.add("A");
        array.add(null);
        array.add("C");
        assertEquals(1, array.search(null));
        assertTrue(array.contains(null));
        array.validateInvariant();
    }

    @Test
    void testResize() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>(2);
        array.add(10);
        array.add(20);
        array.add(30);
        assertEquals(3, array.size());
        assertEquals(4, array.kapasitas());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
        assertEquals(30, array.get(2));
        array.validateInvariant();
    }

    @Test
    void testClear() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>();
        array.add(10);
        array.add(20);
        array.add(30);
        array.clear();
        assertEquals(0, array.size());
        assertTrue(array.isEmpty());
        array.validateInvariant();
    }

    @Test
    void testIndexDiLuarRentang() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>();
        array.add(10);
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(2));
        assertThrows(IndexOutOfBoundsException.class, () -> array.set(-1, 20));
        assertThrows(IndexOutOfBoundsException.class, () -> array.insert(3, 20));
    }

    @Test
    void testKapasitasTidakValid() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayDinamis<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new ArrayDinamis<Integer>(-1));
    }

    @Test
    void testInvariantSetelahBerbagaiOperasi() {
        ArrayDinamis<Integer> array = new ArrayDinamis<>(2);
        array.add(10);
        array.add(20);
        array.validateInvariant();
        array.insert(1, 15);
        array.validateInvariant();
        array.remove(0);
        array.validateInvariant();
        array.set(0, 25);
        array.validateInvariant();
        array.clear();
        array.validateInvariant();
    }
}