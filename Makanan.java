public class Makanan extends Menu {
    private String jenis; // contoh: Nasi, Mie

    public Makanan(String n, float h, int j, String je) {
        super(n, h, j);
        this.jenis = je;
    }

    public Makanan() {
        super();
        this.jenis = "";
    }

    public void setJenis(String je) { this.jenis = je; }
    public String getJenis() { return jenis; }

    @Override
    public String getKategori() {
        return "Makanan";
    }
}
