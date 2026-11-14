package id.ac.umm.strukturdata.struktur;

public class ArrayDinamis<T> implements StrukturData<T> {

    private static final int KAPASITAS_AWAL = 26;

    private Object[] data;
    private int size;
    private int langkahTerakhir;

    public ArrayDinamis() {
        this(KAPASITAS_AWAL);
    }

    public ArrayDinamis(int kapasitasAwal) {
        if (kapasitasAwal < 1) {
            throw new IllegalArgumentException("Kapasitas awal minimal 1");
        }
        this.data = new Object[kapasitasAwal];
        this.size = 0;
    }

    @Override
    public void add(T value) {
        pastikanKapasitas(size + 1);
        data[size] = value;
        size++;
    }

    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Posisi insert di luar rentang: " + position + ", size=" + size);
        }
        pastikanKapasitas(size + 1);
        for (int i = size; i > position; i--) {
            data[i] = data[i - 1];
        }
        data[position] = value;
        size++;
    }

    @Override
    public T get(int position) {
        periksaPosisi(position);
        @SuppressWarnings("unchecked")
        T hasil = (T) data[position];
        return hasil;
    }

    public T set(int position, T value) {
        periksaPosisi(position);
        @SuppressWarnings("unchecked")
        T lama = (T) data[position];
        data[position] = value;
        return lama;
    }

    @Override
    public T remove(int position) {
        periksaPosisi(position);
        @SuppressWarnings("unchecked")
        T dihapus = (T) data[position];
        for (int i = position; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return dihapus;
    }

    public int search(T target) {
        langkahTerakhir = 0;
        for (int i = 0; i < size; i++) {
            langkahTerakhir++;
            if (sama(data[i], target)) {
                return i;
            }
        }
        return -1;
    }

    public int getLangkahTerakhir() {
        return langkahTerakhir;
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
        return search(value) != -1;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public int kapasitas() {
        return data.length;
    }

    private void pastikanKapasitas(int minimal) {
        if (minimal <= data.length) {
            return;
        }
        resize(Math.max(data.length * 2, minimal));
    }

    private void resize(int kapasitasBaru) {
        Object[] baru = new Object[kapasitasBaru];
        for (int i = 0; i < size; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }

    private void periksaPosisi(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi di luar rentang: " + position + ", size=" + size);
        }
    }

    private boolean sama(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    public void validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("size negatif: " + size);
        }
        if (size > data.length) {
            throw new IllegalStateException("size melebihi kapasitas");
        }
        for (int i = size; i < data.length; i++) {
            if (data[i] != null) {
                throw new IllegalStateException("Slot " + i + " harus null");
            }
        }
    }
}
