package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

public class FilterService {

    public static class Kriteria {
        public String jenisNarkotika;
        public Double beratMin;
        public Double beratMaks;
        public String pasalTerbukti;
        public Integer vonisMin;
        public Integer vonisMaks;
        public Long dendaMin;
        public Long dendaMaks;
    }

    private final PutusanService sumber;

    public FilterService(PutusanService sumber) {
        if (sumber == null) {
            throw new IllegalArgumentException("sumber tidak boleh kosong");
        }
        this.sumber = sumber;
    }

    public ArrayDinamis<Putusan> filter(Kriteria k) {
        if (k == null) {
            throw new IllegalArgumentException("kriteria tidak boleh kosong");
        }
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>();
        ArrayDinamis<Putusan> arsip = sumber.getArsip();
        for (int i = 0; i < arsip.size(); i++) {
            Putusan p = arsip.get(i);
            if (cocok(p, k)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> JenisNarkotika(String jenis) {
        Kriteria k = new Kriteria();
        k.jenisNarkotika = jenis;
        return filter(k);
    }

    public ArrayDinamis<Putusan> RentangBerat(double min, double maks) {
        Kriteria k = new Kriteria();
        k.beratMin = min;
        k.beratMaks = maks;
        return filter(k);
    }

    public ArrayDinamis<Putusan> PasalTerbukti(String pasal) {
        Kriteria k = new Kriteria();
        k.pasalTerbukti = pasal;
        return filter(k);
    }

    public ArrayDinamis<Putusan> RentangVonis(int min, int maks) {
        Kriteria k = new Kriteria();
        k.vonisMin = min;
        k.vonisMaks = maks;
        return filter(k);
    }

    public ArrayDinamis<Putusan> RentangDenda(long min, long maks) {
        Kriteria k = new Kriteria();
        k.dendaMin = min;
        k.dendaMaks = maks;
        return filter(k);
    }

    private boolean cocok(Putusan p, Kriteria k) {
        if (k.jenisNarkotika != null
                && !mengandung(p.getJenisNarkotika(), k.jenisNarkotika)) {
            return false;
        }
        if (k.pasalTerbukti != null
                && !mengandung(p.getPasalTerbukti(), k.pasalTerbukti)) {
            return false;
        }
        if (k.beratMin != null && p.getBeratBarangBuktiGram() < k.beratMin) {
            return false;
        }
        if (k.beratMaks != null && p.getBeratBarangBuktiGram() > k.beratMaks) {
            return false;
        }
        if (k.vonisMin != null && p.getVonisBulan() < k.vonisMin) {
            return false;
        }
        if (k.vonisMaks != null && p.getVonisBulan() > k.vonisMaks) {
            return false;
        }
        if (k.dendaMin != null && p.getDendaRupiah() < k.dendaMin) {
            return false;
        }
        if (k.dendaMaks != null && p.getDendaRupiah() > k.dendaMaks) {
            return false;
        }
        return true;
    }

    private boolean mengandung(String teks, String dicari) {
        if (teks == null) {
            return false;
        }
        return teks.toLowerCase().contains(dicari.toLowerCase());
    }
}