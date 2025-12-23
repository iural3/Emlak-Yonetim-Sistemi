package Main.Ui;

import Main.Emlak.Adres;
import Main.Emlak.Arsa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ArsaFormController {

    @FXML private TextField txtIlanNo, txtBaslik, txtFiyat, txtAlan, txtIl, txtIlce, txtSure;
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


            // --- 4. ALAN (ARSA METREKARESİ) KONTROLÜ (GÜNCELLENDİ) ---
            String alanStr = txtAlan.getText().trim();
            if (alanStr.isEmpty())
                throw new Exception("HATA: Arsa alanı boş bırakılamaz!");

            // Önce uzunluk kontrolü (Max 20 karakter)
            if (alanStr.length() > 20)
                throw new Exception("HATA: Arsa alanı için girilen sayı çok uzun! (Maksimum 20 hane)");

            // Regex kontrolü
            if (!alanStr.matches("\\d+"))
                throw new Exception("HATA: Arsa alanına harf, nokta veya virgül giremezsiniz. Sadece tam sayı giriniz.");

            // Double parse (Büyük sayılar için int yerine double kullandık)
            double alan = Double.parseDouble(alanStr);

            // Min kontrolü (150 m2)
            if (alan < 150)
                throw new Exception("HATA: Arsa alanı 150 m²'den küçük olamaz.");


            // --- 5. İL ve İLÇE KONTROLÜ ---
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


            // --- 6. SÜRE KONTROLÜ ---
            String sureStr = txtSure.getText().trim();
            if (sureStr.isEmpty())
                throw new Exception("HATA: İlan süresi boş bırakılamaz.");

            if (!sureStr.matches("\\d+"))
                throw new Exception("HATA: İlan süresine sadece rakam girmelisiniz.");

            int sure = Integer.parseInt(sureStr);
            if (sure < 10 || sure > 45)
                throw new Exception("HATA: İlan süresi en az 10 gün, en fazla 45 gün olabilir.");


            // --- KAYIT İŞLEMİ ---

            int ilanNo = Integer.parseInt(ilanNoStr);
            Adres adres = new Adres(il, ilce, "Merkez");

            Arsa arsa = new Arsa(ilanNo, baslik, (double)fiyatLong, adres, alan);
            arsa.setKalanGun(sure);

            if (MainViewController.ilanController != null) {
                MainViewController.ilanController.ilanEkle(arsa);
                bilgiVer("İşlem Başarılı", "Arsa ilanı sisteme eklendi.\nİlan No: " + ilanNo);
                formuTemizle();
            }

        } catch (NumberFormatException e) {
            hataVer("Biçim Hatası", "Girdiğiniz alanlarda geçersiz karakterler veya çok büyük sayılar var.");
        } catch (Exception e) {
            hataVer("Giriş Hatası", e.getMessage());
        }
    }

    private void formuTemizle() {
        txtIlanNo.clear(); txtBaslik.clear(); txtFiyat.clear(); txtAlan.clear();
        txtIl.clear(); txtIlce.clear(); txtSure.setText("30");
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