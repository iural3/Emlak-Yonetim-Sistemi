package Main.Ui;

import Main.Emlak.IlanController;
import Main.Emlak.IlanDeposu;
import Main.Emlak.Ilan;
import Main.User.Musteri;
import Main.User.MusteriController;
import Main.User.MusteriKuyrugu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur; // Blur efekti için
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region; // Arka plan Region'ı için
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;

public class MainViewController {

    public static IlanController ilanController;
    public static MusteriController musteriController;
    public static MainViewController instance;

    // FXML Bileşenleri
    @FXML private StackPane mainContent;
    @FXML private Button btnIlanEkle, btnIlanListe, btnIlanSil, btnIlanSorgu, btnIlanGuncelle;
    @FXML private Button btnMusteriEkle, btnMusteriListe, btnMusteriSil;

    // İstatistik Alanları (HBox ve Label'lar)
    @FXML private Label musteriSayisiLabel;
    @FXML private Label ilanSayisiLabel;
    @FXML private HBox istatistikKutulari;

    // 🔥 Arka plan ve Blur
    @FXML private Region arkaPlanRegion;
    private final GaussianBlur blurEffect = new GaussianBlur(15);


    @FXML
    public void initialize() {
        instance = this;

        // BAĞIMLILIK BAŞLATMA
        if (ilanController == null) {
            IlanDeposu depo = new IlanDeposu();
            ilanController = new IlanController(depo);
        }
        if (musteriController == null) {
            MusteriKuyrugu kuyruk = new MusteriKuyrugu();
            musteriController = new MusteriController(kuyruk);
        }

        // Başlangıçta Ana Sayfada Blur Yok
        if (arkaPlanRegion != null) {
            arkaPlanRegion.setEffect(null);
        }

        // İstatistikleri yükle
        dashboardVerileriniYukle();


        // Buton Olaylarını Tanımlama
        ayarlaButon(btnIlanEkle, "/IlanFormSecim.fxml");
        ayarlaButon(btnIlanListe, "/IlanListe.fxml");
        ayarlaButon(btnIlanGuncelle, "/IlanGuncelle.fxml");
        ayarlaButon(btnIlanSorgu, "/IlanSorgu.fxml");
        ayarlaButon(btnIlanSil, "/IlanSilForm.fxml");

        ayarlaButon(btnMusteriEkle, "/MusteriForm.fxml");
        ayarlaButon(btnMusteriListe, "/MusteriList.fxml");
        ayarlaButon(btnMusteriSil, "/MusteriSilForm.fxml");
    }

    /**
     * Müşteri ve İlan sayılarını hesaplar ve Label'lara yansıtır.
     */
    public void dashboardVerileriniYukle() {

        if (musteriController != null && musteriSayisiLabel != null) {
            Musteri[] tumMusteriler = musteriController.tumMusterileriGetir();
            musteriSayisiLabel.setText(String.valueOf(tumMusteriler.length));
        }

        if (ilanController != null && ilanSayisiLabel != null) {
            try {
                Ilan[] tumIlanlar = ilanController.getIlanlar();
                ilanSayisiLabel.setText(String.valueOf(tumIlanlar.length));
            } catch (Exception e) {
                ilanSayisiLabel.setText("HATA");
            }
        }
    }


    private void ayarlaButon(Button btn, String fxmlYolu) {
        if (btn != null) {
            btn.setOnAction(e -> ekranAc(fxmlYolu));
        }
    }

    /**
     * Menüden seçilen FXML dosyasını StackPane içine yükler, istatistikleri gizler ve arka plana blur ekler.
     */
    public void ekranAc(String fxmlDosyaYolu) {
        try {
            URL url = getClass().getResource(fxmlDosyaYolu);
            if (url == null) {
                throw new IllegalStateException("FXML bulunamadı: " + fxmlDosyaYolu);
            }

            Node view = FXMLLoader.load(url);

            // FXML Yüklendiğinde, StackPane'in içeriğini değiştir.
            mainContent.getChildren().setAll(view);

            // 🔥 İstatistik Kutularını GİZLE
            if (istatistikKutulari != null) {
                istatistikKutulari.setVisible(false);
                istatistikKutulari.setManaged(false);
            }

            // 🔥 Arka Plana Blur Ekle
            if (arkaPlanRegion != null) {
                arkaPlanRegion.setEffect(blurEffect);
            }


        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hata");
            alert.setHeaderText("Sayfa açılırken kritik hata");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * "Ana Sayfa" butonuna tıklandığında çağrılır. Blur'ı kaldırır ve istatistikleri gösterir.
     */
    @FXML
    private void handleAnaSayfaDonus() {

        // 1. Blur'ı Kaldır (Normal Görüntü)
        if (arkaPlanRegion != null) {
            arkaPlanRegion.setEffect(null);
        }

        // 2. İstatistikleri GÖSTER ve Güncelle
        if (istatistikKutulari != null) {
            dashboardVerileriniYukle();
            istatistikKutulari.setVisible(true);
            istatistikKutulari.setManaged(true);
        }

        // 3. İçeriği temizle (Hoş Geldiniz yazısı ve arka planın görünmesini sağlar)
        mainContent.getChildren().clear();
    }
}