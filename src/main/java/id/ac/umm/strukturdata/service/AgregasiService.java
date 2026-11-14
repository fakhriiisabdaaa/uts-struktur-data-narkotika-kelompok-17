package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

public class AgregasiService {

    public static class HitunganJenis {
        private final String jenis;
        private int jumlah;

        HitunganJenis(String jenis) {
            this.jenis = jenis;
            this.jumlah = 1;
        }

        public String getJenis() { return jenis; }
        public int getJumlah() { return jumlah; }

        @Override
        public String toString() {
            return jenis + ": " + jumlah;
        }
    }

    public int jumlahPerkara(ArrayDinamis<Putusan> data) {
        return data.size();
    }

    public double rataRataVonis(ArrayDinamis<Putusan> data) {
        if (data.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i).getVonisBulan();
        }
        return (double) total / data.size();
    }

    public long totalDenda(ArrayDinamis<Putusan> data) {
        long total = 0;
        for (int i = 0; i < data.size(); i++) {
            total += data.get(i).getDendaRupiah();
        }
        return total;
    }

    public ArrayDinamis<HitunganJenis> distribusiJenis(ArrayDinamis<Putusan> data) {
        ArrayDinamis<HitunganJenis> hasil = new ArrayDinamis<>();
        for (int i = 0; i < data.size(); i++) {
            String jenis = data.get(i).getJenisNarkotika();
            if (jenis == null || jenis.isBlank()) {
                jenis = "(tidak diketahui)";
            }
            int posisi = cariJenis(hasil, jenis);
            if (posisi == -1) {
                hasil.add(new HitunganJenis(jenis));
            } else {
                hasil.get(posisi).jumlah++;
            }
        }
        return hasil;
    }

    public String ringkasan(ArrayDinamis<Putusan> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Jumlah perkara   : ").append(jumlahPerkara(data)).append("\n");
        sb.append("Rata-rata vonis  : ")
                .append(String.format("%.2f", rataRataVonis(data)))
                .append(" bulan\n");
        sb.append("Total denda      : Rp").append(totalDenda(data)).append("\n");
        sb.append("Distribusi jenis :\n");
        ArrayDinamis<HitunganJenis> dist = distribusiJenis(data);
        for (int i = 0; i < dist.size(); i++) {
            sb.append("  - ").append(dist.get(i)).append("\n");
        }
        return sb.toString();
    }

    private int cariJenis(ArrayDinamis<HitunganJenis> daftar, String jenis) {
        for (int i = 0; i < daftar.size(); i++) {
            if (daftar.get(i).getJenis().equalsIgnoreCase(jenis)) {
                return i;
            }
        }
        return -1;
    }
}