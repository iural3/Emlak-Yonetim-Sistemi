package Main.Emlak;

public class Arsa extends Ilan {
    private double alan;

    public Arsa(int ilanNo, String ilanBaslik, double fiyat, Adres adres, double alan) {
        super(ilanNo, ilanBaslik, fiyat, adres);
        this.alan = alan;
    }

    public double getAlan() { return alan; }
    public void setAlan(double alan) { this.alan = alan; }

    @Override
    public double getMetrekare() { return alan; }

    public void setMetrekare(double metrekare) {
        this.alan = metrekare;
    }

    @Override
    public boolean metrekareVarMi() { return true; }


    @Override
    public String getDetayBilgisi() {
        return "Arazi Alanı: " + alan + " m²";
    }
}