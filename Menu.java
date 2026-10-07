public abstract class Menu {
    private String nama;
    private float harga;
    private int jumlah;

    public Menu(String n, float h, int j) {
        this.nama = n;
        this.harga = h;
        this.jumlah = j;
    }

    public Menu() {
        this("", 0, 0);
    }

    public void setNama(String n) { this.nama = n; }
    public void setHarga(float h) { this.harga = h; }
    public void setJumlah(int j) { this.jumlah = j; }

    public String getNama() { return nama; }
    public float getHarga() { return harga; }
    public int getJumlah() { return jumlah; }

    // total harga satu baris item = harga x jumlah
    public float hitungTotal() {
        return harga * jumlah;
    }

    // method abstract, di-override oleh Makanan dan Minuman
    public abstract String getKategori();
}
