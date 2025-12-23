package Main.Ui;

import Main.Emlak.Adres;
import Main.Emlak.Daire;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DaireFormController {

    @FXML private TextField txtIlanNo, txtBaslik, txtFiyat, txtMetrekare, txtOda, txtKat, txtIl, txtIlce;
    @FXML private Button btnKaydet;

    @FXML
    public void initialize() {
        btnKaydet.setOnAction(e -> kaydet());
    }

    private void kaydet() {
        try {
            // --- 1. İLAN NO KONTROLÜ ---
            String ilanNoStr = txtIlanNo.getText().trim();
            if (ilanNoStr.isEmpty())
                throw new Exception("HATA: İlan No alanı boş bırakılamaz!");

            if (!ilanNoStr.matches("\\d+"))
                throw new Exception("HATA: İlan No alanına harf, nokta veya virgül giremezsiniz. Sadece rakam olmalı.");

            if (ilanNoStr.length() > 12)
                throw new Exception("HATA: İlan No çok uzun! En fazla 12 karakter olabilir.");


            // --- 2. BAŞLIK KONTROLÜ ---
            String baslik = txtBaslik.getText().trim();
            if (baslik.isEmpty())
                throw new Exception("HATA: İlan Başlığı girmelisiniz!");

            if (!baslik.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: İlan başlığında rakam veya sembol kullanılamaz. Sadece harf giriniz.");

            if (baslik.length() > 70)
                throw new Exception("HATA: İlan başlığı çok uzun (Max 70 karakter).");

            int boslukSayisi = baslik.length() - baslik.replace(" ", "").length();
            if (boslukSayisi > 4)
                throw new Exception("HATA: İlan başlığında çok fazla boşluk bıraktınız (Max 4 boşluk).");


            // --- 3. FİYAT KONTROLÜ ---
            String fiyatStr = txtFiyat.getText().trim();
            if (fiyatStr.isEmpty())
                throw new Exception("HATA: Fiyat alanı boş bırakılamaz!");

            if (!fiyatStr.matches("\\d+"))
                throw new Exception("HATA: Fiyat kısmına sadece düz sayı giriniz (Nokta, virgül veya harf kullanmayın).");

            long fiyatLong = Long.parseLong(fiyatStr);
            if (fiyatLong < 1500000)
                throw new Exception("HATA: Girilen fiyat çok düşük! Minimum 1.500.000 TL olmalıdır.");

            if (fiyatLong > 500000000)
                throw new Exception("HATA: Girilen fiyat çok yüksek! Maksimum 500.000.000 TL olabilir.");


            // --- 4. METREKARE KONTROLÜ (GÜNCELLENDİ) ---
            String m2Str = txtMetrekare.getText().trim();
            if (m2Str.isEmpty())
                throw new Exception("HATA: Metrekare alanı boş bırakılamaz!");

            // Burası hem harfi, hem noktayı, hem virgülü engeller.
            if (!m2Str.matches("\\d+"))
                throw new Exception("HATA: Metrekare alanına harf, nokta veya virgül giremezsiniz. Sadece tam sayı giriniz.");

            int m2 = Integer.parseInt(m2Str);
            if (m2 < 80)
                throw new Exception("HATA: Daire 80 m²'den küçük olamaz.");

            if (m2 > 1000)
                throw new Exception("HATA: Daire 1000 m²'den büyük olamaz.");


            // --- 5. ODA SAYISI KONTROLÜ ---
            String odaStr = txtOda.getText().trim();
            if (odaStr.isEmpty())
                throw new Exception("HATA: Oda sayısı girmelisiniz.");

            if (!odaStr.matches("\\d+"))
                throw new Exception("HATA: Oda sayısına harf veya sembol giremezsiniz. Sadece rakam giriniz.");

            int oda = Integer.parseInt(odaStr);
            if (oda < 1 || oda > 8)
                throw new Exception("HATA: Oda sayısı 1 ile 8 arasında olmalıdır.");


            // --- 6. KAT SAYISI KONTROLÜ ---
            String katStr = txtKat.getText().trim();
            if (katStr.isEmpty())
                throw new Exception("HATA: Kat bilgisi girmelisiniz.");

            if (!katStr.matches("-?\\d+"))
                throw new Exception("HATA: Kat sayısı sadece tam sayı olmalıdır (Harf giremezsiniz).");

            int kat = Integer.parseInt(katStr);
            if (kat < -1 || kat > 30)
                throw new Exception("HATA: Kat sayısı -1 ile 30 arasında olmalıdır.");


            // --- 7. İL ve İLÇE KONTROLÜ ---
            String il = txtIl.getText().trim();
            String ilce = txtIlce.getText().trim();

            if (il.isEmpty()) throw new Exception("HATA: İl kısmını boş bırakamazsınız.");
            if (ilce.isEmpty()) throw new Exception("HATA: İlçe kısmını boş bırakamazsınız.");

            if (!il.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: İl ismi sadece harflerden oluşmalıdır (Noktalama işareti veya rakam giremezsiniz).");

            if (il.length() > 15)
                throw new Exception("HATA: İl ismi en fazla 15 karakter olabilir.");

            if (!ilce.matches("[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+"))
                throw new Exception("HATA: İlçe ismi sadece harflerden oluşmalıdır (Noktalama işareti veya rakam giremezsiniz).");

            if (ilce.length() > 15)
                throw new Exception("HATA: İlçe ismi en fazla 15 karakter olabilir.");


            // --- KAYIT İŞLEMİ ---

            int ilanNo = Integer.parseInt(ilanNoStr);
            Adres adres = new Adres(il, ilce, "Merkez");

            Daire d = new Daire(ilanNo, baslik, (double)fiyatLong, adres, (double)m2, oda, kat);

            if (MainViewController.ilanController != null) {
                MainViewController.ilanController.ilanEkle(d);
                bilgiVer("İşlem Başarılı", "Daire ilanı sisteme kaydedildi.\nİlan No: " + ilanNo);
                temizle();
            }

        } catch (NumberFormatException e) {
            hataVer("Biçim Hatası", "Girdiğiniz alanlarda geçersiz karakterler var.");
        } catch (Exception e) {
            hataVer("Giriş Hatası", e.getMessage());
        }
    }

    private void temizle() {
        txtIlanNo.clear(); txtBaslik.clear(); txtFiyat.clear();
        txtMetrekare.clear(); txtOda.clear(); txtKat.clear();
        txtIl.clear(); txtIlce.clear();
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