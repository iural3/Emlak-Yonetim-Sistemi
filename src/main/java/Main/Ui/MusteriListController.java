package Main.Ui;

import Main.User.Musteri;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MusteriListController {

    @FXML private TableView<Musteri> tablo;

    // TEMEL BİLGİLER
    @FXML private TableColumn<Musteri, Integer> colId;
    @FXML private TableColumn<Musteri, String> colTc;
    @FXML private TableColumn<Musteri, String> colAd;
    @FXML private TableColumn<Musteri, String> colTel;
    @FXML private TableColumn<Musteri, String> colEposta;

    // TERCİH BİLGİLERİ (Gözükmeyen 4 Alan)
    @FXML private TableColumn<Musteri, String> colTip;
    @FXML private TableColumn<Musteri, Double> colButce;
    @FXML private TableColumn<Musteri, Double> colMinM2;
    @FXML private TableColumn<Musteri, Integer> colMinOda;


    @FXML
    public void initialize() {

        // 1. TEMEL BİLGİLER
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getkullaniciId()));
        colTc.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTc()));
        colAd.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAd()));

        // KRİTİK DÜZELTME: Telefon ve E-posta getter'ları
        colTel.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().gettelefon()));
        colEposta.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().geteposta()));


        // 2. TERCİH ALANLARI
        if (colTip != null) colTip.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmlakTipi()));
        if (colButce != null) colButce.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getButce()));
        if (colMinM2 != null) colMinM2.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMinM2()));
        if (colMinOda != null) colMinOda.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getMinOda()));

        listeyiYenile();
    }

    // Bu metot FXML'den veya initialize'dan çağrılır
    public void listeyiYenile() {
        if (MainViewController.musteriController != null) {
            Musteri[] musteriler = MainViewController.musteriController. tumMusterileriGetir();

            if (musteriler != null) {
                System.out.println("✅ " + musteriler.length + " müşteri tabloya basılıyor.");
                tablo.setItems(FXCollections.observableArrayList(musteriler));
                tablo.refresh();
            }
        }
    }
}