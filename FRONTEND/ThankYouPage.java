package application;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import javafx.geometry.Insets;

public class ThankYouPage {
    private Stage stage;

    public ThankYouPage(Stage stage) {
        this.stage = stage;
        showThankYouPage();
    }

    private void showThankYouPage() {
        StackPane root = new StackPane();
        setupVideoBackground(root);

        // Semi-transparent overlay for content readability
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        // Load fonts for consistency with other pages
        String welcomeFontPath = "C:\\Users\\Josh\\Downloads\\font\\HeadingNowTrial-67ExtraboldItalic.ttf";
        Font welcomeFont = Font.loadFont(new File(welcomeFontPath).toURI().toString(), 55);
        if (welcomeFont == null) {
            welcomeFont = Font.font("Arial Black", FontWeight.BOLD, FontPosture.ITALIC, 55);
        }

        // Title styled like "Select Rides" from page 2
        Text thankYouText = new Text("Thank You for Using The Theme Park Planner!");
        thankYouText.setFont(welcomeFont);
        thankYouText.setFill(Color.web("#ff3131"));
        thankYouText.setStroke(Color.BLACK);
        thankYouText.setStrokeWidth(2);
        
        // Add looping animations to the thank you text
        setupLoopingAnimation(thankYouText);

        // Team members text styled like "WELCOME TO THE" from first page
        Text teamText = new Text("Created by:");
        teamText.setFont(welcomeFont);
        teamText.setFill(Color.WHITE);
        teamText.setStroke(Color.BLACK);
        teamText.setStrokeWidth(1);
        
        // Team member names
        Text namesText = new Text(
            "Reyes, Klyne Zyro\n" +
            "Velasco, Zyrus\n" +
            "Gonda, Micole Kurt\n" +
            "Reyes, Eran Josh"
        );
        namesText.setFont(Font.font("Arial", FontWeight.BOLD, 35));
        namesText.setFill(Color.WHITE);
        namesText.setStroke(Color.BLACK);
        namesText.setStrokeWidth(1.2);
        
        // Create a box for the team credits - similar to the input box in ThemeParkApp
        VBox teamBox = new VBox(15, teamText, namesText);
        teamBox.setAlignment(Pos.CENTER);
        teamBox.setPadding(new Insets(20));
        teamBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1); " + 
            "-fx-border-color: black; " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: none;"
        );
        teamBox.setMaxWidth(600);
        
        Button exitButton = new Button("Exit");
        exitButton.setStyle(
            "-fx-background-color: #ff4b4b; " +
            "-fx-text-fill: black; " +
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-radius: 20px; " +
            "-fx-padding: 10px 30px;"
        );
        exitButton.setOnAction(e -> stage.close());

        VBox layout = new VBox(20, thankYouText, teamBox, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 50px;");

        // Add the content on top of the video background
        root.getChildren().add(layout);

        Scene scene = new Scene(root, 600, 400);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }

    private void setupVideoBackground(StackPane root) {
        if (MediaCache.BACKGROUND_MEDIA != null) {
            MediaPlayer mediaPlayer = new MediaPlayer(MediaCache.BACKGROUND_MEDIA);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Stretch the video fully to fill the background
            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());

            root.getChildren().add(mediaView);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        } else {
            root.setStyle("-fx-background-color: #121212;");
        }
    }
    
    private void setupLoopingAnimation(Text thankYouText) {
        // Scale animation - pulse effect
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(400), thankYouText);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(400), thankYouText);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        // Color transition - change color brightness
        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), thankYouText);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.8);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), thankYouText);
        fadeIn.setFromValue(0.8);
        fadeIn.setToValue(1.0);
        
        // Slight rotation for dynamic effect
        RotateTransition rotateRight = new RotateTransition(Duration.millis(500), thankYouText);
        rotateRight.setByAngle(2);
        
        RotateTransition rotateLeft = new RotateTransition(Duration.millis(500), thankYouText);
        rotateLeft.setByAngle(-2);
        
        // Slight vertical movement
        TranslateTransition moveUp = new TranslateTransition(Duration.millis(700), thankYouText);
        moveUp.setByY(-10);
        
        TranslateTransition moveDown = new TranslateTransition(Duration.millis(700), thankYouText);
        moveDown.setByY(10);
        
        // Combine animations into parallel animations for each phase
        ParallelTransition growPhase = new ParallelTransition(scaleUp, fadeOut, rotateRight, moveUp);
        ParallelTransition shrinkPhase = new ParallelTransition(scaleDown, fadeIn, rotateLeft, moveDown);
        
        // Combine phases into a sequential animation that will loop
        SequentialTransition sequence = new SequentialTransition(growPhase, shrinkPhase);
        sequence.setCycleCount(Timeline.INDEFINITE); // Loop indefinitely
        sequence.setAutoReverse(true);
        
        // Start the animation sequence
        sequence.play();
    }
}