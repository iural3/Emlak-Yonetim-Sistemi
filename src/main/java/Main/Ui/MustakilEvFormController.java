package Main.Ui;

import Main.Emlak.Adres;
import Main.Emlak.MustakilEv;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MustakilEvFormController {

    @FXML private TextField txtIlanNo, txtBaslik, txtFiyat, txtMetrekare, txtOda, txtKat, txtIl, txtIlce, txtMahalle, txtSure;
    @FXML private Button btnKaydet;

    @FXML
    public void initialize() {
        btnKaydet.setOnAction(e -> kaydet());
    }

    private void kaydet() {
        try {
            // --- 1. İLAN NO KONTROLÜ ---
            String ilanNoStr = txtIlanNo.getText().trim();
            if (ilanNoStr.isEmpty()) throw new Exception("HATA: İlan No boş bırakılamaz.");
            if (ilanNoStr.length() > 10) throw new Exception("HATA: İlan No çok uzun (Max 10 karakter).");
            if (!ilanNoStr.matches("\\d+")) throw new Exception("HATA: İlan No sadece rakam içermelidir.");


            // --- 2. BAŞLIK KONTROLÜ ---
            String baslik = txtBaslik.getText().trim();
            if (baslik.isEmpty()) throw new Exception("HATA: Başlık girmelisiniz.");

            // İSTEK: Max 30 karakter
            if (baslik.length() > 30) throw new Exception("HATA: Başlık çok uzun (Max 30 karakter).");

            // İSTEK: Max 4 boşluk
            int boslukSayisi = baslik.length() - baslik.replace(" ", "").length();
            if (boslukSayisi > 4) throw new Exception("HATA: Başlıkta en fazla 4 adet boşluk kullanabilirsiniz.");

            // İSTEK: "sadece sembol ve rakam olamaz" yazısı
            if (!baslik.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: Başlıkta sembol ve rakam olamaz. Sadece harf giriniz.");


            // --- 3. FİYAT KONTROLÜ ---
            String fiyatStr = txtFiyat.getText().trim();
            if (fiyatStr.isEmpty()) throw new Exception("HATA: Fiyat boş olamaz.");
            // Önce harf kontrolü yapıyoruz ki "NumberFormat" hatasına düşmesin
            if (!fiyatStr.matches("\\d+")) throw new Exception("HATA: Fiyat alanına harf veya sembol giremezsiniz. Sadece rakam olmalı.");

            long fiyat;
            try {
                fiyat = Long.parseLong(fiyatStr);
            } catch (NumberFormatException e) {
                throw new Exception("HATA: Girilen fiyat çok büyük, lütfen geçerli bir değer girin.");
            }

            // İSTEK: Min 1M, Max 30M
            if (fiyat < 1000000) throw new Exception("HATA: Fiyat çok düşük! (Min: 1.000.000 TL)");
            if (fiyat > 30000000) throw new Exception("HATA: Fiyat çok yüksek! (Max: 30.000.000 TL)");


            // --- 4. METREKARE KONTROLÜ ---
            String m2Str = txtMetrekare.getText().trim();
            if (m2Str.isEmpty()) throw new Exception("HATA: Metrekare boş bırakılamaz.");
            if (!m2Str.matches("\\d+(\\.\\d+)?")) throw new Exception("HATA: Metrekare alanına harf veya sembol girmeyiniz.");

            // Uzunluk kontrolü (parse etmeden önce)
            if (m2Str.length() > 6) throw new Exception("HATA: Metrekare değeri çok uzun.");

            double m2 = Double.parseDouble(m2Str);
            if (m2 < 70) throw new Exception("HATA: Ev çok küçük! (Min: 70 m²)");
            if (m2 > 500) throw new Exception("HATA: Ev çok büyük! (Max: 500 m²)");


            // --- 5. ODA SAYISI KONTROLÜ ---
            String odaStr = txtOda.getText().trim();
            if (odaStr.isEmpty()) throw new Exception("HATA: Oda sayısı boş bırakılamaz.");
            if (!odaStr.matches("\\d+")) throw new Exception("HATA: Oda sayısı sadece rakam olmalı.");
            if (odaStr.length() > 2) throw new Exception("HATA: Oda sayısı çok uzun.");

            int oda = Integer.parseInt(odaStr);
            if (oda < 1) throw new Exception("HATA: En az 1 oda olmalı.");
            if (oda > 8) throw new Exception("HATA: En fazla 8 oda olabilir.");


            // --- 6. KAT SAYISI KONTROLÜ (DÜZELTİLDİ) ---
            String katStr = txtKat.getText().trim();

            // İSTEK: Boş girince "Boş olamaz" desin (Genel hataya düşmesin)
            if (katStr.isEmpty()) throw new Exception("HATA: Kat sayısı boş bırakılamaz.");

            // İSTEK: Çok uzun girince "Max 4 olabilir" desin
            if (katStr.length() > 4) throw new Exception("HATA: Kat sayısı çok uzun (Max 4 hane).");

            // Harf kontrolü
            if (!katStr.matches("\\d+")) throw new Exception("HATA: Kat sayısına harf veya sembol girmeyiniz.");

            int kat = Integer.parseInt(katStr);
            if (kat < 1) throw new Exception("HATA: Kat sayısı en az 1 olabilir.");
            if (kat > 4) throw new Exception("HATA: Müstakil ev en fazla 4 katlı olabilir.");


            // --- 7. ADRES (İL, İLÇE, MAHALLE) KONTROLÜ ---
            String il = txtIl.getText().trim();
            String ilce = txtIlce.getText().trim();
            String mahalle = txtMahalle.getText().trim();

            if (il.isEmpty()) throw new Exception("HATA: İl ismi boş bırakılamaz.");
            if (ilce.isEmpty()) throw new Exception("HATA: İlçe ismi boş bırakılamaz.");
            if (mahalle.isEmpty()) throw new Exception("HATA: Mahalle ismi boş bırakılamaz.");

            // İL KONTROLÜ (DÜZELTİLDİ)
            if (il.length() > 15) throw new Exception("HATA: İl ismi en fazla 15 karakter olabilir.");
            if (!il.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: İl isminde rakam, noktalama işareti veya sembol kullanılamaz.");

            // İLÇE KONTROLÜ
            if (ilce.length() > 15) throw new Exception("HATA: İlçe ismi en fazla 15 karakter olabilir.");
            if (!ilce.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: İlçe isminde rakam, noktalama işareti veya sembol kullanılamaz.");

            // MAHALLE KONTROLÜ
            if (mahalle.length() > 20) throw new Exception("HATA: Mahalle ismi en fazla 20 karakter olabilir.");
            if (!mahalle.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: Mahalle isminde rakam, noktalama işareti veya sembol kullanılamaz.");


            // --- 8. SÜRE KONTROLÜ (DÜZELTİLDİ) ---
            String sureStr = txtSure.getText().trim();

            // Boş kontrolü
            if (sureStr.isEmpty()) throw new Exception("HATA: Süre alanı boş bırakılamaz.");

            // Uzunluk kontrolü (Örn: 999999 girerse NumberFormat hatası vermesin diye)
            if (sureStr.length() > 3) throw new Exception("HATA: Süre alanı çok uzun.");

            // Rakam kontrolü
            if (!sureStr.matches("\\d+")) throw new Exception("HATA: Süre alanına harf veya sembol girmeyiniz.");

            int sure = Integer.parseInt(sureStr);
            // İSTEK: Min 12, Max 45
            if (sure < 12 || sure > 45)
                throw new Exception("HATA: İlan süresi en az 12, en fazla 45 gün olabilir.");


            // --- KAYIT İŞLEMİ ---
            int no = Integer.parseInt(ilanNoStr);
            Adres adres = new Adres(il, ilce, mahalle);

            MustakilEv ev = new MustakilEv(no, baslik, (double)fiyat, adres, oda, kat, m2);
            ev.setKalanGun(sure);

            if (MainViewController.ilanController != null) {
                MainViewController.ilanController.ilanEkle(ev);
                bilgiVer("Başarılı", "Müstakil ev ilanı sisteme eklendi.\nSüre: " + sure + " Gün");
                formuTemizle();
            }

        } catch (NumberFormatException e) {
            // Bu kısım artık neredeyse hiç çalışmayacak çünkü yukarıda her şeyi manuel kontrol ettik.
            // Yine de ekstra bir güvenlik olarak dursun.
            hataVer("Biçim Hatası", "Girdiğiniz değerlerden biri sayı formatına uymuyor.");
        } catch (Exception e) {
            hataVer("Giriş Hatası", e.getMessage());
        }
    }

    private void formuTemizle() {
        txtIlanNo.clear(); txtBaslik.clear(); txtFiyat.clear(); txtMetrekare.clear();
        txtOda.clear(); txtKat.clear(); txtIl.clear(); txtIlce.clear(); txtMahalle.clear();
        txtSure.setText("30");
    }

    private void bilgiVer(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.show();
    }

    private void hataVer(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t);
        a.setHeaderText("Lütfen Düzeltin");
        a.setContentText(m);
        a.show();
    }
}