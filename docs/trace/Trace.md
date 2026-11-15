# Trace Manual: ArrayDinamis dan Stack

Trace ini menelusuri kode `ArrayDinamis<T>` dan `Stack<T>` baris demi baris. Seluruh isi array dan angka pada tabel sudah dicocokkan dengan hasil menjalankan kode yang sama.

## Cara membaca

- Kapasitas awal memakai **4** lewat constructor `new ArrayDinamis<>(4)` dan `new Stack<>(4)` agar resize cepat terlihat. Program sebenarnya memakai kapasitas awal **26**.
- `[_]` berarti slot berisi `null`. Semua array ditampilkan lengkap sesuai kapasitasnya.
- Data array: A, B, C, D, E mewakili `ID-0001` sampai `ID-0005`.
- Data stack: `#1`, `#2`, `#3`, ... mewakili entri riwayat audit (`#1 IMPOR`, `#2 TAMPIL`, dst.).
- Rumus pergeseran: `remove(pos)` menggeser `size - 1 - pos` elemen, `insert(pos)` menggeser `size - pos` elemen.

---

# Bagian I. ArrayDinamis

## 1. Penambahan sampai resize

`add` memanggil `pastikanKapasitas(size + 1)`. Bila `size + 1 > data.length`, dipanggil `resize(max(data.length * 2, size + 1))`.

| Langkah | Isi data | size | kapasitas |
|---|---|---|---|
| awal | `[_][_][_][_]` | 0 | 4 |
| add(A) | `[A][_][_][_]` | 1 | 4 |
| add(B) | `[A][B][_][_]` | 2 | 4 |
| add(C) | `[A][B][C][_]` | 3 | 4 |
| add(D) | `[A][B][C][D]` | 4 | 4 |
| add(E) | lihat rincian di bawah | 5 | 8 |

Rincian `add(E)`:

1. `pastikanKapasitas(5)`: 5 > 4, jadi `resize(max(8, 5))` = `resize(8)`.
2. `resize` membuat array baru berukuran 8 dan menyalin 4 elemen lama: `[A][B][C][D][_][_][_][_]`.
3. `data[4] = E`, `size = 5`: `[A][B][C][D][E][_][_][_]`.

Invariant: slot indeks 5 sampai 7 bernilai `null`, `size` 5 <= kapasitas 8. **Lolos.**

## 2. Hapus elemen pertama: `remove(0)`

Awal: `[A][B][C][D][E][_][_][_]`, size 5. Perulangan berjalan selama `i < size - 1` (yaitu `i < 4`).

| i | operasi | isi data |
|---|---|---|
| 0 | `data[0] = data[1]` | `[B][B][C][D][E][_][_][_]` |
| 1 | `data[1] = data[2]` | `[B][C][C][D][E][_][_][_]` |
| 2 | `data[2] = data[3]` | `[B][C][D][D][E][_][_][_]` |
| 3 | `data[3] = data[4]` | `[B][C][D][E][E][_][_][_]` |
| - | `data[size - 1] = null` | `[B][C][D][E][_][_][_][_]` |

Hasil: A dikembalikan, `size` menjadi 4, **4 elemen bergeser** (5 - 1 - 0). Invariant lolos.

## 3. Hapus elemen tengah: `remove(2)`

Awal: `[B][C][D][E][_][_][_][_]`, size 4. Perulangan berjalan selama `i < 3`.

| i | operasi | isi data |
|---|---|---|
| 2 | `data[2] = data[3]` | `[B][C][E][E][_][_][_][_]` |
| - | `data[size - 1] = null` | `[B][C][E][_][_][_][_][_]` |

Hasil: D dikembalikan, `size` menjadi 3, **1 elemen bergeser** (4 - 1 - 2).

## 4. Hapus elemen terakhir: `remove(2)`

Awal: `[B][C][E][_][_][_][_][_]`, size 3.

Perulangan tidak berjalan sama sekali karena `i = 2` dan syarat `2 < size - 1 = 2` salah. Langsung `data[2] = null`:

`[B][C][_][_][_][_][_][_]`

Hasil: E dikembalikan, `size` menjadi 2, **0 elemen bergeser** (3 - 1 - 2). Invariant lolos.

## 5. Tambah kembali dengan `insert` (kebalikan bagian 2 sampai 4)

Tiga record dikembalikan dengan urutan terbalik dari penghapusan: E, lalu D, lalu A. Dengan urutan ini setiap record kembali ke posisi aslinya.

Awal: `[B][C][_][_][_][_][_][_]`, size 2. Perulangan `insert` berjalan **dari belakang**: `for (i = size; i > position; i--) data[i] = data[i - 1]`.

**a. `insert(2, E)`**: posisi sama dengan `size`, jadi perulangan tidak berjalan (0 geseran).

| langkah | isi data | size |
|---|---|---|
| `data[2] = E` | `[B][C][E][_][_][_][_][_]` | 3 |

**b. `insert(2, D)`**: 1 geseran.

| i | operasi | isi data |
|---|---|---|
| 3 | `data[3] = data[2]` | `[B][C][E][E][_][_][_][_]` |
| - | `data[2] = D` | `[B][C][D][E][_][_][_][_]` |

size menjadi 4.

**c. `insert(0, A)`**: 4 geseran.

| i | operasi | isi data |
|---|---|---|
| 4 | `data[4] = data[3]` | `[B][C][D][E][E][_][_][_]` |
| 3 | `data[3] = data[2]` | `[B][C][D][D][E][_][_][_]` |
| 2 | `data[2] = data[1]` | `[B][C][C][D][E][_][_][_]` |
| 1 | `data[1] = data[0]` | `[B][B][C][D][E][_][_][_]` |
| - | `data[0] = A` | `[A][B][C][D][E][_][_][_]` |

size menjadi 5. Isi akhir sama dengan isi awal bagian 1. Invariant lolos.

**Mengapa penggeseran dari belakang?** Bila dari depan, `data[1] = data[0]` menimpa B sebelum B sempat disalin ke `data[2]`, sehingga A akan menyalin dirinya ke seluruh slot sesudahnya dan data hilang.

## 6. Pencarian linear dengan jumlah langkah

Isi: `[A][B][C][D][E]`, size 5. `search` menambah `langkahTerakhir` pada setiap elemen yang diperiksa.

| Cari | langkah | hasil | keterangan |
|---|---|---|---|
| A | 1 | posisi 0 | elemen pertama, kasus terbaik |
| E | 5 | posisi 4 | elemen terakhir, kasus batas |
| Z | 5 | -1 | tidak ada, seluruh elemen diperiksa |

Aplikasi memakai `PutusanService.cariIdKasus`, yang menghitung langkah dengan cara yang sama dan melaporkannya pada menu Cari dan skenario 2.

## 7. Kasus batas (boundary)

Isi: `[A][B][C][D][E]`, size 5. Semua kasus ditolak dengan `IndexOutOfBoundsException` dan struktur tidak berubah.

| Panggilan | Pesan kesalahan |
|---|---|
| `insert(6, X)` | `Posisi insert di luar rentang: 6, size=5` |
| `remove(5)` | `Posisi di luar rentang: 5, size=5` |
| `get(-1)` | `Posisi di luar rentang: -1, size=5` |

Catatan: `insert(5, X)` (posisi sama dengan `size`) **sah**, karena berarti menambah di akhir.

## 8. Resize pada program sebenarnya

Kapasitas awal 26. Elemen ke-26 masih muat. Pada elemen ke-27 terjadi `resize(max(52, 27))` = `resize(52)`: 26 elemen lama disalin ke array baru.

| Elemen ke- | kapasitas |
|---|---|
| 26 | 26 |
| 27 | 52 |
| 50 | 52 |

Untuk 50 data, resize terjadi **satu kali**.

---

# Bagian II. Stack

Stack menyimpan elemen dari indeks 0 ke atas. **Puncak** adalah elemen paling kanan yang terisi, di indeks `size - 1`. `get(position)` membaca dari puncak: `data[size - 1 - position]`.

## 9. `push` sampai resize

Memakai `new Stack<>(4)`. `push` menolak `null`, memanggil `resize(data.length * 2)` bila `size == data.length`, lalu `data[size++] = value`.

| Langkah | Isi data | size | puncak |
|---|---|---|---|
| push(#1) | `[#1][_][_][_]` | 1 | #1 |
| push(#2) | `[#1][#2][_][_]` | 2 | #2 |
| push(#3) | `[#1][#2][#3][_]` | 3 | #3 |
| push(#4) | `[#1][#2][#3][#4]` | 4 | #4 |
| push(#5) | `size == 4 == data.length`, maka `resize(8)`, lalu `[#1][#2][#3][#4][#5][_][_][_]` | 5 | #5 |

## 10. `peek`, `get`, dan `pop`

Isi: `[#1][#2][#3][#4][#5][_][_][_]`, size 5.

| Operasi | hasil | isi data setelahnya |
|---|---|---|
| `peek()` | #5 | tidak berubah |
| `get(0)` | #5 | tidak berubah |
| `get(1)` | #4 | tidak berubah |
| `get(4)` | #1 | tidak berubah |
| `pop()` | #5 | `[#1][#2][#3][#4][_][_][_][_]`, size 4 |

`get(0)` sampai `get(size - 1)` membaca dari yang terbaru ke yang terlama. Itulah sebabnya `AuditService.tampilRiwayat()` menampilkan riwayat dengan **terbaru di atas**.

## 11. `searchPreserve` dan `restore`

Isi stack: `[#1][#2][#3][#4]`, size 4. Cari `#2`. Stack bantu dibuat dengan kapasitas yang sama dengan stack utama (8). Variabel `jarak` dimulai dari 0 dan bertambah setiap elemen yang bukan target.

| langkah | pop | isi stack | isi bantu | pemeriksaan |
|---|---|---|---|---|
| 1 | #4 | `[#1][#2][#3]` | `[#4]` | #4 bukan #2, jarak menjadi 1 |
| 2 | #3 | `[#1][#2]` | `[#4][#3]` | #3 bukan #2, jarak menjadi 2 |
| 3 | #2 | `[#1]` | `[#4][#3][#2]` | #2 sama dengan #2, `hasil = 2`, berhenti |

Blok `finally` memanggil `restore()`: elemen bantu di-pop satu per satu lalu di-push kembali ke stack utama.

| restore | isi stack | isi bantu |
|---|---|---|
| pop #2 dari bantu, push | `[#1][#2]` | `[#4][#3]` |
| pop #3 dari bantu, push | `[#1][#2][#3]` | `[#4]` |
| pop #4 dari bantu, push | `[#1][#2][#3][#4]` | kosong |

Hasil: `searchPreserve(#2)` mengembalikan **2** (dua langkah dari puncak), `langkahTerakhir` = **3**, dan isi stack kembali `[#1][#2][#3][#4]`.

**Target tidak ada**, `searchPreserve(#9)`: keempat elemen di-pop satu per satu (4 langkah) tanpa ada yang cocok, hasilnya **-1**. `restore()` tetap dijalankan dan isi stack kembali `[#1][#2][#3][#4]`.

Invariant setelah pencarian: `validateInvariant()` memeriksa bahwa stack bantu **kosong**. **Lolos.**

## 12. Kasus kesalahan Stack

| Panggilan | Pengecualian dan pesan |
|---|---|
| `pop()` pada stack kosong | `IllegalStateException: Stack kosong` |
| `push(null)` | `IllegalArgumentException: Elemen stack tidak boleh null` |
| `remove(1)` | `UnsupportedOperationException: Stack hanya boleh menghapus puncak (posisi 0)` |

---
