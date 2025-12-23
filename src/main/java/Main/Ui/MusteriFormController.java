package Main.Ui;

import Main.User.Musteri;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class MusteriFormController {

    @FXML private TextField txtKullaniciId;
    @FXML private TextField txtAd;
    @FXML private TextField txtEposta;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtTc;
    @FXML private ComboBox<String> cmbEmlakTipi;
    @FXML private TextField txtButce;
    @FXML private TextField txtMinM2;
    @FXML private TextField txtMinOda;

    @FXML
    public void initialize() {
        cmbEmlakTipi.getItems().addAll("Daire", "Müstakil Ev", "Arsa");
    }

    @FXML
    private void kaydet() {
        String idStr = txtKullaniciId.getText();
        String adStr = txtAd.getText();
        String telStr = txtTelefon.getText();
        String emailStr = txtEposta.getText();
        String tcStr = txtTc.getText();
        String emlakTipi = cmbEmlakTipi.getValue();
        String butceStr = txtButce.getText();
        String m2Str = txtMinM2.getText();
        String odaStr = txtMinOda.getText();


        if (!alanlariKontrolEt(idStr, adStr, telStr, emailStr, tcStr, butceStr, m2Str, odaStr, emlakTipi)) {
            return;
        }

        try {
            if (MainViewController.musteriController != null) {

                int id = Integer.parseInt(idStr);
                double butce = Double.parseDouble(butceStr);
                double minM2 = Double.parseDouble(m2Str);
                int minOda = Integer.parseInt(odaStr);

                Musteri yeniMusteri = new Musteri(
                        id, adStr, emailStr, telStr, tcStr,
                        emlakTipi, butce, minM2, minOda
                );

                boolean basarili = MainViewController.musteriController.musteriEkle(yeniMusteri);

                if (basarili) {
                    bilgiVer(Alert.AlertType.INFORMATION, "Başarılı", "✅ Müşteri kaydı başarıyla tamamlandı.");
                    formuTemizle();
                } else {
                    // HATA DURUMU ANALİZİ (ID Çakışması vs. I/O Hatası)
                    if (MainViewController.musteriController.musteriBul(id) != null) {
                        // Eğer Controller false döndü AMA ID sistemde bulunuyorsa = ID Çakışmasıydı
                        bilgiVer(Alert.AlertType.ERROR, "Kısıtlama İhlali", "🚨 Kayıt yapılamadı: Girilen ID (" + idStr + ") zaten sistemde mevcut!");
                    } else {
                        // Eğer Controller false döndü VE ID sistemde yoksa = KRİTİK I/O HATASI
                        bilgiVer(Alert.AlertType.ERROR, "Kritik Hata", "💥 Kayıt Başarısız: Veri dosyasına yazılamadı!");
                    }
                }
            }
        } catch (NumberFormatException e) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "🚨 Sayısal Alan Hatası: Verilen değerler dönüştürülemedi.");
        } catch (Exception e) {
            bilgiVer(Alert.AlertType.ERROR, "Genel Hata", "💥 Beklenmedik bir hata oluştu: " + e.getMessage());
        }
    }


    private boolean alanlariKontrolEt(String id, String ad, String tel, String email, String tc, String butce, String m2, String oda, String emlakTipi) {

        // 1. BOŞ ALAN KISITLAMASI
        if (id.trim().isEmpty() || ad.trim().isEmpty() || butce.trim().isEmpty() || m2.trim().isEmpty() || emlakTipi == null) {
            bilgiVer(Alert.AlertType.WARNING, "Uyarı", "Lütfen tüm temel alanları doldurunuz.");
            return false;
        }

        if (!ad.trim().matches("^[a-zA-ZçğıöşüÇĞİÖŞÜ\\s]+$")) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "İsim alanı sadece harflerden oluşmalıdır.");
            return false;
        }

        // Arsa değilse oda sayısının da dolu olması gerekir
        if (!emlakTipi.equals("Arsa") && oda.trim().isEmpty()) {
            bilgiVer(Alert.AlertType.WARNING, "Uyarı", "Seçilen emlak tipi için oda sayısı girmelisiniz.");
            return false;
        }

        // 2. SAYISAL VE ARALIK KISITLAMALARI
        try {
            int idVal = Integer.parseInt(id);
            double butceVal = Double.parseDouble(butce);
            double m2Val = Double.parseDouble(m2);

            // ID Kontrolü
            if (idVal <= 0) {
                bilgiVer(Alert.AlertType.ERROR, "Hata", "Müşteri ID pozitif bir sayı olmalıdır.");
                return false;
            }

            // Bütçe Kontrolü (1.000.000 - 30.000.000)
            if (butceVal < 1000000 || butceVal > 30000000) {
                bilgiVer(Alert.AlertType.ERROR, "Hata", "Bütçe 1.000.000 TL ile 30.000.000 TL arasında olmalıdır.");
                return false;
            }

            // Metrekare Kontrolü (80 - 1000)
            if (m2Val < 80 || m2Val > 1000) {
                bilgiVer(Alert.AlertType.ERROR, "Hata", "Minimum Metrekare 80 ile 1000 arasında olmalıdır.");
                return false;
            }

            // Oda Sayısı Kontrolü (1 - 20) -> Sadece Daire ve Müstakil için
            if (!emlakTipi.equals("Arsa")) {
                int odaVal = Integer.parseInt(oda);
                if (odaVal < 1 || odaVal > 20) {
                    bilgiVer(Alert.AlertType.ERROR, "Hata", "Minimum Oda sayısı 1 ile 20 arasında olmalıdır.");
                    return false;
                }
            }

        } catch (NumberFormatException e) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "ID, Bütçe, M2 ve Oda alanlarına geçerli sayısal değerler giriniz!");
            return false;
        }

        // 3. TELEFON VE TC KISITLAMALARI
        if (!tel.trim().isEmpty() && !tel.matches("^[0-9]{10,11}$")) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "Telefon numarası sadece 10 veya 11 rakamdan oluşmalıdır.");
            return false;
        }

        if (!tc.trim().isEmpty() && !tc.matches("^[0-9]{11}$")) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "T.C. Kimlik Numarası 11 rakamdan oluşmalıdır.");
            return false;
        }

        // 4. E-POSTA KISITLAMASI
        if (!email.trim().isEmpty() && (!email.contains("@") || email.contains(" "))) {
            bilgiVer(Alert.AlertType.ERROR, "Format Hatası", "Geçerli bir E-posta adresi giriniz.");
            return false;
        }

        return true;
    }

    // --- DİĞER YARDIMCI METOTLAR ---
    @FXML private void temizleButonu() { formuTemizle(); }
    private void formuTemizle() { /* ... alan temizleme kodları ... */ }
    private void bilgiVer(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}