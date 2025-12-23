package Main.Ui;

import Main.Emlak.Ilan;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class IlanSilFormController {

    @FXML private TableView<Ilan> tabloSil;
    @FXML private TableColumn<Ilan, Integer> colId;
    @FXML private TableColumn<Ilan, String> colBaslik;
    @FXML private TableColumn<Ilan, Double> colFiyat;
    @FXML private TableColumn<Ilan, String> colIl;

    @FXML private TableColumn<Ilan, Void> colSilButon; // Buton sütunu

    @FXML
    public void initialize() {
        // 1. SÜTUNLARI BAĞLA
        // Eğer FXML'den düzgün yüklenmezse burada NullPointer oluşur.
        // Bu yüzden FXML'deki fx:id'ler ile bu değişken isimlerinin tam uyumu KRİTİKTİR.

        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getIlanNo()));
        colBaslik.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIlanBaslik()));
        colFiyat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFiyat()));
        colIl.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAdres().getIl()));

        // 2. SİLME BUTONU EKLE
        if (colSilButon != null) {
            butonSutunuEkle();
        }

        // 3. TABLOYU DOLDUR
        listeyiYenile();
    }

    private void listeyiYenile() {
        if (MainViewController.ilanController != null) {
            Ilan[] ilanlar = MainViewController.ilanController.getIlanlar();
            if (ilanlar != null && tabloSil != null) {
                tabloSil.setItems(FXCollections.observableArrayList(ilanlar));
            }
        }
    }

    private void butonSutunuEkle() {
        Callback<TableColumn<Ilan, Void>, TableCell<Ilan, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Ilan, Void> call(final TableColumn<Ilan, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("X");

                    {
                        btn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
                        btn.setOnAction(event -> {
                            Ilan secilenIlan = getTableView().getItems().get(getIndex());
                            silmeIslemi(secilenIlan);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colSilButon.setCellFactory(cellFactory);
    }

    private void silmeIslemi(Ilan ilan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("İlan Silinecek: " + ilan.getIlanBaslik());

        if (alert.showAndWait().get() == ButtonType.OK) {
            boolean silindi = MainViewController.ilanController.ilanSil(ilan.getIlanNo());

            if (silindi) {
                // UI'dan da sil ki anında güncellensin
                tabloSil.getItems().remove(ilan);
            }
        }
    }
}