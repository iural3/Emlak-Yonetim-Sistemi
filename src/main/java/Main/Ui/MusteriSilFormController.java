package Main.Ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MusteriSilFormController {

    @FXML private TextField txtSilId;

    @FXML
    private void musteriyiSil() {
        String idStr = txtSilId.getText();

        if (idStr.trim().isEmpty()) {
            bilgiVer(Alert.AlertType.WARNING, "Uyarı", "Lütfen silinecek müşteri ID'sini giriniz.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            if (MainViewController.musteriController != null) {

                // Controller'daki musteriSil metodunu çağır (Bu metot diske kaydetmeyi de yapar)
                boolean basarili = MainViewController.musteriController.musteriSil(id);

                if (basarili) {
                    bilgiVer(Alert.AlertType.INFORMATION, "Başarılı", "Müşteri ID " + id + " başarıyla silindi.");
                    txtSilId.clear();
                } else {
                    bilgiVer(Alert.AlertType.ERROR, "Hata", "Müşteri ID " + id + " bulunamadı veya silinemedi.");
                }
            }
        } catch (NumberFormatException e) {
            bilgiVer(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir sayısal ID giriniz.");
        }
    }

    private void bilgiVer(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}