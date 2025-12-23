package Main.Emlak;

public abstract class Ilan {

    // Değişkenler
    private int ilanNo;
    private String ilanBaslik;
    private double fiyat;
    private Adres adres;
    private int kalanGun;

  ;

    // Constructor
    public Ilan(int ilanNo, String ilanBaslik, double fiyat, Adres adres) {
        this.ilanNo = ilanNo;
        this.ilanBaslik = ilanBaslik;
        this.fiyat = fiyat;
        this.adres = adres;
        this.kalanGun = 30; // Varsayılan süre

    }

    // GETTER METOTLARI
    public int getIlanNo() { return ilanNo; }
    public String getIlanBaslik() { return ilanBaslik; }
    public double getFiyat() { return fiyat; }
    public Adres getAdres() { return adres; }
    public int getKalanGun() { return kalanGun; }



    // SETTER METOTLARI
    public void setIlanBaslik(String baslik) { this.ilanBaslik = baslik; }
    public void setFiyat(double fiyat) { this.fiyat = fiyat; }
    public void setAdres(Adres adres) { this.adres = adres; }
    public void setKalanGun(int gun) { this.kalanGun = gun; }



    public void gunAzalt() {
        if (kalanGun > 0) kalanGun--;
    }
    public boolean sureBittiMi() {
        return kalanGun <= 0;
    }


    public abstract double getMetrekare();
    public abstract boolean metrekareVarMi();

    public abstract String getDetayBilgisi();

    public String toFileStringTemel() {

        return getIlanNo() + "|" + getIlanBaslik() + "|" + getFiyat() + "|" + getAdres().toString();
    }

    public static Ilan fromFileString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 5) return null; // En az temel 5 alan varsayımı


        String ilanTipi = parts[0].trim();
        try {
            if (ilanTipi.equals("DAIRE")) {
            } else if (ilanTipi.equals("ARSA")) {
            }
        } catch (Exception e) {
            System.err.println("Dosyadan ilan oluşturma hatası: " + line);
        }

        return null;
    }
    @Override
    public String toString() {
        return "No: " + ilanNo + " | " + ilanBaslik + " | " + fiyat + " TL";
    }
}