package id.ac.umm.strukturdata.model;
import java.time.LocalDate;

public class Putusan {
    private final String idKasus;
    private final String noPerkara;
    private final String pengadilan;
    private final LocalDate tanggalPutusan;
    private final String idTerdakwa;
    private final String jenisNarkotika;
    private final double beratBarangBuktiGram;
    private final String pasalDakwaan;
    private final String pasalTerbukti;
    private final int tuntutanBulan;
    private final int vonisBulan;
    private final long dendaRupiah;
    private final String barangBukti;
    private final String keadaanMemberatkan;
    private final String keadaanMeringankan;
    private final String halSumber;

    public Putusan(String idKasus, String noPerkara, String pengadilan, LocalDate tanggalPutusan, String idTerdakwa,
                   String jenisNarkotika, double beratBarangBuktiGram, String pasalDakwaan, String pasalTerbukti,
                   int tuntutanBulan, int vonisBulan, long dendaRupiah, String barangBukti, String keadaanMemberatkan,
                   String keadaanMeringankan, String halSumber) {
        if (idKasus == null || idKasus.isBlank()) {
            throw new IllegalArgumentException("caseId tidak boleh kosong");
        }
        this.idKasus = idKasus;
        this.noPerkara = noPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.idTerdakwa = idTerdakwa;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.halSumber = halSumber;
    }
}