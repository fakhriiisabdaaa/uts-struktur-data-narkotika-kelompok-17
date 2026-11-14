package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.struktur.Stack;

import java.time.LocalDateTime;

public class AuditService {

    private final Stack<String> riwayat;
    private int nomorAksi;

    public AuditService() {
        this.riwayat = new Stack<>();
        this.nomorAksi = 0;
    }


    public void catat(String jenis, String detail) {
        if (jenis == null || jenis.isBlank()) {
            throw new IllegalArgumentException("Jenis aksi tidak boleh kosong");
        }
        nomorAksi++;
        String isi = "#" + nomorAksi + " [" + LocalDateTime.now() + "] " + jenis + " - " + (detail == null ? "" : detail);
        riwayat.push(isi);
    }

    public String tampilRiwayat() {
        if (riwayat.isEmpty()) {
            return "(Riwayat kosong)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < riwayat.size(); i++) {
            sb.append(riwayat.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String aksiTerakhir() {
        if (riwayat.isEmpty()) {
            return "(Riwayat kosong)";
        }
        return riwayat.peek();
    }

    public String hapusCatatanTerakhir() {
        return riwayat.pop();
    }

    public int cariRiwayat(String teks) {
        return riwayat.searchPreserve(teks);
    }

    public int getLangkahCari() {
        return riwayat.getLangkahTerakhir();
    }

    public int jumlah() {
        return riwayat.size();
    }

    public boolean kosong() {
        return riwayat.isEmpty();
    }

    public void bersihkan() {
        riwayat.clear();
    }

    public void validateInvariant() {
        riwayat.validateInvariant();
    }
}