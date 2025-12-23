package Main.Ui;

import Main.Emlak.Ilan;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class IlanSorguController {

    @FXML private TextField txtMinFiyat, txtMaxFiyat, txtIl, txtIlce, txtOda;
    @FXML private ComboBox<String> cmbEmlakTipi;
    @FXML private Button btnSorgula, btnTemizle;

    @FXML private TableView<Ilan> tablo;
    @FXML private TableColumn<Ilan, Integer> colId;
    @FXML private TableColumn<Ilan, String> colBaslik;
    @FXML private TableColumn<Ilan, Double> colFiyat;
    @FXML private TableColumn<Ilan, String> colIl;
    @FXML private TableColumn<Ilan, String> colIlce;


    @FXML private TableColumn<Ilan, String> colDetay;

    @FXML
    public void initialize() {
        // ComboBox
        cmbEmlakTipi.setItems(FXCollections.observableArrayList("Tümü", "Daire", "Müstakil", "Arsa"));
        cmbEmlakTipi.getSelectionModel().selectFirst();

        // Sütun Bağlantıları
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIlanNo()));
        colBaslik.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIlanBaslik()));
        colFiyat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFiyat()));
        colIl.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdres().getIl()));
        colIlce.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdres().getIlce()));

        if (colDetay != null) {
            colDetay.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDetayBilgisi()));
        }

        // Butonlar
        btnSorgula.setOnAction(e -> sorgula());
        btnTemizle.setOnAction(e -> temizle());
    }

    private void sorgula() {
        try {
            Integer min = txtMinFiyat.getText().isEmpty() ? null : Integer.parseInt(txtMinFiyat.getText());
            Integer max = txtMaxFiyat.getText().isEmpty() ? null : Integer.parseInt(txtMaxFiyat.getText());
            String il = txtIl.getText();
            String ilce = txtIlce.getText();
            String oda = txtOda.getText();
            String tip = cmbEmlakTipi.getValue();
            if (tip == null || tip.equals("Tümü")) tip = "";

            if (MainViewController.ilanController != null) {
                Ilan[] sonuc = MainViewController.ilanController.ilanAra(min, max, il, ilce, oda, tip);
                if (sonuc != null) {
                    tablo.setItems(FXCollections.observableArrayList(sonuc));
                } else {
                    tablo.setItems(FXCollections.observableArrayList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void temizle() {
        txtMinFiyat.clear(); txtMaxFiyat.clear(); txtIl.clear(); txtIlce.clear(); txtOda.clear();
        tablo.getItems().clear();
    }
}