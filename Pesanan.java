import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Pesanan implements CetakStruk {
    private static final float TARIF_SERVICE = 0.05f; // service charge 5%
    private static final float TARIF_PB1 = 0.10f;     // PB1 10% dari subtotal

    private String noStruk;
    private String salesNo;
    private String namaToko;
    private String tanggal;
    private String kasir;
    private float subtotal;
    private float serviceCharge;
    private float pb1;
    private float grandTotal;
    private ArrayList<Menu> listMenu;
    private Pelanggan pelanggan;
    private Pembayaran pembayaran;

    public Pesanan(String ns, String sn, String nt, String t, String k, Pelanggan pl, Pembayaran pb) {
        this.noStruk = ns;
        this.salesNo = sn;
        this.namaToko = nt;
        this.tanggal = t;
        this.kasir = k;
        this.pelanggan = pl;
        this.pembayaran = pb;
        this.listMenu = new ArrayList<>();
    }

    public Pesanan() {
        this("", "", "", "", "", new Pelanggan(), new Pembayaran());
    }

    public void setNoStruk(String ns) { this.noStruk = ns; }
    public void setSalesNo(String sn) { this.salesNo = sn; }
    public void setNamaToko(String nt) { this.namaToko = nt; }
    public void setTanggal(String t) { this.tanggal = t; }
    public void setKasir(String k) { this.kasir = k; }
    public void setPelanggan(Pelanggan pl) { this.pelanggan = pl; }
    public void setPembayaran(Pembayaran pb) { this.pembayaran = pb; }

    public void addMenu(Menu m) {
        listMenu.add(m);
    }

    public String getNoStruk() { return noStruk; }
    public String getSalesNo() { return salesNo; }
    public String getNamaToko() { return namaToko; }
    public String getTanggal() { return tanggal; }
    public String getKasir() { return kasir; }
    public float getSubtotal() { return subtotal; }
    public float getServiceCharge() { return serviceCharge; }
    public float getPb1() { return pb1; }
    public float getGrandTotal() { return grandTotal; }
    public ArrayList<Menu> getListMenu() { return listMenu; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public Pembayaran getPembayaran() { return pembayaran; }

    // total qty semua item (di struk: "13 items")
    public int getJumlahItem() {
        int total = 0;
        for (Menu m : listMenu) total += m.getJumlah();
        return total;
    }

    public float hitungSubtotal() {
        subtotal = 0;
        for (Menu m : listMenu) subtotal += m.hitungTotal();
        return subtotal;
    }

    public float hitungServiceCharge() {
        serviceCharge = Math.round(hitungSubtotal() * TARIF_SERVICE);
        return serviceCharge;
    }

    public float hitungPb1() {
        pb1 = Math.round(hitungSubtotal() * TARIF_PB1);
        return pb1;
    }

    public float hitungGrandTotal() {
        grandTotal = hitungSubtotal() + hitungServiceCharge() + hitungPb1();
        return grandTotal;
    }

    // format angka gaya Indonesia: 430100 -> 430.100
    private String rp(float x) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"));
        nf.setMaximumFractionDigits(0);
        return nf.format(x);
    }

    private String garis() {
        return "-".repeat(44);
    }

    // menyusun isi struk dalam bentuk teks
    private String buatTeksStruk() {
        hitungGrandTotal();
        StringBuilder sb = new StringBuilder();
        String n = System.lineSeparator();
        sb.append(garis()).append(n);
        sb.append(String.format("%28s", "RUMAH SEJOLI")).append(n);
        sb.append(String.format("%28s", namaToko)).append(n);
        sb.append(garis()).append(n);
        sb.append(String.format("%-10s: %s", "No", noStruk)).append(n);
        sb.append(String.format("%-10s: %s", "Sales No", salesNo)).append(n);
        sb.append(String.format("%-10s: %s", "Date", tanggal)).append(n);
        sb.append(String.format("%-10s: %s", "Info", pelanggan.getNama())).append(n);
        sb.append(String.format("%-10s: %d", "Table", pelanggan.getNoMeja())).append(n);
        sb.append(String.format("%-10s: %s", "Purpose", pelanggan.getTipePesanan())).append(n);
        sb.append(String.format("%-10s: %s", "Cashier", kasir)).append(n);
        sb.append(garis()).append(n);
        for (Menu m : listMenu) {
            sb.append(m.getNama()).append(n);
            String qty = m.getJumlah() + "x   @" + rp(m.getHarga());
            sb.append(String.format("%-30s%14s", qty, rp(m.hitungTotal()))).append(n);
        }
        sb.append(garis()).append(n);
        sb.append(getJumlahItem()).append(" items").append(n);
        sb.append(String.format("%28s : %13s", "Subtotal", rp(subtotal))).append(n);
        sb.append(String.format("%28s : %13s", "Service Charge", rp(serviceCharge))).append(n);
        sb.append(String.format("%28s : %13s", "PB1", rp(pb1))).append(n);
        sb.append(garis()).append(n);
        sb.append(String.format("%28s : %13s", "Grand Total", rp(grandTotal))).append(n);
        sb.append(String.format("%28s : %13s", pembayaran.getMetode(), rp(pembayaran.getJumlahBayar()))).append(n);
        if (pembayaran.getKembalian() > 0) {
            sb.append(String.format("%28s : %13s", "Kembalian", rp(pembayaran.getKembalian()))).append(n);
        }
        sb.append(garis()).append(n);
        sb.append(String.format("%28s", "#dirumahsejoli")).append(n);
        return sb.toString();
    }

    @Override
    public void cetakStruk() {
        String teks = buatTeksStruk();
        System.out.println();
        System.out.println(teks);

        // simpan struk ke file txt, contoh: struk_RMSJ202608140119.txt
        String namaFile = "struk_" + noStruk + ".txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(namaFile))) {
            pw.print(teks);
            System.out.println("Struk disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk: " + e.getMessage());
        }
    }
}
