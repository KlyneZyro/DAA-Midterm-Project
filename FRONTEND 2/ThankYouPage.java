package application;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

public class ThankYouPage {
    private Stage stage;

    public ThankYouPage(Stage stage) {
        this.stage = stage;
        showThankYouPage();
    }

    private void showThankYouPage() {
        // Root container to layer video + UI
        StackPane root = new StackPane();

        // Video background
        String absolutePath = "C:\\Users\\USER\\Downloads\\mylivewallpapers-com-Cute-City.mp4";
        File videoFile = new File(absolutePath);
        if (videoFile.exists()) {
            try {
                String videoPath = videoFile.toURI().toString();
                System.out.println("Attempting to load video from: " + videoPath);
                Media media = new Media(videoPath);
                MediaPlayer mediaPlayer = new MediaPlayer(media);

                // Handle potential errors during media playback
                mediaPlayer.setOnError(() -> {
                    System.err.println("MediaPlayer error: " + mediaPlayer.getError().getMessage());
                });

                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                MediaView mediaView = new MediaView(mediaPlayer);

                // Keep video filling window, add slight blur
                mediaView.setPreserveRatio(true);
                mediaView.fitWidthProperty().bind(root.widthProperty());
                mediaView.fitHeightProperty().bind(root.heightProperty());
                mediaView.setEffect(new BoxBlur(5, 5, 3));

                root.getChildren().add(mediaView);
                mediaPlayer.play();
                System.out.println("Video playback started successfully.");
            } catch (Exception e) {
                System.err.println("Error loading video: " + e.getMessage());
                e.printStackTrace();
                root.setStyle("-fx-background-color: #121212;");
            }
        } else {
            System.err.println("Video file not found at: " + videoFile.getAbsolutePath());
            root.setStyle("-fx-background-color: #121212;");
        }

        // Semi-transparent overlay to soften background
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        // ========== Thank You Box ==========
        Label thankYouLabel = new Label("🎢 Thank You for Using the Theme Park Planner!");
        thankYouLabel.setFont(Font.font("System", FontWeight.BOLD, 50));
        thankYouLabel.setWrapText(false);
        thankYouLabel.setTextAlignment(TextAlignment.CENTER);
        thankYouLabel.setTextFill(Color.WHITE);
        thankYouLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        thankYouLabel.setEffect(new DropShadow(10, Color.web("#ff99cc")));

        // Animated Color-Changing Effect for Title
        Timeline gradientAnimation = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(thankYouLabel.textFillProperty(), Color.web("#ff66cc"))),
            new KeyFrame(Duration.seconds(1), new KeyValue(thankYouLabel.textFillProperty(), Color.web("#6699ff"))),
            new KeyFrame(Duration.seconds(2), new KeyValue(thankYouLabel.textFillProperty(), Color.web("#cc66ff"))),
            new KeyFrame(Duration.seconds(3), new KeyValue(thankYouLabel.textFillProperty(), Color.web("#ff66cc")))
        );
        gradientAnimation.setCycleCount(Timeline.INDEFINITE);
        gradientAnimation.setAutoReverse(true);

        // Pulsing Scale Effect for Title
        ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), thankYouLabel);
        scaleAnimation.setFromX(1.0);
        scaleAnimation.setFromY(1.0);
        scaleAnimation.setToX(1.05);
        scaleAnimation.setToY(1.05);
        scaleAnimation.setCycleCount(Timeline.INDEFINITE);
        scaleAnimation.setAutoReverse(true);

        // Combine animations
        ParallelTransition titleAnimation = new ParallelTransition(gradientAnimation, scaleAnimation);
        titleAnimation.play();

        VBox thankYouBox = new VBox(thankYouLabel);
        thankYouBox.setAlignment(Pos.CENTER);
        thankYouBox.setPadding(new Insets(20));
        thankYouBox.setMinWidth(900);
        thankYouBox.setMaxWidth(1200);
        thankYouBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );

        // ========== Credits and Exit Box ==========
        Label teamLabel = new Label("Created by:\n Gonda, Micole Kurt\nReyes, Eran Josh\nReyes, Klyne Zyro\nVelasco, Zyrus");
        teamLabel.setFont(Font.font("System", FontWeight.BOLD, 30));
        teamLabel.setWrapText(false);
        teamLabel.setTextAlignment(TextAlignment.CENTER);
        teamLabel.setTextFill(Color.WHITE);
        teamLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        teamLabel.setEffect(new DropShadow(5, Color.web("#ffccff")));

        // Pastel pink -> peach gradient for the button
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#ff66cc")),
            new Stop(1, Color.web("#ffcc66"))
        };
        LinearGradient buttonGradient = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );

        Button exitButton = new Button("Exit 🚪");
        exitButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        exitButton.setTextFill(Color.web("#000000"));
        exitButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-color: #ffffff; " +
            "-fx-border-width: 2px; -fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
        );

        // Hover effect for exit button
        exitButton.setOnMouseEntered(e -> {
            exitButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff66cc); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ffccff, 12, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), exitButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        exitButton.setOnMouseExited(e -> {
            exitButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), exitButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Exit action
        exitButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> stage.close());
            fadeOut.play();
        });

        VBox creditsBox = new VBox(15, teamLabel, exitButton);
        creditsBox.setAlignment(Pos.CENTER);
        creditsBox.setPadding(new Insets(20));
        creditsBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-width: 4px; -fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );
        creditsBox.setMaxWidth(600);

        // ========== Floating Glowing Particles ==========
        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            Circle particle = new Circle(3 + rand.nextDouble() * 3, Color.web("#ff99cc"));
            particle.setOpacity(0.7);
            particle.setTranslateX(rand.nextInt(1000) - 500);
            particle.setTranslateY(rand.nextInt(700) - 350);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(3 + rand.nextDouble() * 2), particle);
            tt.setByY(rand.nextInt(100) - 50);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.setAutoReverse(true);
            tt.play();

            root.getChildren().add(particle);
        }

        // ========== Main Layout ==========
        VBox layout = new VBox(20, thankYouBox, creditsBox);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(100, 30, 30, 30));

        root.getChildren().add(layout);

        // ========== Animations ==========
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        TranslateTransition thankYouTranslate = new TranslateTransition(Duration.millis(700), thankYouBox);
        thankYouTranslate.setFromY(-50);
        thankYouTranslate.setToY(0);

        TranslateTransition creditsTranslate = new TranslateTransition(Duration.millis(700), creditsBox);
        creditsTranslate.setFromY(50);
        creditsTranslate.setToY(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, thankYouTranslate, creditsTranslate);
        parallelTransition.play();

        // Scene and Stage
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setFullScreen(true);  // Added fullscreen mode
        stage.show();
    }
}