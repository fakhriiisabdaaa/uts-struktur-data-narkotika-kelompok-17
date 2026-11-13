package id.ac.umm.strukturdata.io;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvExporter {

    public void exportCsv(
            ArrayDinamis<Putusan> daftarPutusan,
            String path
    ) throws IOException {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(path))) {
            writer.write(
                    "idKasus,noPerkara,pengadilan,tanggalPutusan,idTerdakwa,jenisNarkotika,beratBarangBuktiGram,pasalDakwaan," +
                            "pasalTerbukti,tuntutanBulan,vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan," +
                            "halSumber"
            );

            writer.newLine();
            for (int i = 0; i < daftarPutusan.size(); i++) {

                Putusan p = daftarPutusan.get(i);

                String[] kolom = {
                        p.getIdKasus(),
                        p.getNoPerkara(),
                        p.getPengadilan(),
                        p.getTanggalPutusan().toString(),
                        p.getIdTerdakwa(),
                        p.getJenisNarkotika(),
                        String.valueOf(p.getBeratBarangBuktiGram()),
                        p.getPasalDakwaan(),
                        p.getPasalTerbukti(),
                        String.valueOf(p.getTuntutanBulan()),
                        String.valueOf(p.getVonisBulan()),
                        String.valueOf(p.getDendaRupiah()),
                        p.getBarangBukti(),
                        p.getKeadaanMemberatkan(),
                        p.getKeadaanMeringankan(),
                        p.getHalSumber()
                };

                for (int j = 0; j < kolom.length; j++) {

                    if (j > 0) {
                        writer.write(",");
                    }
                    writer.write(escapeCsv(kolom[j]));
                }

                writer.newLine();
            }
        }
    }

    private String escapeCsv(String value) {

        if (value == null) {
            value = "";
        }
        value = value.replace("\"", "\"\"");
        return "\"" + value + "\"";
    }
}