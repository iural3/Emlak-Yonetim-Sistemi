package Main.Ui;

import Main.Emlak.Ilan;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class IlanListeController {

    @FXML private TableView<Ilan> tablo;
    @FXML private TableColumn<Ilan, Integer> colId;
    @FXML private TableColumn<Ilan, String> colBaslik;
    @FXML private TableColumn<Ilan, Double> colFiyat;
    @FXML private TableColumn<Ilan, String> colKonum;

    // YENİ SÜTUN: DETAYLAR
    @FXML private TableColumn<Ilan, String> colDetay;

    @FXML private TableColumn<Ilan, Object> colSure;

    @FXML private Button btnYenile;
    @FXML private Button btnGunAtla;

    @FXML
    public void initialize() {
        // 1. TEMEL SÜTUNLAR
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIlanNo()));
        colBaslik.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIlanBaslik()));
        colFiyat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFiyat()));

        // Konum (İl / İlçe)
        colKonum.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getAdres().getIl() + " / " + cell.getValue().getAdres().getIlce()
        ));

        // 2. YENİ DETAY SÜTUNU (Akıllı Veri)
        // Ilan.java'daki getDetayBilgisi() metodunu kullanır
        if (colDetay != null) {
            colDetay.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDetayBilgisi()));
        }

        // 3. SÜRE SÜTUNU (Renkli Uyarı)
        colSure.setCellValueFactory(cell -> {
            int gun = cell.getValue().getKalanGun();
            if (gun <= 0) return new SimpleObjectProperty<>("SÜRE BİTTİ ⚠️");
            return new SimpleObjectProperty<>(gun + " Gün");
        });

        // 4. BUTON AKSİYONLARI
        btnYenile.setOnAction(e -> listeyiYenile());
        btnGunAtla.setOnAction(e -> gunAtlat());

        // Başlangıçta yükle
        listeyiYenile();
    }

    private void listeyiYenile() {
        if (MainViewController.ilanController != null) {
            Ilan[] ilanlar = MainViewController.ilanController.getIlanlar();
            tablo.setItems(FXCollections.observableArrayList(ilanlar));
            tablo.refresh();
        }
    }

    private void gunAtlat() {
        if (MainViewController.ilanController != null) {
            MainViewController.ilanController.gunuIlerlet();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gün Sonu İşlemi");
            alert.setHeaderText(null);
            alert.setContentText("Tüm ilanların süresi 1 gün azaltıldı.");
            alert.show();
            listeyiYenile();
        }
    }
}