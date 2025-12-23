package Main.Emlak;

public class Daire extends Ilan {
    private double metrekare;
    private int oda;
    private int kacincikat;


    public Daire(int ilanNo, String ilanBaslik, double fiyat, Adres adres, double metrekare, int oda, int kacincikat) {
        super(ilanNo, ilanBaslik, fiyat, adres);
        this.metrekare = metrekare;
        this.oda = oda;
        this.kacincikat = kacincikat;
    }

    public int getoda() {
        return oda;
    }

    public void setoda(int oda) {
        this.oda = oda;
    }

    public int getkacincikat() {
        return kacincikat;
    }

    public void setkacincikat(int kacincikat) {
        this.kacincikat = kacincikat;
    }

    @Override
    public double getMetrekare() {
        return metrekare;
    }

    public void setMetrekare(double metrekare) {
        this.metrekare = metrekare;
    }
    @Override
    public boolean metrekareVarMi() {
        return true;
    }

    @Override
    public String getDetayBilgisi() {
        return metrekare + " m² , " + oda + " Oda , " + kacincikat + ". Kat";
    }




}