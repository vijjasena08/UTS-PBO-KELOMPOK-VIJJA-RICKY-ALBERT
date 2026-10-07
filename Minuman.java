public class Minuman extends Menu {
    private String suhu; // contoh: Dingin, Panas

    public Minuman(String n, float h, int j, String s) {
        super(n, h, j);
        this.suhu = s;
    }

    public Minuman() {
        super();
        this.suhu = "";
    }

    public void setSuhu(String s) { this.suhu = s; }
    public String getSuhu() { return suhu; }

    @Override
    public String getKategori() {
        return "Minuman";
    }
}
