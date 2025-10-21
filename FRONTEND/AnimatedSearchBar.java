package application;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class AnimatedSearchBar extends HBox {
    private TextField searchField;
    private Timeline expandTimeline;
    private Timeline collapseTimeline;
    private Circle circle;
    private Line line;
    
    public AnimatedSearchBar() {
        // Create search icon using shapes
        circle = new Circle(10, 10, 8); // Center at (10, 10), radius 8
        circle.setFill(Color.WHITE); // Changed from TRANSPARENT to WHITE
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        
        line = new Line(15, 15, 25, 25); // Diagonal handle from (15, 15) to (25, 25)
        line.setStroke(Color.BLACK); // Changed from WHITE to BLACK
        line.setStrokeWidth(2);
        
        Group searchIconGroup = new Group(circle, line);
        StackPane searchIconContainer = new StackPane(searchIconGroup);
        searchIconContainer.setPrefSize(20, 20); // Fixed size for the icon
        
        // Create search field
        searchField = new TextField();
        searchField.setPromptText("Search rides...");
        searchField.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: white; -fx-border-color: #ff4b4b;");
        searchField.setPrefWidth(0); // Initially hidden
        searchField.setMaxWidth(0);
        searchField.setOpacity(0);
        
        // Add components to HBox
        getChildren().addAll(searchIconContainer, searchField);
        setSpacing(5); // Space between icon and search field
        
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
                        new KeyValue(searchField.prefWidthProperty(), 0),
                        new KeyValue(searchField.maxWidthProperty(), 0),
                        new KeyValue(searchField.opacityProperty(), 0)
                )
        );
        
        // Event handlers for hover
        setOnMouseEntered(event -> expandTimeline.play());
        
        // Modified to check if searchField is empty before collapsing
        setOnMouseExited(event -> {
            if (searchField.getText().isEmpty()) {
                collapseTimeline.play();
            }
        });
        
        // Listener to prevent collapse when text is present
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty() && searchField.getPrefWidth() == 0) {
                expandTimeline.play();
            }
        });
    }
    
    /** Returns the search field for external access, e.g., to add listeners */
    public TextField getSearchField() {
        return searchField;
    }
}