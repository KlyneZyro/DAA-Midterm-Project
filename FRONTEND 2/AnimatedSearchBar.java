package application;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class AnimatedSearchBar extends HBox {
    private TextField searchField;
    private Timeline expandTimeline;
    private Timeline collapseTimeline;
    private ScaleTransition scaleTransition;

    public AnimatedSearchBar() {
        // Create search icon using shapes
        Circle circle = new Circle(10, 10, 8); // Center at (10, 10), radius 8
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.web("#ff66cc")); // Pastel pink gradient start
        circle.setStrokeWidth(2);
        Line line = new Line(15, 15, 25, 25); // Diagonal handle from (15, 15) to (25, 25)
        line.setStroke(Color.web("#ffcc66")); // Peach gradient end
        line.setStrokeWidth(2);
        Group searchIconGroup = new Group(circle, line);
        StackPane searchIconContainer = new StackPane(searchIconGroup);
        searchIconContainer.setPrefSize(20, 20); // Fixed size for the icon

        // Create search field
        searchField = new TextField();
        searchField.setPromptText("Search rides..."); // Prompt text always visible
        searchField.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " + // Pastel pink to peach gradient
            "-fx-text-fill: white; " +
            "-fx-border-color: #ffffff; " + // White border
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; " +
            "-fx-padding: 5px; " +
            "-fx-stroke: #000000; -fx-stroke-width: 1;" // White text with black stroke
        );
        searchField.setPrefWidth(100); // Minimum width to show prompt
        searchField.setMaxWidth(100); // Minimum width to show prompt
        searchField.setOpacity(0.7); // Slightly visible when collapsed

        // Add glowing effect
        DropShadow dropShadow = new DropShadow(8, Color.web("#ff99cc"));
        searchField.setEffect(dropShadow);

        // Add components to HBox
        getChildren().addAll(searchIconContainer, searchField);
        setSpacing(5); // Space between icon and search field
        setAlignment(Pos.CENTER_LEFT);

        // Set up expand animation
        expandTimeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(searchField.prefWidthProperty(), 200),
                        new KeyValue(searchField.maxWidthProperty(), 200),
                        new KeyValue(searchField.opacityProperty(), 1)
                )
        );

        // Set up collapse animation
        collapseTimeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(searchField.prefWidthProperty(), 100), // Collapse to minimum width
                        new KeyValue(searchField.maxWidthProperty(), 100), // Collapse to minimum width
                        new KeyValue(searchField.opacityProperty(), 0.7) // Slightly visible
                )
        );

        // Set up scale animation for hover effect
        scaleTransition = new ScaleTransition(Duration.millis(200), searchField);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);

        // Event handlers for hover
        setOnMouseEntered(event -> {
            expandTimeline.play();
            scaleTransition.play();
        });
        
        // Modified to check if searchField is empty before collapsing
        setOnMouseExited(event -> {
            if (searchField.getText().isEmpty()) {
                collapseTimeline.play();
            }
        });

        // Listener to prevent collapse when text is present
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && searchField.getPrefWidth() == 100) {
                expandTimeline.play();
            }
        });
    }

    /** Returns the search field for external access, e.g., to add listeners */
    public TextField getSearchField() {
        return searchField;
    }
}