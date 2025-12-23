package Main.Emlak;

public class MustakilEv extends Ilan {
    private int oda;
    private int kackat; // Toplam kat sayısı
    private double metrekare;

    public MustakilEv(int ilanNo, String ilanBaslik, double fiyat, Adres adres, int oda, int kackat, double metrekare) {
        super(ilanNo, ilanBaslik, fiyat, adres);
        this.oda = oda;
        this.kackat = kackat;
        this.metrekare = metrekare;
    }

    public int getOda() { return oda; }
    public int getKackat() { return kackat; }

    @Override
    public double getMetrekare() { return metrekare; }

    @Override
    public boolean metrekareVarMi() { return true; }

    public void setOda(int oda) {
        this.oda = oda;
    }
    public void setKackat(int kackat) {
        this.kackat = kackat;
    }
    public  void setMetrekare(double metrekare) {
        this.metrekare = metrekare;
    }

    @Override
    public String getDetayBilgisi() {
        return metrekare + " m² | " + oda + " Oda | " + kackat + " Katlı Bina";
    }
}