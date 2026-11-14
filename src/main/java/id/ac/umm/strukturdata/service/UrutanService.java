package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

public class UrutanService {

    private final PutusanService sumber;
    private int langkahTerakhir;

    public UrutanService(PutusanService sumber) {
        if (sumber == null) {
            throw new IllegalArgumentException("sumber tidak boleh kosong");
        }
        this.sumber = sumber;
    }

    public ArrayDinamis<Putusan> urutVonisNaik() {
        return urutVonis(true);
    }

    public ArrayDinamis<Putusan> urutVonisTurun() {
        return urutVonis(false);
    }

    public ArrayDinamis<Putusan> urutVonis(boolean naik) {
        ArrayDinamis<Putusan> salinan = salin(sumber.getArsip());

        langkahTerakhir = 0;
        for (int i = 1; i < salinan.size(); i++) {
            Putusan kunci = salinan.get(i);
            int j = i - 1;

            while (j >= 0) {
                langkahTerakhir++;
                if (harusDigeser(salinan.get(j), kunci, naik)) {
                    salinan.set(j + 1, salinan.get(j)); // geser ke kanan
                    j--;
                } else {
                    break;
                }
            }
            salinan.set(j + 1, kunci);
        }
        return salinan;
    }

    public String tampil(ArrayDinamis<Putusan> hasil) {
        if (hasil.isEmpty()) {
            return "(data kosong)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hasil.size(); i++) {
            sb.append(i + 1).append(". ").append(hasil.get(i)).append("\n");
        }
        return sb.toString();
    }

    public int getLangkahTerakhir() {
        return langkahTerakhir;
    }

    private boolean harusDigeser(Putusan a, Putusan kunci, boolean naik) {
        if (naik) {
            return a.getVonisBulan() > kunci.getVonisBulan();
        }
        return a.getVonisBulan() < kunci.getVonisBulan();
    }

    private ArrayDinamis<Putusan> salin(ArrayDinamis<Putusan> asal) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(Math.max(1, asal.size()));
        for (int i = 0; i < asal.size(); i++) {
            hasil.add(asal.get(i));
        }
        return hasil;
    }
}