package id.ac.umm.strukturdata.io;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.ArrayDinamis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class CsvImporter {

    public ArrayDinamis<Putusan> importCsv(String path)
            throws IOException {

        ArrayDinamis<Putusan> daftarPutusan = new ArrayDinamis<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(path))) {

            String baris;
            boolean header = true;
            int nomorBaris = 0;

            while ((baris = reader.readLine()) != null) {
                nomorBaris++;

                if (baris.isBlank()) {
                    continue;
                }

                if (header) {
                    header = false;
                    continue;
                }

                try {
                    String[] kolom = penguraibarisCsv(baris);
                    if (kolom.length != 16) {
                        throw new IllegalArgumentException("Jumlah kolom harus 16, ditemukan " + kolom.length);
                    }

                    Putusan putusan = new Putusan(
                            kolom[0],
                            kolom[1],
                            kolom[2],
                            LocalDate.parse(kolom[3]),
                            kolom[4],
                            kolom[5],
                            Double.parseDouble(kolom[6]),
                            kolom[7],
                            kolom[8],
                            Integer.parseInt(kolom[9]),
                            Integer.parseInt(kolom[10]),
                            Long.parseLong(kolom[11]),
                            kolom[12],
                            kolom[13],
                            kolom[14],
                            kolom[15]
                    );
                    daftarPutusan.add(putusan);

                } catch (RuntimeException e) {
                    throw new IOException("Gagal membaca baris " + nomorBaris + ": " + e.getMessage(), e);
                }
            }
        }
        return daftarPutusan;
    }

    private String[] penguraibarisCsv(String line) {
        String[] hasil = new String[16];
        StringBuilder kolom = new StringBuilder();

        boolean dalamKutipan = false;
        int jumlahKolom = 0;
        for (int i = 0; i < line.length(); i++) {
            char karakter = line.charAt(i);
            if (karakter == '"') {
                if (dalamKutipan && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    kolom.append('"');
                    i++;
                } else {
                    dalamKutipan = !dalamKutipan;
                }

            } else if (karakter == ',' && !dalamKutipan) {

                if (jumlahKolom >= hasil.length) {
                    throw new IllegalArgumentException("Jumlah kolom melebihi 16");
                }
                hasil[jumlahKolom++] = kolom.toString();
                kolom.setLength(0);

            } else {
                kolom.append(karakter);
            }
        }

        if (dalamKutipan) {
            throw new IllegalArgumentException("Tanda kutip CSV tidak berpasangan");
        }

        if (jumlahKolom >= hasil.length) {
            throw new IllegalArgumentException("Jumlah kolom melebihi 16");
        }

        hasil[jumlahKolom++] = kolom.toString();

        if (jumlahKolom != 16) {
            throw new IllegalArgumentException("Jumlah kolom harus 16, ditemukan " + jumlahKolom);
        }
        return hasil;
    }
}
