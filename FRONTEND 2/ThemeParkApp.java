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
import javafx.scene.control.TextField;
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

public class ThemeParkApp {
    private Stage stage;

    public ThemeParkApp(Stage stage) {
        this.stage = stage;
        showMainPage();
    }

    private void showMainPage() {
        // Root container to layer video + UI
        StackPane root = new StackPane();

        // Video background
        String absolutePath = "C:\\Users\\USER\\Downloads\\mylivewallpapers-com-Cute-City.mp4";
        File videoFile = new File(absolutePath);
        if (videoFile.exists()) {
            String videoPath = videoFile.toURI().toString();
            Media media = new Media(videoPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Keep video filling window, add slight blur
            mediaView.setPreserveRatio(true);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setEffect(new BoxBlur(5, 5, 3));

            root.getChildren().add(mediaView);
            mediaPlayer.play();
        } else {
            System.err.println("Video file not found: " + videoFile.getAbsolutePath());
            root.setStyle("-fx-background-color: #121212;");
        }

        // Semi-transparent overlay to soften background
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        // ========== Title Box ==========
        Label welcomeLabel = new Label("🎢 Welcome to the Theme Park Ride Planner!");
        // Keep large text size
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 50));
        // Prevent text wrapping
        welcomeLabel.setWrapText(false);
        welcomeLabel.setTextAlignment(TextAlignment.CENTER);
        // White text with a black stroke for contrast
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        // Soft pink drop shadow
        welcomeLabel.setEffect(new DropShadow(10, Color.web("#ff99cc")));
        
        // ---- Added: Animated Color-Changing Effect for Title ----
        Timeline gradientAnimation = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(welcomeLabel.textFillProperty(), Color.web("#ff66cc"))),
            new KeyFrame(Duration.seconds(1), new KeyValue(welcomeLabel.textFillProperty(), Color.web("#6699ff"))),
            new KeyFrame(Duration.seconds(2), new KeyValue(welcomeLabel.textFillProperty(), Color.web("#cc66ff"))),
            new KeyFrame(Duration.seconds(3), new KeyValue(welcomeLabel.textFillProperty(), Color.web("#ff66cc")))
        );
        gradientAnimation.setCycleCount(Timeline.INDEFINITE);
        gradientAnimation.setAutoReverse(true);

        // ---- Added: Pulsing Scale Effect for Title ----
        ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), welcomeLabel);
        scaleAnimation.setFromX(1.0);
        scaleAnimation.setFromY(1.0);
        scaleAnimation.setToX(1.1);  // Scale up to 110% of original size
        scaleAnimation.setToY(1.1);  // Scale up to 110% of original size
        scaleAnimation.setCycleCount(Timeline.INDEFINITE);
        scaleAnimation.setAutoReverse(true);

        // Combine color and scale animations
        ParallelTransition titleAnimation = new ParallelTransition(gradientAnimation, scaleAnimation);
        titleAnimation.play();
        // ---------------------------------------------------------

        VBox welcomeBox = new VBox(welcomeLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(15));
        // Ensure the box is wide enough so the text stays on one line
        welcomeBox.setMinWidth(900);  // Adjust if needed
        welcomeBox.setMaxWidth(1200); // Just in case

        // Pastel pink -> peach gradient border
        welcomeBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );

        // ========== Input Box ==========
        Label titleLabel = new Label("Enter Your Available Time (minutes)");
        // Keep it large enough to be visible
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 30));
        titleLabel.setWrapText(false);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        // White text with black stroke
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        titleLabel.setEffect(new DropShadow(5, Color.web("#ffccff")));

        TextField timeInput = new TextField();
        timeInput.setPromptText("Enter time...");
        timeInput.setFont(Font.font("System", FontWeight.BOLD, 20));
        timeInput.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.8); " +
            "-fx-text-fill: #000000; " +
            "-fx-border-color: #ff66cc; " + // Pink border
            "-fx-border-width: 3px; " +
            "-fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; " +
            "-fx-padding: 8px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
        );
        timeInput.setPrefWidth(200);
        timeInput.setMaxWidth(300);

        Label errorLabel = new Label("");
        errorLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        errorLabel.setTextFill(Color.web("#ff0000"));
        errorLabel.setStyle("-fx-stroke: #ffffff; -fx-stroke-width: 1;");
        errorLabel.setVisible(false);

        // Pastel pink -> peach gradient for the button
        Stop[] stops = new Stop[] {
            new Stop(0, Color.web("#ff66cc")),
            new Stop(1, Color.web("#ffcc66"))
        };
        LinearGradient buttonGradient = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops
        );

        Button nextButton = new Button("Next");
        nextButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        // Use black text for contrast on the pastel gradient
        nextButton.setTextFill(Color.web("#000000"));
        nextButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-color: #ffffff; " +
            "-fx-border-width: 2px; -fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
        );

        // Hover effect (reverse gradient, scale up slightly)
        nextButton.setOnMouseEntered(e -> {
            nextButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff66cc); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ffccff, 12, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), nextButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        nextButton.setOnMouseExited(e -> {
            nextButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), nextButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Validate & move to next page
        nextButton.setOnAction(e -> {
            String input = timeInput.getText();
            try {
                int availableTime = Integer.parseInt(input);
                if (availableTime <= 0) throw new NumberFormatException();

                Scene currentScene = stage.getScene();
                if (currentScene != null) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentScene.getRoot());
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(event -> new RideSelectionPage(stage, availableTime));
                    fadeOut.play();
                } else {
                    new RideSelectionPage(stage, availableTime);
                }
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
        // Matching pastel pink -> peach border
        inputBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-width: 4px; -fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );
        // Increase maxWidth so it doesn't crop
        inputBox.setMaxWidth(600);

        // ========== Floating Glowing Particles ==========
        // These particles are added to the root behind the layout for extra visual effect.
        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            // Create a small glowing circle with varying size
            Circle particle = new Circle(3 + rand.nextDouble() * 3, Color.web("#ff99cc"));
            particle.setOpacity(0.7);
            // Randomly position the particle within a range
            particle.setTranslateX(rand.nextInt(1000) - 500);
            particle.setTranslateY(rand.nextInt(700) - 350);
            
            // Animate the particle with a gentle vertical float
            TranslateTransition tt = new TranslateTransition(Duration.seconds(3 + rand.nextDouble() * 2), particle);
            tt.setByY(rand.nextInt(100) - 50);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.setAutoReverse(true);
            tt.play();
            
            root.getChildren().add(particle);
        }

        // ========== Main Layout ==========
        VBox layout = new VBox(40, welcomeBox, inputBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        root.getChildren().add(layout);

        // ========== Animations ==========
        // Fade-in for entire layout
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Slide in the top box from above
        TranslateTransition welcomeTranslate = new TranslateTransition(Duration.millis(700), welcomeBox);
        welcomeTranslate.setFromY(-50);
        welcomeTranslate.setToY(0);

        // Slide in the bottom box from below
        TranslateTransition inputTranslate = new TranslateTransition(Duration.millis(700), inputBox);
        inputTranslate.setFromY(50);
        inputTranslate.setToY(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, welcomeTranslate, inputTranslate);
        parallelTransition.play();

        // Scene and Stage
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }
}