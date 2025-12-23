package Main.Emlak;

import java.io.*;

public class IlanController {

    private IlanDeposu depo;
    private final String DOSYA_YOLU = "ilanlar.txt";

    public IlanController(IlanDeposu depo) {
        this.depo = depo;
        txtYukle(); // Uygulama başlatıldığında verileri yükle
    }

    //EKLEME METODU
    public void ilanEkle(Ilan ilan) throws Exception {
        if(depo.ilanVarMi(ilan.getIlanNo())) {
            throw new Exception("Bu İlan No zaten kullanılıyor!");
        }
        depo.ilanEkle(ilan);
        txtKaydet();
    }

    // DAİRE OLUŞTURMA
    public void createDaire(String ilanNo, String baslik, String fiyat, String il, String ilce, String mah, String m2, String oda, String kat) throws Exception {
        int id = safeInt(ilanNo);
        if (id == 0) throw new Exception("İlan No geçersiz.");


        Daire d = new Daire(id, temizle(baslik), safeDouble(fiyat), new Adres(il, ilce, mah), safeDouble(m2), safeInt(oda), safeInt(kat));
        ilanEkle(d);
    }

    // MÜSTAKİL EV OLUŞTURMA
    public void createMustakilEv(String ilanNo, String baslik, String fiyat, String il, String ilce, String mah, String m2, String oda, String kat) throws Exception {
        int id = safeInt(ilanNo);
        MustakilEv ev = new MustakilEv(id, temizle(baslik), safeDouble(fiyat), new Adres(il, ilce, mah), safeInt(oda), safeInt(kat), safeDouble(m2));
        ilanEkle(ev);
    }

    // ARSA OLUŞTURMA
    public void createArsa(String ilanNo, String baslik, String fiyat, String il, String ilce, String mah, String alan) throws Exception {
        int id = safeInt(ilanNo);
        Arsa arsa = new Arsa(id, temizle(baslik), safeDouble(fiyat), new Adres(il, ilce, mah), safeDouble(alan));
        ilanEkle(arsa);
    }

    // ARAMA METODU
    public Ilan[] ilanAra(Integer min, Integer max, String il, String ilce, String oda, String tip) {
        return depo.ilanSorgula(min, max, il, ilce, oda, tip);
    }

    // GÜNCELLEME METODU
    public boolean ilanGuncelle(int id, String baslik, String fiyat, String adres) {
        boolean basarili = depo.ilanGuncelle(id, baslik, fiyat, adres);
        if (basarili) { txtKaydet(); }
        return basarili;
    }

    // SİLME METODU
    public boolean ilanSil(int no) {
        boolean b = depo.ilanSil(no);
        if(b) txtKaydet();
        return b;
    }

    public Ilan[] getIlanlar() { return depo.getIlanlarArray(); }
    public void gunuIlerlet() { depo.gunIslet(); txtKaydet(); }


    // DOSYAYA KAYDETME (TXT KAYDET)
    public void txtKaydet() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_YOLU))) {
            for (Ilan i : depo.getIlanlarArray()) {
                StringBuilder sb = new StringBuilder();
                String tip = "";
                if (i instanceof Daire) tip = "DAIRE";
                else if (i instanceof MustakilEv) tip = "MUSTAKILEV";
                else if (i instanceof Arsa) tip = "ARSA";

                // Ortak Alanlar
                sb.append(tip).append(",")
                        .append(i.getIlanNo()).append(",")
                        .append(i.getIlanBaslik()).append(",")
                        .append(i.getFiyat()).append(",")
                        .append(i.getAdres().getIl()).append(",")
                        .append(i.getAdres().getIlce()).append(",")
                        .append(i.getAdres().getMahalle()).append(",")
                        .append(i.getMetrekare());

                // Tipe Özgü Alanlar
                if (i instanceof Daire) {
                    Daire d = (Daire) i;
                    sb.append(",").append(d.getoda()).append(",").append(d.getkacincikat());
                } else if (i instanceof MustakilEv) {
                    MustakilEv m = (MustakilEv) i;
                    sb.append(",").append(m.getOda()).append(",").append(m.getKackat());
                } else if (i instanceof Arsa) {
                    sb.append(",0,0"); // Arsa için oda/kat yerine 0 koyuyoruz
                }

                sb.append(",").append(i.getKalanGun());

                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (Exception e) {
            System.err.println("Kaydetme hatası: " + e.getMessage());
        }
    }

    // DOSYADAN YÜKLEME (TXT YUKLE)
    public void txtYukle() {
        File file = new File(DOSYA_YOLU);
        if (!file.exists()) return;
        depo.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                if (satir.trim().isEmpty()) continue;
                try {
                    String[] p = satir.split(",");
                    if (p.length < 9) continue;

                    String tip = p[0].trim();
                    int id = safeInt(p[1]);
                    String baslik = p[2];
                    double fiyat = safeDouble(p[3]);
                    Adres adres = new Adres(p[4], p[5], p[6]);
                    double m2 = safeDouble(p[7]);

                    Ilan yeni = null;

                    if (tip.equals("DAIRE")) {
                        int oda = safeInt(p[8]);
                        int kat = safeInt(p[9]);
                        yeni = new Daire(id, baslik, fiyat, adres, m2, oda, kat);
                    } else if (tip.equals("MUSTAKILEV")) {
                        int oda = safeInt(p[8]);
                        int kat = safeInt(p[9]);
                        yeni = new MustakilEv(id, baslik, fiyat, adres, oda, kat, m2);
                    } else if (tip.equals("ARSA")) {
                        yeni = new Arsa(id, baslik, fiyat, adres, m2);
                    }

                    if (yeni != null) {
                        int gun = safeInt(p[p.length - 1]);
                        yeni.setKalanGun(gun);
                        depo.ilanEkle(yeni);
                    }
                } catch (Exception e) {
                    System.out.println("Satır atlandı: " + satir);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private int safeInt(String val) { try { return Integer.parseInt(val.replaceAll("[^0-9]", "")); } catch (Exception e) { return 0; } }
    private double safeDouble(String val) { try { return Double.parseDouble(val); } catch (Exception e) { return 0.0; } }
    private String temizle(String val) { return val.replace(",", " "); }

    public int getIlanSayisi() {
        return 0; // Geçici olarak sabit bir değer atandı
    }
}