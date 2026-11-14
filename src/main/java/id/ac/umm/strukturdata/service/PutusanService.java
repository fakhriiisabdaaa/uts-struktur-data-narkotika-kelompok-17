package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

public class PutusanService {

    private final ArrayDinamis<Putusan> arsip;
    private int langkahTerakhir;

    public PutusanService() {
        this.arsip = new ArrayDinamis<>();
    }

    public void tambah(Putusan putusan) {
        if (putusan == null) {
            throw new IllegalArgumentException("Putusan tidak boleh kosong");
        }
        if (cariByIdKasus(putusan.getIdKasus()) != -1) {
            throw new IllegalArgumentException("ID KASUS sudah digunakan: " + putusan.getIdKasus());
        }
        arsip.add(putusan);
    }

    public void sisip(int posisi, Putusan putusan) {
        if (putusan == null) {
            throw new IllegalArgumentException("Putusan tidak boleh kosong");
        }
        if (cariByIdKasus(putusan.getIdKasus()) != -1) {
            throw new IllegalArgumentException("ID KASUS sudah digunakan: " + putusan.getIdKasus());
        }
        arsip.insert(posisi, putusan);
    }

    public Putusan hapus(int posisi) {
        return arsip.remove(posisi);
    }

    public Putusan ambil(int posisi) {
        return arsip.get(posisi);
    }

    public String tampilSemua() {
        if (arsip.isEmpty()) {
            return "(arsip kosong)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arsip.size(); i++) {
            sb.append(i).append(". ").append(arsip.get(i)).append("\n");
        }
        return sb.toString();
    }

    public int cariByIdKasus(String idKasus) {
        langkahTerakhir = 0;
        for (int i = 0; i < arsip.size(); i++) {
            langkahTerakhir++;
            if (sama(arsip.get(i).getIdKasus(), idKasus)) {
                return i;
            }
        }
        return -1;
    }

    public int cariByNoPerkara(String noPerkara) {
        langkahTerakhir = 0;
        for (int i = 0; i < arsip.size(); i++) {
            langkahTerakhir++;
            if (sama(arsip.get(i).getNoPerkara(), noPerkara)) {
                return i;
            }
        }
        return -1;
    }

    public int getLangkahTerakhir() {
        return langkahTerakhir;
    }

    public int jumlah() {
        return arsip.size();
    }

    public ArrayDinamis<Putusan> getArsip() {
        return arsip;
    }

    private boolean sama(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }
}