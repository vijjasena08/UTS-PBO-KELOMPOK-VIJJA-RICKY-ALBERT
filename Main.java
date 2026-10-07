import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Menu> daftarMenu = new ArrayList<>();       // data menu
    static ArrayList<Pesanan> riwayatPesanan = new ArrayList<>(); // data pesanan
    static int counterStruk = 119;

    static final String FILE_MENU = "data_menu.txt";
    static final String FILE_PESANAN = "data_pesanan.txt";

    public static void main(String[] args) {
        bacaMenu();                                  // ambil data menu dari txt
        counterStruk += hitungJumlahPesananTersimpan();
        int pilih;
        do {
            System.out.println("\n===== PROGRAM STRUK RUMAH VIKING =====");
            System.out.println("1. Lihat Menu");
            System.out.println("2. Tambah Menu");
            System.out.println("3. Buat Pesanan");
            System.out.println("4. Cetak Struk Terakhir");
            System.out.println("5. Lihat Riwayat Pesanan");
            System.out.println("0. Keluar");
            pilih = inputInt("Pilih: ");
            switch (pilih) {
                case 1: lihatMenu(); break;
                case 2: tambahMenu(); break;
                case 3: buatPesanan(); break;
                case 4: cetakStrukTerakhir(); break;
                case 5: lihatRiwayat(); break;
                case 0: System.out.println("Terima kasih!"); break;
                default: System.out.println("Pilihan tidak valid.");
            }
        } while (pilih != 0);
    }

    // data menu awal diambil dari struk Rumah Viking
    static void isiMenuAwal() {
        daftarMenu.add(new Makanan("Nasi Gunting Ayam Jahe", 42000, 0, "Nasi"));
        daftarMenu.add(new Makanan("Nasi Lemak Ayam Gulai", 45000, 0, "Nasi"));
        daftarMenu.add(new Makanan("Nasi Ayam Gunting Kari", 42000, 0, "Nasi"));
        daftarMenu.add(new Makanan("Mie Ayam", 32000, 0, "Mie"));
        daftarMenu.add(new Makanan("Mee Pok", 38000, 0, "Mie"));
        daftarMenu.add(new Makanan("Chilli Pan Mee", 38000, 0, "Mie"));
        daftarMenu.add(new Minuman("Teh O Dingin", 8000, 0, "Dingin"));
        daftarMenu.add(new Minuman("Yuenyeung Dingin", 25000, 0, "Dingin"));
        daftarMenu.add(new Minuman("Milo Dinosaurus Dingin", 23000, 0, "Dingin"));
    }

    static void lihatMenu() {
        System.out.println("\n--------------- DAFTAR MENU ---------------");
        System.out.printf("%-3s %-26s %-8s %10s%n", "No", "Nama", "Kategori", "Harga");
        for (int i = 0; i < daftarMenu.size(); i++) {
            Menu m = daftarMenu.get(i);
            System.out.printf("%-3d %-26s %-8s %10s%n", i + 1, m.getNama(), m.getKategori(), rp(m.getHarga()));
        }
    }

    static void tambahMenu() {
        System.out.println("\nKategori: 1. Makanan  2. Minuman");
        int k = inputInt("Pilih kategori: ");
        if (k != 1 && k != 2) {
            System.out.println("Kategori tidak valid.");
            return;
        }
        String nama = inputString("Nama menu: ");
        float harga = inputFloat("Harga: ");
        if (k == 1) {
            String jenis = inputString("Jenis (Nasi/Mie/dll): ");
            daftarMenu.add(new Makanan(nama, harga, 0, jenis));
        } else {
            String suhu = inputString("Suhu (Dingin/Panas): ");
            daftarMenu.add(new Minuman(nama, harga, 0, suhu));
        }
        simpanMenu();
        System.out.println("Menu berhasil ditambahkan dan disimpan ke " + FILE_MENU);
    }

    static void buatPesanan() {
        // data pelanggan
        System.out.println("\n--- Data Pelanggan ---");
        String nama = inputString("Nama pelanggan: ");
        int meja = inputInt("Nomor meja: ");
        int tp = inputInt("Tipe pesanan (1. DINE IN  2. TAKE AWAY): ");
        String tipe = (tp == 2) ? "TAKE AWAY" : "DINE IN";
        Pelanggan pelanggan = new Pelanggan(nama, meja, tipe);

        String tgl = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Date());
        String noStruk = "RMSJ" + new SimpleDateFormat("yyyyMMdd").format(new Date())
                + String.format("%04d", ++counterStruk);
        String salesNo = "SRMSJ" + System.currentTimeMillis();
        Pesanan pesanan = new Pesanan(noStruk, salesNo, "Rumah Sejoli", tgl, "KASIR PC", pelanggan, null);

        // pilih menu
        while (true) {
            lihatMenu();
            int no = inputInt("Pilih nomor menu (0 = selesai): ");
            if (no == 0) break;
            if (no < 1 || no > daftarMenu.size()) {
                System.out.println("Nomor menu tidak valid.");
                continue;
            }
            int jumlah = inputInt("Jumlah: ");
            if (jumlah <= 0) {
                System.out.println("Jumlah harus lebih dari 0.");
                continue;
            }
            Menu dipilih = daftarMenu.get(no - 1);
            // buat objek baru supaya jumlah di daftar menu tidak berubah
            if (dipilih instanceof Makanan) {
                Makanan mk = (Makanan) dipilih;
                pesanan.addMenu(new Makanan(mk.getNama(), mk.getHarga(), jumlah, mk.getJenis()));
            } else {
                Minuman mn = (Minuman) dipilih;
                pesanan.addMenu(new Minuman(mn.getNama(), mn.getHarga(), jumlah, mn.getSuhu()));
            }
            System.out.println(dipilih.getNama() + " x" + jumlah + " ditambahkan.");
        }

        if (pesanan.getListMenu().isEmpty()) {
            System.out.println("Pesanan kosong, dibatalkan.");
            counterStruk--;
            return;
        }

        // ringkasan
        pesanan.hitungGrandTotal();
        System.out.println("\nSubtotal       : " + rp(pesanan.getSubtotal()));
        System.out.println("Service Charge : " + rp(pesanan.getServiceCharge()));
        System.out.println("PB1            : " + rp(pesanan.getPb1()));
        System.out.println("Grand Total    : " + rp(pesanan.getGrandTotal()));

        String konfirmasi = inputString("Lanjutkan pesanan? (y = ya / n = batalkan): ");
        if (!konfirmasi.equalsIgnoreCase("y")) {
            System.out.println("Pesanan dibatalkan. Silakan buat pesanan ulang.");
            counterStruk--;
            return;
        }

        // pembayaran
        System.out.println("\nMetode pembayaran: 1. QRIS  2. TUNAI  3. DEBIT");
        int mp = inputInt("Pilih: ");
        Pembayaran bayar;
        if (mp == 2) {
            float uang;
            do {
                uang = inputFloat("Uang dibayar: ");
                if (uang < pesanan.getGrandTotal()) System.out.println("Uang kurang!");
            } while (uang < pesanan.getGrandTotal());
            bayar = new Pembayaran("TUNAI", uang);
        } else {
            String metode = (mp == 3) ? "DEBIT" : "QRIS";
            bayar = new Pembayaran(metode, pesanan.getGrandTotal());
        }
        bayar.hitungKembalian(pesanan.getGrandTotal());
        pesanan.setPembayaran(bayar);

        riwayatPesanan.add(pesanan); // simpan data pesanan
        simpanPesanan(pesanan);      // simpan juga ke file txt
        System.out.println("Pembayaran berhasil.");
        pesanan.cetakStruk();
    }

    static void cetakStrukTerakhir() {
        if (!riwayatPesanan.isEmpty()) {
            riwayatPesanan.get(riwayatPesanan.size() - 1).cetakStruk();
            return;
        }
        // program baru dibuka lagi: ambil struk terakhir dari file txt
        String baris = null, terakhir = null;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PESANAN))) {
            while ((baris = br.readLine()) != null) {
                if (!baris.trim().isEmpty()) terakhir = baris;
            }
        } catch (IOException e) {
            // file belum ada
        }
        if (terakhir == null) {
            System.out.println("Belum ada pesanan.");
            return;
        }
        String noStruk = terakhir.split(";")[0];
        try (BufferedReader br = new BufferedReader(new FileReader("struk_" + noStruk + ".txt"))) {
            System.out.println();
            while ((baris = br.readLine()) != null) System.out.println(baris);
        } catch (IOException e) {
            System.out.println("File struk " + noStruk + " tidak ditemukan.");
        }
    }

    // ===== file txt =====

    // format tiap baris data_menu.txt -> Kategori;Nama;Harga;Jenis/Suhu
    static void bacaMenu() {
        File f = new File(FILE_MENU);
        if (!f.exists()) {
            isiMenuAwal();   // pertama kali dijalankan: pakai menu dari struk
            simpanMenu();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] d = baris.split(";");
                if (d.length < 4) continue;
                float harga = Float.parseFloat(d[2]);
                if (d[0].equalsIgnoreCase("Makanan")) {
                    daftarMenu.add(new Makanan(d[1], harga, 0, d[3]));
                } else {
                    daftarMenu.add(new Minuman(d[1], harga, 0, d[3]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Gagal membaca " + FILE_MENU + ": " + e.getMessage());
        }
    }

    static void simpanMenu() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_MENU))) {
            for (Menu m : daftarMenu) {
                String detail = (m instanceof Makanan) ? ((Makanan) m).getJenis() : ((Minuman) m).getSuhu();
                pw.println(m.getKategori() + ";" + m.getNama() + ";" + (long) m.getHarga() + ";" + detail);
            }
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu: " + e.getMessage());
        }
    }

    // format tiap baris data_pesanan.txt ->
    // NoStruk;SalesNo;Tanggal;Nama;Meja;Tipe;JumlahItem;Subtotal;Service;PB1;GrandTotal;Metode
    static void simpanPesanan(Pesanan p) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PESANAN, true))) { // true = tambah di akhir file
            pw.println(p.getNoStruk() + ";" + p.getSalesNo() + ";" + p.getTanggal() + ";"
                    + p.getPelanggan().getNama() + ";" + p.getPelanggan().getNoMeja() + ";"
                    + p.getPelanggan().getTipePesanan() + ";" + p.getJumlahItem() + ";"
                    + (long) p.getSubtotal() + ";" + (long) p.getServiceCharge() + ";"
                    + (long) p.getPb1() + ";" + (long) p.getGrandTotal() + ";"
                    + p.getPembayaran().getMetode());
            System.out.println("Data pesanan disimpan ke " + FILE_PESANAN);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan pesanan: " + e.getMessage());
        }
    }

    static int hitungJumlahPesananTersimpan() {
        int n = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PESANAN))) {
            String baris;
            while ((baris = br.readLine()) != null) if (!baris.trim().isEmpty()) n++;
        } catch (IOException e) {
            // file belum ada, berarti belum ada pesanan
        }
        return n;
    }

    static void lihatRiwayat() {
        File f = new File(FILE_PESANAN);
        if (!f.exists()) {
            System.out.println("Belum ada riwayat pesanan.");
            return;
        }
        System.out.println("\n------------------------- RIWAYAT PESANAN -------------------------");
        System.out.printf("%-18s %-17s %-10s %-5s %-6s %12s%n", "No Struk", "Tanggal", "Pelanggan", "Meja", "Item", "Grand Total");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                String[] d = baris.split(";");
                if (d.length < 12) continue;
                System.out.printf("%-18s %-17s %-10s %-5s %-6s %12s%n",
                        d[0], d[2], d[3], d[4], d[6], rp(Float.parseFloat(d[10])));
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca riwayat: " + e.getMessage());
        }
    }

    // ===== helper input =====
    static int inputInt(String label) {
        while (true) {
            System.out.print(label);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    static float inputFloat(String label) {
        while (true) {
            System.out.print(label);
            try {
                return Float.parseFloat(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    static String inputString(String label) {
        System.out.print(label);
        return sc.nextLine().trim();
    }

    static String rp(float x) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        nf.setMaximumFractionDigits(0);
        return nf.format(x);
    }
}
