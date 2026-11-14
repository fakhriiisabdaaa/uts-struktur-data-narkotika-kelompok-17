package id.ac.umm.strukturdata.struktur;

public class Stack<T> implements StrukturData<T> {

    private static final int KAPASITAS_AWAL = 26;
    private Object[] data;
    private int size;
    private int langkahTerakhir;
    private Stack<T> bantu;

    public Stack() {
        this(KAPASITAS_AWAL);
    }

    public Stack(int kapasitasAwal) {
        if (kapasitasAwal < 1) {throw new IllegalArgumentException("Kapasitas awal minimal 1");
        }
        this.data = new Object[kapasitasAwal];
        this.size = 0;
    }

    public void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Elemen stack tidak boleh null");
        }
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size] = value;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong");
        }
        @SuppressWarnings("unchecked")
        T hasil = (T) data[size - 1];
        data[size - 1] = null;
        size--;
        return hasil;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack kosong");
        }
        @SuppressWarnings("unchecked")
        T hasil = (T) data[size - 1];
        return hasil;
    }

    public int searchPreserve(T target) {
        langkahTerakhir = 0;
        if (bantu == null) {
            bantu = new Stack<>(data.length);
        }
        int hasil = -1;
        try {
            int jarak = 0;
            while (!isEmpty()) {
                T elemen = pop();
                bantu.push(elemen);
                langkahTerakhir++;
                if (sama(elemen, target)) {
                    hasil = jarak;
                    break;
                }
                jarak++;
            }
        } finally {
            restore();
        }
        return hasil;
    }

    public void restore() {
        if (bantu == null) {
            return;
        }
        while (!bantu.isEmpty()) {
            push(bantu.pop());
        }
    }

    public int getLangkahTerakhir() {
        return langkahTerakhir;
    }

    @Override
    public void add(T value) {
        push(value);
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi di luar rentang: " + position + ", size=" + size);
        }
        @SuppressWarnings("unchecked")
        T hasil = (T) data[size - 1 - position];
        return hasil;
    }

    @Override
    public T remove(int position) {
        if (position != 0) {
            throw new UnsupportedOperationException("Stack hanya boleh menghapus puncak (posisi 0)");
        }
        return pop();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T value) {
        return searchPreserve(value) != -1;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
        if (bantu != null) {
            bantu.clear();
        }
    }

    public int kapasitas() {
        return data.length;
    }

    private void resize(int kapasitasBaru) {
        Object[] baru = new Object[kapasitasBaru];
        for (int i = 0; i < size; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }

    private boolean sama(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public void validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("Size negatif: " + size);
        }
        if (size > data.length) {
            throw new IllegalStateException("Size melebihi kapasitas");
        }
        for (int i = 0; i < size; i++) {
            if (data[i] == null) {
                throw new IllegalStateException("Slot " + i + " tidak boleh null(berlubang)");
            }
        }
        for (int i = size; i < data.length; i++) {
            if (data[i] != null) {
                throw new IllegalStateException("Slot " + i + " harus null");
            }
        }
        if (bantu != null && !bantu.isEmpty()) {
            throw new IllegalStateException("Stack bantu harus kosong di luar pencarian");
        }
    }
}
