package Main.Ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IlanFormSecimController {

    @FXML private Button btnDaire;
    @FXML private Button btnMustakil;
    @FXML private Button btnArsa;

    @FXML
    public void initialize() {
        // Artık MainViewController üzerinden garanti geçiş yapıyoruz
        btnDaire.setOnAction(e -> sayfaDegistir("/DaireForm.fxml"));
        btnMustakil.setOnAction(e -> sayfaDegistir("/MustakilEvForm.fxml"));
        btnArsa.setOnAction(e -> sayfaDegistir("/ArsaForm.fxml"));
    }

    private void sayfaDegistir(String fxmlYolu) {
        if (MainViewController.instance != null) {
            // Ana kumandayı kullanarak sayfayı değiştir
            MainViewController.instance.ekranAc(fxmlYolu);
        } else {
            System.out.println("HATA: Ana ekran (MainViewController) bulunamadı!");
        }
    }
}