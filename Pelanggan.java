public class Pelanggan {
    private String nama;
    private int noMeja;
    private String tipePesanan; // DINE IN / TAKE AWAY

    public Pelanggan(String n, int nm, String tp) {
        this.nama = n;
        this.noMeja = nm;
        this.tipePesanan = tp;
    }

    public Pelanggan() {
        this("", 0, "DINE IN");
    }

    public void setNama(String n) { this.nama = n; }
    public void setNoMeja(int nm) { this.noMeja = nm; }
    public void setTipePesanan(String tp) { this.tipePesanan = tp; }

    public String getNama() { return nama; }
    public int getNoMeja() { return noMeja; }
    public String getTipePesanan() { return tipePesanan; }
}
