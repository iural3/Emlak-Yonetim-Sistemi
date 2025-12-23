package Main.User;

public class Yonetici extends Kullanici {
    private String parola;
    public Yonetici(int kullaniciId, String ad, String eposta, String telefon, String parola) {
        super(kullaniciId, ad, eposta, telefon);
        this.parola=parola;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

}
