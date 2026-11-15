package id.ac.umm.strukturdata.app;

import id.ac.umm.strukturdata.io.CsvExporter;
import id.ac.umm.strukturdata.io.CsvImporter;
import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.service.AgregasiService;
import id.ac.umm.strukturdata.service.AuditService;
import id.ac.umm.strukturdata.service.FilterService;
import id.ac.umm.strukturdata.service.PutusanService;
import id.ac.umm.strukturdata.service.UrutanService;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    private static final String FILE_CSV = "data/data_50_putusan.csv";
    private final Scanner in = new Scanner(System.in);
    private final PutusanService service = new PutusanService();
    private final FilterService filter = new FilterService(service);
    private final UrutanService urutan = new UrutanService(service);
    private final AgregasiService agregasi = new AgregasiService();
    private final AuditService audit = new AuditService();
    private final CsvImporter importer = new CsvImporter();
    private final CsvExporter exporter = new CsvExporter();

    private ArrayDinamis<Putusan> terakhir;

    public static void main(String[] args) {
        new Main().jalankan();
    }

    public void jalankan() {
        boolean lanjut = true;
        while (lanjut) {
            cetakMenu();
            int pilihan = bacaInt("Pilih menu: ");
            try {
                switch (pilihan) {
                    case 1:
                        impor();
                        break;
                    case 2:
                        tampil();
                        break;
                    case 3:
                        detailSatuData();
                        break;
                    case 4:
                        tambah();
                        break;
                    case 5:
                        hapus();
                        break;
                    case 6:
                        cari();
                        break;
                    case 7:
                        filterMenu();
                        break;
                    case 8:
                        agregasiMenu();
                        break;
                    case 9:
                        urutMenu();
                        break;
                    case 10:
                        auditMenu();
                        break;
                    case 11:
                        ekspor();
                        break;
                    case 0:
                        lanjut = false;

                    default:
                        System.out.println("Menu tidak dikenal.");
                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }
        System.out.println("Sampai jumpa.");
    }

    private void cetakMenu() {
        System.out.println();
        System.out.println("=== ARSIP PUTUSAN NARKOTIKA (jumlah: " + service.jumlah() + ") ===");
        System.out.println(" 1. Impor CSV");
        System.out.println(" 2. Tampil semua");
        System.out.println(" 3. Tambil Detail 1 Data");
        System.out.println(" 4. Tambah putusan");
        System.out.println(" 5. Hapus putusan");
        System.out.println(" 6. Cari (idKasus / nomor perkara / idTerdakwa)");
        System.out.println(" 7. Filter");
        System.out.println(" 8. Agregasi");
        System.out.println(" 9. Urutkan berdasarkan vonis");
        System.out.println("10. Riwayat audit");
        System.out.println("11. Ekspor CSV");
        System.out.println(" 0.  Keluar");
    }

    private void impor() {
        ArrayDinamis<String> galat = new ArrayDinamis<>();
        ArrayDinamis<Putusan> hasil;
        try {
            hasil = importer.importCsv(FILE_CSV, galat);
        } catch (IOException e) {
            System.out.println("Gagal membuka " + FILE_CSV + ": " + e.getMessage());
            audit.catat("IMPOR-GAGAL", e.getMessage());
            return;
        }
        int masuk = 0;
        for (int i = 0; i < hasil.size(); i++) {
            Putusan p = hasil.get(i);
            try {
                service.tambah(p);
                masuk++;
            } catch (IllegalArgumentException e) {
                galat.add("Putusan " + p.getIdKasus() + ": " + e.getMessage());
            }
        }
        System.out.println("Record valid dimuat : " + masuk);
        System.out.println("Record ditolak      : " + galat.size());
        for (int i = 0; i < galat.size(); i++) {
            System.out.println("  - " + galat.get(i));
        }
        System.out.println("Total di arsip      : " + service.jumlah());
        if (service.jumlah() != 50) {
            System.out.println("Peringatan: tugas mewajibkan tepat 50 record valid.");
        }
        service.getArsip().validateInvariant();
        audit.catat("IMPOR", masuk + " dimuat, " + galat.size() + " ditolak");
    }

    private void tampil() {
        System.out.println(service.tampilSemua());
        audit.catat("TAMPIL", service.jumlah() + " putusan");
    }

    private void detailSatuData() {
        if (service.jumlah() == 0) {
            System.out.println("Arsip kosong. Impor data dulu (menu 1).");
            return;
        }
        int pos = bacaInt("Nomor urut data (0 sampai " + (service.jumlah() - 1) + "): ");
        if (pos < 0 || pos >= service.jumlah()) {
            System.out.println("Ditolak: nomor urut di luar rentang.");
            audit.catat("DETAIL-DITOLAK", "posisi " + pos + " di luar rentang");
            return;
        }

        Putusan p = service.ambil(pos);
        System.out.println();
        System.out.println("=== DETAIL PUTUSAN (posisi " + pos + ") ===");
        System.out.println(" 1. idKasus              : " + nilai(p.getIdKasus()));
        System.out.println(" 2. Nomor perkara        : " + nilai(p.getNoPerkara()));
        System.out.println(" 3. Pengadilan           : " + nilai(p.getPengadilan()));
        System.out.println(" 4. Tanggal putusan      : " + (p.getTanggalPutusan() == null ? "(kosong)" : p.getTanggalPutusan()));
        System.out.println(" 5. Kode terdakwa        : " + nilai(p.getIdTerdakwa()));
        System.out.println(" 6. Jenis narkotika      : " + nilai(p.getJenisNarkotika()));
        System.out.println(" 7. Berat barang bukti   : " + p.getBeratBarangBuktiGram() + " gram");
        System.out.println(" 8. Pasal dakwaan        : " + nilai(p.getPasalDakwaan()));
        System.out.println(" 9. Pasal terbukti       : " + nilai(p.getPasalTerbukti()));
        System.out.println("10. Tuntutan             : " + p.getTuntutanBulan() + " bulan");
        System.out.println("11. Vonis                : " + p.getVonisBulan() + " bulan");
        System.out.println("12. Denda                : Rp" + p.getDendaRupiah());
        System.out.println("13. Barang bukti         : " + nilai(p.getBarangBukti()));
        System.out.println("14. Keadaan memberatkan  : " + nilai(p.getKeadaanMemberatkan()));
        System.out.println("15. Keadaan meringankan  : " + nilai(p.getKeadaanMeringankan()));
        System.out.println("16. Halaman sumber       : " + nilai(p.getHalSumber()));

        audit.catat("DETAIL", p.getIdKasus() + " (posisi " + pos + ")");
    }

    private String nilai(String teks) {
        if (teks == null || teks.isBlank()) {
            return "(kosong)";
        }
        return teks;
    }

    private void tambah() {
        String id = bacaTeks("idKasus: ");
        String noPerkara = bacaTeks("Nomor perkara: ");
        String pengadilan = bacaTeks("Pengadilan: ");
        LocalDate tanggal;
        try {
            tanggal = LocalDate.parse(bacaTeks("Tanggal putusan (yyyy-MM-dd): "));
        } catch (DateTimeParseException e) {
            System.out.println("Ditolak: format tanggal tidak valid.");
            audit.catat("TAMBAH-DITOLAK", id + " (tanggal tidak valid)");
            return;
        }
        String terdakwa = bacaTeks("Kode terdakwa: ");
        String jenis = bacaTeks("Jenis narkotika: ");
        double berat = bacaDouble("Berat barang bukti gram (0 jika tidak ada): ");
        String pasalDakwaan = bacaTeks("Pasal dakwaan: ");
        String pasalTerbukti = bacaTeks("Pasal terbukti: ");
        int tuntutan = bacaInt("Tuntutan (bulan): ");
        int vonis = bacaInt("Vonis (bulan): ");
        long denda = bacaLong("Denda (rupiah): ");
        String barangBukti = bacaTeks("Barang bukti: ");
        String memberatkan = bacaTeks("Keadaan memberatkan: ");
        String meringankan = bacaTeks("Keadaan meringankan: ");
        String halSumber = bacaTeks("Halaman sumber: ");
        try {
            Putusan p = new Putusan(id, noPerkara, pengadilan, tanggal, terdakwa, jenis, berat,
                    pasalDakwaan, pasalTerbukti, tuntutan, vonis, denda,
                    barangBukti, memberatkan, meringankan, halSumber);
            service.tambah(p);
            service.getArsip().validateInvariant();
            System.out.println("Berhasil ditambah. Jumlah sekarang: " + service.jumlah());
            audit.catat("TAMBAH", id);
        } catch (IllegalArgumentException e) {
            System.out.println("Ditolak: " + e.getMessage());
            audit.catat("TAMBAH-DITOLAK", id + " (" + e.getMessage() + ")");
        }
    }

    private void hapus() {
        if (service.jumlah() == 0) {
            System.out.println("Arsip kosong.");
            return;
        }
        int pos = bacaInt("Posisi yang dihapus (0 sampai " + (service.jumlah() - 1) + "): ");
        try {
            Putusan p = service.hapus(pos);
            service.getArsip().validateInvariant();
            System.out.println("Dihapus: " + p.getIdKasus() + ". Elemen sesudahnya bergeser satu posisi ke kiri. Sisa: " + service.jumlah());
            audit.catat("HAPUS", p.getIdKasus() + " dari posisi " + pos);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Ditolak: " + e.getMessage());
        }
    }

    private void cari() {
        System.out.println("1. idKasus  2. Nomor perkara  3. idTerdakwa");
        int jenis = bacaInt("Cari berdasarkan: ");
        if (jenis < 1 || jenis > 3) {
            System.out.println("Pilihan tidak dikenal.");
            return;
        }
        String kunci = bacaTeks("Kata kunci (persis sama): ");
        int pos;
        if (jenis == 1) {
            pos = service.cariIdKasus(kunci);
        } else if (jenis == 2) {
            pos = service.cariNoPerkara(kunci);
        } else {
            pos = service.cariIdTerdakwa(kunci);
        }
        int langkah = service.getLangkahTerakhir();
        if (pos == -1) {
            System.out.println("Tidak ditemukan. Langkah: " + langkah);
        } else {
            System.out.println("Ditemukan di posisi " + pos + ". Langkah: " + langkah);
            System.out.println(service.ambil(pos));
        }
        audit.catat("CARI", kunci + " -> posisi " + pos + ", " + langkah + " langkah");
    }

    private void filterMenu() {
        System.out.println("Kosongkan isian untuk melewati kriteria. Semua kriteria terisi harus terpenuhi.");
        FilterService.Kriteria k = new FilterService.Kriteria();
        String jenis = bacaTeks("Jenis narkotika (mengandung): ");
        k.jenisNarkotika = jenis.isEmpty() ? null : jenis;
        k.beratMin = bacaDoubleOpsional("Berat minimum (gram): ");
        k.beratMaks = bacaDoubleOpsional("Berat maksimum (gram): ");
        String pasal = bacaTeks("Pasal terbukti (mengandung): ");
        k.pasalTerbukti = pasal.isEmpty() ? null : pasal;
        k.vonisMin = bacaIntOpsional("Vonis minimum (bulan): ");
        k.vonisMaks = bacaIntOpsional("Vonis maksimum (bulan): ");
        k.dendaMin = bacaLongOpsional("Denda minimum (rupiah): ");
        k.dendaMaks = bacaLongOpsional("Denda maksimum (rupiah): ");

        ArrayDinamis<Putusan> hasil = filter.filter(k);
        terakhir = hasil;
        System.out.println("Ditemukan " + hasil.size() + " putusan:");
        System.out.println(urutan.tampil(hasil));
        audit.catat("FILTER", hasil.size() + " hasil dari " + service.jumlah());
    }

    private void agregasiMenu() {
        ArrayDinamis<Putusan> data = service.getArsip();
        String sumber = "seluruh arsip";
        if (terakhir != null) {
            String p = bacaTeks("Hitung dari (1) seluruh arsip, (2) hasil filter/urutan terakhir [1]: ");
            if (p.equals("2")) {
                data = terakhir;
                sumber = "hasil terakhir";
            }
        }
        System.out.println("Sumber data: " + sumber);
        System.out.println(agregasi.ringkasan(data));
        audit.catat("AGREGASI", sumber);
    }

    private void urutMenu() {
        String arah = bacaTeks("Urutan vonis (1) naik, (2) turun [1]: ");
        boolean naik = !arah.equals("2");
        ArrayDinamis<Putusan> hasil = urutan.urutVonis(naik);
        terakhir = hasil;
        System.out.println(urutan.tampil(hasil));
        System.out.println("Langkah pengurutan: " + urutan.getLangkahTerakhir());
        service.getArsip().validateInvariant();
        System.out.println("Arsip asli tidak berubah urutannya (yang diurutkan hanya salinan).");
        audit.catat("URUT", naik ? "vonis naik" : "vonis turun");
    }

    private void auditMenu() {
        System.out.println("1. Tampil riwayat (terbaru di atas)  2. Aksi terakhir");
        int p = bacaInt("Pilih: ");
        if (p == 1) {
            System.out.println(audit.tampilRiwayat());
        } else if (p == 2) {
            System.out.println(audit.aksiTerakhir());
        } else {
            System.out.println("Pilihan tidak dikenal.");
        }
    }

    private void ekspor() {
        ArrayDinamis<Putusan> data = service.getArsip();
        String sumber = "seluruh arsip";
        if (terakhir != null) {
            String p = bacaTeks("Ekspor (1) seluruh arsip, (2) hasil terakhir [1]: ");
            if (p.equals("2")) {
                data = terakhir;
                sumber = "hasil terakhir";
            }
        }
        String nama = bacaTeks("Nama file [hasil-ekspor.csv]: ");
        if (nama.isEmpty()) {
            nama = "hasil-ekspor.csv";
        }
        try {
            exporter.exportCsv(data, nama);
            System.out.println(data.size() + " baris (" + sumber + ") disimpan ke " + nama);
            audit.catat("EKSPOR", sumber + " -> " + nama);
        } catch (IOException | RuntimeException e) {
            System.out.println("Gagal ekspor: " + e.getMessage());
        }
    }



    private void laporCari(String keterangan, String id) {
        int pos = service.cariIdKasus(id);
        System.out.println("Cari " + keterangan + " [" + id + "]: posisi " + pos + ", langkah " + service.getLangkahTerakhir());
    }

    private String bacaTeks(String tampilkan) {
        System.out.print(tampilkan);
        return in.nextLine().trim();
    }

    private int bacaInt(String tampilkan) {
        while (true) {
            try {
                return Integer.parseInt(bacaTeks(tampilkan));
            } catch (NumberFormatException e) {
                System.out.println("Masukkan bilangan bulat.");
            }
        }
    }

    private long bacaLong(String tampilkan) {
        while (true) {
            try {
                return Long.parseLong(bacaTeks(tampilkan));
            } catch (NumberFormatException e) {
                System.out.println("Masukkan bilangan bulat.");
            }
        }
    }

    private double bacaDouble(String tampilkan) {
        while (true) {
            try {
                return Double.parseDouble(bacaTeks(tampilkan));
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka (pakai titik untuk desimal).");
            }
        }
    }

    private Integer bacaIntOpsional(String tampilkan) {
        while (true) {
            String s = bacaTeks(tampilkan);
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Integer.valueOf(s);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan bilangan bulat atau kosongkan.");
            }
        }
    }

    private Long bacaLongOpsional(String tampilkan) {
        while (true) {
            String s = bacaTeks(tampilkan);
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Long.valueOf(s);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan bilangan bulat atau kosongkan.");
            }
        }
    }

    private Double bacaDoubleOpsional(String tampilkan) {
        while (true) {
            String s = bacaTeks(tampilkan);
            if (s.isEmpty()) {
                return null;
            }
            try {
                return Double.valueOf(s);
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka atau kosongkan.");
            }
        }
    }
}