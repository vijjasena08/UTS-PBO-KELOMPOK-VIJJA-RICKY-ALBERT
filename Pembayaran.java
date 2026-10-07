public class Pembayaran {
    private String metode;      // contoh: QRIS MANDIRI, TUNAI, DEBIT BCA
    private float jumlahBayar;
    private float kembalian;

    public Pembayaran(String m, float jb) {
        this.metode = m;
        this.jumlahBayar = jb;
        this.kembalian = 0;
    }

    public Pembayaran() {
        this("", 0);
    }

    public void setMetode(String m) { this.metode = m; }
    public void setJumlahBayar(float jb) { this.jumlahBayar = jb; }

    public String getMetode() { return metode; }
    public float getJumlahBayar() { return jumlahBayar; }
    public float getKembalian() { return kembalian; }

    public float hitungKembalian(float total) {
        kembalian = jumlahBayar - total;
        if (kembalian < 0) kembalian = 0;
        return kembalian;
    }
}
