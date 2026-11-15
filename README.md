# UTS Struktur Data: Arsip Putusan Pengadilan Narkotika

Aplikasi Java berorientasi objek (berbasis menu di konsol) yang menyimpan dan mengolah 50 putusan pengadilan narkotika memakai struktur data yang dibuat dari nol, tanpa koleksi bawaan Java.

Tugas UTS Struktur Data, Fakultas Teknik, Universitas Muhammadiyah Malang, Semester Ganjil 2026/2027. Dosen pengampu: Galih W. Wicaksono.

## Identitas Kelompok

**Kelompok 17**, Kelas [ISI: B / D]

| Nama | NIM | Akun GitHub |
|---|---|---|
| Fakhri Sabda Lazuardi | [202510370110002] | [@fakhriiisabdaaa](https://github.com/fakhriiisabdaaa) |
| Marsciano Tri Julian | [202510370110025] | [@Marsciano](https://github.com/Marsciano) |

Bukti kontribusi masing-masing anggota ada di [CONTRIBUTIONS.md](CONTRIBUTIONS.md).

## Struktur Data yang Dipilih

| Peran | Struktur | Kelas | Dipakai untuk |
|---|---|---|---|
| Utama | Array dinamis | `ArrayDinamis<T>` | Arsip seluruh 50 objek `Putusan` |
| Sekunder | Stack | `Stack<T>` | Riwayat audit operasi pengguna |

Keduanya generik, mengimplementasikan antarmuka `StrukturData<T>`, dan punya `validateInvariant()`.

**ArrayDinamis.** Menyimpan elemen di `Object[]` dengan kapasitas awal 26. Saat penuh, kapasitas digandakan dan elemen lama disalin ke array baru. Metode: `add`, `insert`, `get`, `set`, `remove`, `search`, `contains`, `clear`, `kapasitas`.

**Stack.** Menyimpan elemen di `Object[]` dengan puncak di indeks `size - 1`. Metode: `push`, `pop`, `peek`, `searchPreserve`, `restore`. `searchPreserve` memindahkan elemen ke stack bantu saat mencari lalu `restore` mengembalikannya, sehingga urutan stack tidak berubah.

Alasan pemilihan dan strategi pencarian atau pengurutan ada di `docs/adr/`.

### Batasan implementasi

- Penyimpanan inti tidak memakai `ArrayList`, `LinkedList`, `Stack`, `Vector`, `Queue`, `Deque`, `PriorityQueue`, atau koleksi lain dari `java.util`.
- `java.util` hanya dipakai untuk masukan konsol (`Scanner`); tanggal memakai `java.time`.
- Pengurutan dilakukan pada salinan, sehingga urutan dan invariant arsip utama tidak berubah.

## Fitur

Menu utama aplikasi:

| Menu | Fitur | Keterangan |
|---|---|---|
| 1 | Impor CSV | Membaca CSV, menolak record tidak valid tanpa menghentikan program |
| 2 | Tampil semua | Urutan sama dengan urutan di struktur utama |
| 3 | Tampil Detail 1 Data | Menampilkan seluruh isi data secara lengkap |
| 4 | Tambah putusan | Validasi ID kosong dan ID ganda |
| 5 | Hapus putusan | Berdasarkan posisi, elemen sesudahnya bergeser |
| 6 | Cari | Berdasarkan `idKasus`, nomor perkara, atau `idTerdakwa`, dengan jumlah langkah |
| 7 | Filter | Jenis narkotika, rentang berat, pasal terbukti, rentang vonis, rentang denda |
| 8 | Agregasi | Jumlah perkara, rata-rata vonis, total denda, distribusi jenis narkotika |
| 9 | Urutkan | Berdasarkan vonis, naik atau turun |
| 10 | Riwayat audit | Disimpan di `Stack`, terbaru di atas |
| 11 | Ekspor CSV | Seluruh arsip atau hasil filter/urutan terakhir |
| 12 | Demo skenario | Menjalankan skenario pengujian 4.1 secara berurutan |
| 0 | Keluar | |

## Struktur Proyek

```
uts-struktur-data-narkotika-kelompok-17/
|-- src/main/java/id/ac/umm/strukturdata/
|   |-- app/        Main
|   |-- io/         CsvImporter, CsvExporter
|   |-- model/      Putusan
|   |-- service/    PutusanService, FilterService, UrutanService,
|   |               AgregasiService, AuditService
|   `-- struktur/   StrukturData, ArrayDinamis, Stack
|-- src/test/java/id/ac/umm/strukturdata/
|   |-- skenario/   pengujian tujuh skenario bagian 4.1
|   `-- struktur/   pengujian ArrayDinamis dan Stack
|-- data/putusan-50.csv
|-- docs/
|   |-- data-provenance.csv
|   |-- adr/
|   |-- trace/
|   `-- demo/
|-- AI_USAGE.md
|-- CONTRIBUTIONS.md
|-- README.md
`-- pom.xml
```

## Dataset

Dataset berisi 50 putusan dari pengadilan negeri yang berbeda, diambil dari [putusan3.mahkamahagung.go.id](https://putusan3.mahkamahagung.go.id). Berkas PDF sumber tidak disimpan di repository. Nama terdakwa disamarkan dengan kode anonim.

Atribut setiap putusan (`model/Putusan`): `idKasus`, `noPerkara`, `pengadilan`, `tanggalPutusan`, `idTerdakwa`, `jenisNarkotika`, `beratBarangBuktiGram`, `pasalDakwaan`, `pasalTerbukti`, `tuntutanBulan`, `vonisBulan`, `dendaRupiah`, `barangBukti`, `keadaanMemberatkan`, `keadaanMeringankan`, `halSumber`.

Aturan data:

- Nilai yang tidak ditemukan di putusan ditulis kosong (0 atau null), tidak diperkirakan.
- Aturan agregasi berat barang bukti: [ISI: tulis aturan yang dipakai, misalnya apakah berat netto dijumlahkan bila ada beberapa barang bukti].
- Sumber tiap atribut penting dicatat di [`docs/data-provenance.csv`](docs/data-provenance.csv), satu baris per atribut per putusan, lengkap dengan extractor dan reviewer.

## Prasyarat

- JDK [ISI: versi sesuai `pom.xml`]. Kode memakai `switch` berbentuk panah, jadi minimal JDK 14.
- Apache Maven 3.6 atau lebih baru.

## Build

Jalankan dari folder utama proyek:

```
mvn clean compile
```

## Test

```
mvn test
```

Tes otomatis ada di `src/test/java`: paket `struktur` menguji `ArrayDinamis` dan `Stack` (termasuk `validateInvariant()`), dan paket `skenario` menguji tujuh skenario bagian 4.1 panduan.

## Menjalankan

Jalankan **dari folder utama proyek**, karena aplikasi membaca file CSV lewat path relatif `data/...`:

```
mvn clean compile
java -cp target/classes id.ac.umm.strukturdata.app.Main
```

Bisa juga dijalankan dari IntelliJ IDEA lewat tombol Run pada kelas `Main`.

### Contoh penggunaan

1. Pilih `1` (Impor CSV). Keluaran yang diharapkan: `Record valid dimuat : 50` dan `Total di arsip : 50`.
2. Pilih `5` (Cari), pilih `1` (idKasus), lalu ketik `ID-0001`. Aplikasi menampilkan posisi dan jumlah langkah pencarian.
3. Pilih `6` (Filter), isi jenis narkotika `sabu`, berat minimum `1`, berat maksimum `5`, pasal terbukti `114`, vonis minimum `60`, vonis maksimum `72`, lalu kosongkan isian lain dengan Enter.
4. Pilih `7` (Agregasi) untuk melihat ringkasan seluruh arsip atau hasil filter.
5. Pilih `9` (Riwayat audit), lalu `1` untuk melihat riwayat dengan aksi terbaru di atas.
6. Pilih `11` (Demo skenario) untuk menjalankan skenario 1 sampai 7 berurutan.

## Skenario Pengujian

Skenario bagian 4.1 panduan bisa dijalankan lewat menu 11 maupun lewat tes otomatis.

| Skenario | Yang dibuktikan | Bukti |
|---|---|---|
| 1 | Setelah impor, size bernilai 50 | menu 12, pada class PengujianTest |
| 2 | Cari record yang ada, posisi batas, dan yang tidak ada, dengan jumlah langkah | menu 12, pada class PengujianTest |
| 3 | Hapus pertama, tengah, terakhir; terlihat pergeseran elemen | menu 12, pada class PengujianTest |
| 4 | Tambah kembali tiga record; invariant lolos dan urutan sama seperti semula | menu 12, pada class PengujianTest |
| 5 | Filter gabungan, dicocokkan dengan hitung ulang dan `docs/data-provenance.csv` | menu 12, pada class PengujianTest |
| 6 | Pencarian pada Stack tidak mengubah urutan | menu 12, pada class PengujianTest |
| 7 | Record tidak valid ditolak dan struktur tetap konsisten | menu 12, pada class PengujianTest |

Hasil demo disimpan di [`docs/demo/`](docs/demo/).

## Dokumentasi

| Berkas | Isi |
|---|---|
| [`docs/adr/`](docs/adr/) | Keputusan desain: pemilihan struktur, strategi pencarian dan pengurutan |
| [`docs/trace/`](docs/trace/) | Trace manual `ArrayDinamis` dan `Stack` |
| [`docs/data-provenance.csv`](docs/data-provenance.csv) | Sumber dan pemeriksa tiap atribut penting |
| [`CONTRIBUTIONS.md`](CONTRIBUTIONS.md) | Issue, PR, review, dan commit utama tiap anggota |
| [`AI_USAGE.md`](AI_USAGE.md) | Catatan penggunaan AI |

## Alur Kerja Git

- Branch `main` hanya menerima perubahan lewat pull request.
- Setiap fitur dimulai dari GitHub Issue, dikerjakan di branch `feature/<nomor-issue>-<nama>`, lalu digabung lewat PR yang direview anggota lain.
- Tag final: `uts-v1.0`.
