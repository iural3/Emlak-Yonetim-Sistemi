package Main.Ui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private VBox loginForm;
    @FXML private ImageView leftDoor;
    @FXML private ImageView rightDoor;

    // Form Elemanları
    @FXML private TextField txtTc;
    @FXML private TextField txtUser;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPass;
    @FXML private Label lblError;
    @FXML private Button btnLogin;

    // Efekt Elemanları
    @FXML private StackPane effectPane;
    @FXML private Circle lightBurst;
    @FXML private Pane confettiPane;
    @FXML private Text welcomeText;

    private final Random random = new Random();

    // Konfeti renkleri daha canlı hale getirildi
    private static final Color[] CONFETTI_COLORS = {
            Color.RED, Color.CYAN, Color.GOLD, Color.LIME,
            Color.MAGENTA, Color.ORANGE, Color.WHITE, Color.PINK, Color.LIGHTBLUE
    };

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        prepareConfetti();
    }

    private void prepareConfetti() {
        // Konfeti sayısı artırıldı (100 -> 150) daha yoğun görünmesi için
        for (int i = 0; i < 150; i++) {
            javafx.scene.shape.Shape particle;

            // Konfeti boyutları büyütüldü (Daha net görünmesi için)
            if (random.nextBoolean()) {
                // Yarıçap 4 ile 9 arasında (Eskisi 2-6 idi)
                particle = new Circle(random.nextDouble() * 5 + 4);
            } else {
                // Kare boyutları büyütüldü
                particle = new Rectangle(random.nextDouble() * 10 + 6, random.nextDouble() * 10 + 6);
            }

            particle.setFill(CONFETTI_COLORS[random.nextInt(CONFETTI_COLORS.length)]);
            particle.setTranslateX(0);
            particle.setTranslateY(0);
            particle.setOpacity(0.0);
            confettiPane.getChildren().add(particle);
        }
    }

    @FXML
    public void handleLoginButton() {
        lblError.setText("");

        String tc = txtTc.getText().trim();
        String user = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = txtPass.getText().trim();

        // 1. BOŞ ALAN KONTROLÜ
        if (tc.isEmpty() || user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            lblError.setText("Lütfen tüm alanları doldurunuz.");
            return;
        }

        // 2. TC KİMLİK KONTROLÜ
        if (!tc.matches("\\d+") || tc.length() != 11) {
            lblError.setText("Hata: TC No 11 haneli rakam olmalıdır.");
            return;
        }

        int sonRakam = Character.getNumericValue(tc.charAt(10));
        if (sonRakam % 2 != 0) {
            lblError.setText("Geçersiz TC: TC Kimlik No çift sayı ile bitmelidir.");
            return;
        }

        // 3. KULLANICI ADI KONTROLÜ
        if (user.length() > 15) {
            lblError.setText("Hata: Kullanıcı adı max 15 karakter olabilir.");
            return;
        }
        if (!user.equals("anirx")) {
            lblError.setText("Hata: Kullanıcı adı yanlış.");
            return;
        }

        // 4. E-POSTA KONTROLÜ
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            lblError.setText("Hata: Geçersiz e-posta formatı (örnek: abc@mail.com).");
            return;
        }

        // 5. ŞİFRE KONTROLÜ
        if (!pass.equals("1234")) {
            lblError.setText("Hata: Şifre yanlış.");
            return;
        }

        performEntranceSequence();
    }

    private void performEntranceSequence() {
        btnLogin.setDisable(true);
        lblError.setText("Giriş Başarılı! Hoş Geldiniz...");
        lblError.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");

        FadeTransition fadeOutForm = new FadeTransition(Duration.seconds(0.5), loginForm);
        fadeOutForm.setFromValue(1.0);
        fadeOutForm.setToValue(0.0);

        fadeOutForm.setOnFinished(e -> {
            loginForm.setVisible(false);
            startMainShow();
        });

        fadeOutForm.play();
    }

    private void startMainShow() {
        Scene scene = rootPane.getScene();
        PerspectiveCamera camera = new PerspectiveCamera();
        scene.setCamera(camera);

        Rotate rotateLeft = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        leftDoor.getTransforms().add(rotateLeft);
        Rotate rotateRight = new Rotate(0, rightDoor.getFitWidth(), 0, 0, Rotate.Y_AXIS);
        rightDoor.getTransforms().add(rotateRight);

        Timeline doorTimeline = new Timeline();
        KeyValue kvLeft = new KeyValue(rotateLeft.angleProperty(), -85);
        KeyValue kvRight = new KeyValue(rotateRight.angleProperty(), 85);
        KeyFrame kfDoor = new KeyFrame(Duration.seconds(2.5), kvLeft, kvRight);
        doorTimeline.getKeyFrames().add(kfDoor);

        FadeTransition showEffectPane = new FadeTransition(Duration.seconds(0.1), effectPane);
        showEffectPane.setToValue(1.0);

        // Işık patlaması biraz daha büyütüldü
        ScaleTransition expandLight = new ScaleTransition(Duration.seconds(1.2), lightBurst);
        expandLight.setFromX(0.1); expandLight.setFromY(0.1);
        expandLight.setToX(8.0); expandLight.setToY(8.0);

        FadeTransition fadeLight = new FadeTransition(Duration.seconds(1.5), lightBurst);
        fadeLight.setFromValue(1.0); fadeLight.setToValue(0.0);

        // --- KONFETİ ANİMASYONU ---
        ParallelTransition confettiAnim = new ParallelTransition();
        for (var node : confettiPane.getChildren()) {
            // Süreler uzatıldı (Daha uzun süre havada kalsınlar)
            TranslateTransition fly = new TranslateTransition(Duration.seconds(random.nextDouble() * 2.0 + 1.5), node);

            // Sağa sola yayılma (Spread) artırıldı
            fly.setByX((random.nextDouble() - 0.5) * 900);
            // Yukarı fırlama mesafesi
            fly.setByY(random.nextDouble() * -600 - 150);

            RotateTransition spin = new RotateTransition(Duration.seconds(random.nextDouble() + 0.5), node);
            spin.setByAngle(random.nextDouble() * 720 - 360);
            spin.setCycleCount(4); // Dönme sayısı artırıldı

            FadeTransition show = new FadeTransition(Duration.seconds(0.1), node); show.setToValue(1.0);

            // Görünür kalma süresi uzatıldı (Delay artırıldı)
            FadeTransition hide = new FadeTransition(Duration.seconds(1.0), node);
            hide.setDelay(Duration.seconds(1.5)); // 1.5 saniye net görünsün sonra silinsin
            hide.setToValue(0.0);

            SequentialTransition particleSeq = new SequentialTransition(show, new ParallelTransition(fly, spin), hide);
            confettiAnim.getChildren().add(particleSeq);
        }

        // --- HOŞ GELDİNİZ YAZISI AYARI ---
        // Yazıyı manuel olarak biraz daha aşağı itiyoruz ki kapının tam ortasına gelsin
        welcomeText.setTranslateY(50); // Bu değer yazıyı aşağı iter.

        FadeTransition showWelcome = new FadeTransition(Duration.seconds(0.8), welcomeText);
        showWelcome.setToValue(1.0);

        ScaleTransition popWelcome = new ScaleTransition(Duration.seconds(1.0), welcomeText);
        popWelcome.setFromX(0.8); popWelcome.setFromY(0.8);
        popWelcome.setToX(1.1); popWelcome.setToY(1.1);

        ParallelTransition welcomeAnim = new ParallelTransition(showWelcome, popWelcome);
        welcomeAnim.setDelay(Duration.seconds(0.6));

        ParallelTransition fullSequence = new ParallelTransition(
                doorTimeline,
                new SequentialTransition(showEffectPane, new ParallelTransition(expandLight, fadeLight)),
                confettiAnim,
                welcomeAnim
        );

        fullSequence.setOnFinished(event -> {
            try {
                // Bekleme süresi: Yazıyı biraz okusunlar sonra geçiş yapsın
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(e -> {
                    try {
                        switchToMainPage();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                pause.play();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fullSequence.play();
    }

    private void switchToMainPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/MainView.fxml")
        );
        Parent root = loader.load();

        Stage oldStage = (Stage) rootPane.getScene().getWindow();
        oldStage.close();

        Stage newStage = new Stage();
        newStage.setTitle("Emlak Ofisi");
        // Main view'ın da tam ekran açılması için:
        newStage.setMaximized(true);
        newStage.setScene(new Scene(root));
        newStage.show();
    }
}