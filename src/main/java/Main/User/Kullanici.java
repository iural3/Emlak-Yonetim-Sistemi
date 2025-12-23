package Main.User;

public class Kullanici {
    private int kullaniciId;
    private String ad;
    private String eposta;
    private String telefon;

    public Kullanici(int kullaniciId, String ad, String eposta, String telefon) {
        this.kullaniciId = kullaniciId;
        this.ad = ad;
        this.eposta = eposta;
        this.telefon = telefon;
    }

    // --- BU METOTLAR ŞART ---
    public int getkullaniciId() { return kullaniciId; }
    public String getAd() { return ad; }
    public String geteposta() { return eposta; }
    public String gettelefon() { return telefon; }

    public void setkullaniciId(int kullaniciId) {
        this.kullaniciId = kullaniciId;
    }
    public void setAd(String ad) { this.ad = ad; }
    public void seteposta(String eposta) { this.eposta = eposta; }
    public void settelefon(String telefon) { this.telefon = telefon; }

}
