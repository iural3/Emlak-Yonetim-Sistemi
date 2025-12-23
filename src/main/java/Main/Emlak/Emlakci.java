package Main.Emlak;

public class Emlakci {

    private String ad;
    private String telefon;
    private IlanDeposu ilanDeposu;

    public Emlakci(String ad, String telefon, IlanDeposu ilanDeposu) {
        this.ad = ad;
        this.telefon = telefon;
        this.ilanDeposu = ilanDeposu;
    }
    public String getAd(String ad) {
        return ad;}
    public void setAd(String ad) {
        this.ad = ad;}
    public String gettelefon(String telefon) {
        return telefon;
    }
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    // İlan Yayınlama (Emlakçı üzerinden ilan eklenir)
    public void ilanYayinla(Ilan ilan) {
        ilanDeposu.ilanEkle(ilan);
    }

    // İlan Silme (Emlakçı kontrolünde)
    public boolean ilanSil(int ilanNo) {
        return ilanDeposu.ilanSil(ilanNo);
    }

    // İlan Arama (binary search Emlakçı üzerinden çağrılır)
    public Ilan ilanAra(double fiyat) {
        return ilanDeposu.fiyatAraBST(fiyat);
    }
    public void ilanAraVeListele(double fiyat) {
        ilanDeposu.fiyatAraBST(fiyat);
    }




    // Emlakçının tüm ilanlarını listelemek
    public void ilanlariListele() {
        ilanDeposu.listele("fiyat");
    }

    public String getAd() { return ad; }
    public String gettelefon() { return telefon; }

}