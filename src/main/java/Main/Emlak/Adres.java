package Main.Emlak;

public class Adres {
    private String il;
    private String ilce;
    private String mahalle;

    public Adres(String il, String ilce, String mahalle) {
        this.il = il;
        this.ilce = ilce;
        this.mahalle = mahalle;
    }

    public String getIl() { return il; }

    public void setIl(String il) {
        this.il = il;
    }
    public String getIlce() { return ilce; }
    public void setIlce(String ilce) {
        this.ilce = ilce;
    }
    public String getMahalle() { return mahalle; }
    public void setMahalle(String mahalle) {
        this.mahalle = mahalle;
    }

    public String toString() {
        return il + "," + ilce + "," + mahalle;
    }
}