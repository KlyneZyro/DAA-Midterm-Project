package application;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;

public class ThemeParkApp {
    private Stage stage;
    private Font welcomeFontLarge;
    private Font plannerFont;
    private Font labelFont;

    public ThemeParkApp(Stage stage) {
        this.stage = stage;
        loadFonts();
        showMainPage();
    }

    private void loadFonts() {
        String welcomeFontPath = "C:\\Users\\Josh\\Downloads\\font\\HeadingNowTrial-67ExtraboldItalic.ttf";
        String plannerFontPath = "C:\\Users\\Josh\\Downloads\\font\\HeadingNowTrial-16BoldItalic.ttf";

        welcomeFontLarge = Font.loadFont(new File(welcomeFontPath).toURI().toString(), 105);
        if (welcomeFontLarge == null) {
            System.err.println("Failed to load font: " + welcomeFontPath);
            welcomeFontLarge = Font.font("Arial Black", FontWeight.BOLD, FontPosture.ITALIC, 105);
        }

        plannerFont = Font.loadFont(new File(plannerFontPath).toURI().toString(), 135);
        if (plannerFont == null) {
            System.err.println("Failed to load font: " + plannerFontPath);
            plannerFont = Font.font("Arial Narrow", FontWeight.BOLD, FontPosture.ITALIC, 135);
        }

        String welcomeFamily = welcomeFontLarge.getFamily();
        labelFont = Font.font(welcomeFamily, FontWeight.BOLD, FontPosture.REGULAR, 30);
    }

    private void showMainPage() {
        StackPane root = new StackPane();
        setupVideoBackground(root);

        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        VBox welcomeBox = createWelcomeBox();
        VBox inputBox = createInputBox();

        VBox layout = new VBox(-20, welcomeBox, inputBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        root.getChildren().add(layout);

        // Animations
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        TranslateTransition welcomeTranslate = new TranslateTransition(Duration.millis(700), welcomeBox);
        welcomeTranslate.setFromY(-100);
        welcomeTranslate.setToY(-50);

        TranslateTransition inputTranslate = new TranslateTransition(Duration.millis(700), inputBox);
        inputTranslate.setFromY(50);
        inputTranslate.setToY(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, welcomeTranslate, inputTranslate);
        parallelTransition.play();

        Scene scene = new Scene(root, 1000, 700);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        
        // Only on the welcome page, show the ESC hint
        stage.setFullScreenExitHint("Press ESC to exit full screen mode");
        stage.setFullScreen(true);
        stage.show();
    }

    private void setupVideoBackground(StackPane root) {
        String absolutePath = "C:\\Users\\Josh\\Downloads\\bg.mp4";
        File videoFile = new File(absolutePath);
        if (videoFile.exists()) {
            String videoPath = videoFile.toURI().toString();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Stretch the video so it fills the entire window (may distort the image)
            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());

            root.getChildren().add(mediaView);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        } else {
            System.err.println("Video file not found: " + videoFile.getAbsolutePath());
            root.setStyle("-fx-background-color: #121212;");
        }
    }

    private VBox createWelcomeBox() {
        Text welcomeLine1 = new Text("WELCOME");
        welcomeLine1.setFont(welcomeFontLarge);
        welcomeLine1.setFill(Color.WHITE);
        welcomeLine1.setEffect(new DropShadow(35, Color.BLACK));

        Text welcomeLine2 = new Text("TO THE");
        welcomeLine2.setFont(welcomeFontLarge);
        welcomeLine2.setFill(Color.WHITE);
        welcomeLine2.setEffect(new DropShadow(35, Color.BLACK));

        Text plannerText = new Text("THEME PARK RIDE PLANNER");
        plannerText.setFont(plannerFont);
        plannerText.setFill(Color.web("#ff3131"));
        plannerText.setStroke(Color.BLACK);
        plannerText.setStrokeWidth(2);

        VBox welcomeBox = new VBox(-60, welcomeLine1, welcomeLine2, plannerText);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(15));
        welcomeBox.setMinWidth(900);
        welcomeBox.setMaxWidth(1200);
        return welcomeBox;
    }

    private VBox createInputBox() {
        Label titleLabel = new Label("Enter Your Available Time (minutes)");
        titleLabel.setFont(labelFont);
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setWrapText(false);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setEffect(null);

        TextField timeInput = new TextField();
        timeInput.setPromptText("Enter time...");
        timeInput.setFont(Font.font("System", FontWeight.BOLD, 20));
        timeInput.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.3); " +
            "-fx-text-fill: #000000; " +
            "-fx-border-color: black; " +
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 8px; " +
            "-fx-effect: none;"
        );
        timeInput.setPrefWidth(200);
        timeInput.setMaxWidth(300);

        Label errorLabel = new Label("");
        errorLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        errorLabel.setTextFill(Color.web("#ff0000"));
        errorLabel.setStyle("-fx-stroke: #ffffff; -fx-stroke-width: 1;");
        errorLabel.setVisible(false);

        Button nextButton = new Button("Next");
        nextButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        nextButton.setTextFill(Color.web("#000000"));
        nextButton.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3); " +
            "-fx-border-color: black; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 10px 20px; " +
            "-fx-effect: none;"
        );
        nextButton.setOnMouseEntered(e -> {
            nextButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3); " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10px 20px; " +
                "-fx-effect: none;"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), nextButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        nextButton.setOnMouseExited(e -> {
            nextButton.setStyle(
                "-fx-background-color: rgba(255,255,255,0.3); " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 10px 20px; " +
                "-fx-effect: none;"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), nextButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        nextButton.setOnAction(e -> {
            String input = timeInput.getText();
            try {
                int availableTime = Integer.parseInt(input);
                if (availableTime <= 0) throw new NumberFormatException();
                FadeTransition fadeOut = new FadeTransition(Duration.millis(500), stage.getScene().getRoot());
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event -> new RideSelectionPage(stage, availableTime));
                fadeOut.play();
            } catch (NumberFormatException ex) {
                errorLabel.setText("❌ Invalid Input!");
                errorLabel.setVisible(true);
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(ev -> errorLabel.setVisible(false));
                delay.play();
            }
        });

        VBox inputBox = new VBox(15, titleLabel, timeInput, errorLabel, nextButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(20));
        inputBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1); " + 
            "-fx-border-color: black; " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: none;"
        );
        inputBox.setMaxWidth(600);
        return inputBox;
    }
}