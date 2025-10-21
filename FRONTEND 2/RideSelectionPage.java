package application;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.util.ArrayList;
import java.util.List;

public class RideSelectionPage {
    private Stage stage;
    private int availableTime;
    private RideList rideList;
    private ObservableList<Ride> allRides;
    private List<Ride> selectedRides;
    private IntegerProperty totalTime;
    private Label timeLabel;
    private GridPane ridesGrid;
    private FilteredList<Ride> filteredRides;
    private ToggleGroup sortGroup;

    public RideSelectionPage(Stage stage, int availableTime) {
        this.stage = stage;
        this.availableTime = availableTime;
        this.rideList = new RideList();
        this.allRides = FXCollections.observableArrayList(rideList.getRides());
        this.selectedRides = new ArrayList<>();
        this.totalTime = new SimpleIntegerProperty(0);
        this.filteredRides = new FilteredList<>(allRides, p -> true);
        this.sortGroup = new ToggleGroup();
        initUI();
    }

    private void initUI() {
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
            mediaView.setPreserveRatio(true);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            mediaView.setEffect(new BoxBlur(3, 3, 2)); // Reduced blur intensity
            root.getChildren().add(mediaView);
            mediaPlayer.play();
        } else {
            System.err.println("Video file not found: " + videoFile.getAbsolutePath());
            root.setStyle("-fx-background-color: #121212;");
        }

        // Semi-transparent overlay
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        // ========== Single Large Container ==========
        VBox layout = new VBox(20); // Reduced spacing to 20
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20)); // Adjusted padding
        layout.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.15); " +
            "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );
        layout.setMaxWidth(800); // Limit max width
        layout.setMaxHeight(600); // Limit max height
        HBox.setHgrow(layout, Priority.NEVER);
        VBox.setVgrow(layout, Priority.NEVER);

        // **Title**
        Label titleLabel = new Label("🎢 Select Your Rides");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 50));
        titleLabel.setWrapText(false);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        titleLabel.setEffect(new DropShadow(10, Color.web("#ff99cc")));

        // Animated Color-Changing Effect
        Timeline gradientAnimation = new Timeline(
            new KeyFrame(Duration.seconds(0), new KeyValue(titleLabel.textFillProperty(), Color.web("#ff66cc"))),
            new KeyFrame(Duration.seconds(1), new KeyValue(titleLabel.textFillProperty(), Color.web("#6699ff"))),
            new KeyFrame(Duration.seconds(2), new KeyValue(titleLabel.textFillProperty(), Color.web("#cc66ff"))),
            new KeyFrame(Duration.seconds(3), new KeyValue(titleLabel.textFillProperty(), Color.web("#ff66cc")))
        );
        gradientAnimation.setCycleCount(Timeline.INDEFINITE);
        gradientAnimation.setAutoReverse(true);

        // Add pulsing scale effect
        ScaleTransition scaleAnimation = new ScaleTransition(Duration.seconds(2), titleLabel);
        scaleAnimation.setFromX(1.0);
        scaleAnimation.setFromY(1.0);
        scaleAnimation.setToX(1.1);
        scaleAnimation.setToY(1.1);
        scaleAnimation.setCycleCount(Timeline.INDEFINITE);
        scaleAnimation.setAutoReverse(true);

        ParallelTransition titleAnimation = new ParallelTransition(gradientAnimation, scaleAnimation);
        titleAnimation.play();

        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        // **Suggested Rides Section**
        Label suggestedLabel = new Label("Suggested Rides:");
        suggestedLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        suggestedLabel.setTextFill(Color.WHITE);
        suggestedLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        suggestedLabel.setEffect(new DropShadow(5, Color.web("#ffccff")));
        HBox suggestedLabelBox = new HBox(suggestedLabel);
        suggestedLabelBox.setAlignment(Pos.CENTER);

        VBox suggestedBox = createSuggestedRidesBox();
        HBox suggestedBoxWrapper = new HBox(suggestedBox);
        suggestedBoxWrapper.setAlignment(Pos.CENTER);

        VBox suggestedSection = new VBox(5, suggestedLabelBox, suggestedBoxWrapper);
        suggestedSection.setAlignment(Pos.CENTER);

        // **Search and Sort Section**
        AnimatedSearchBar searchBar = new AnimatedSearchBar();
        searchBar.getSearchField().textProperty().addListener((obs, oldText, newText) -> {
            filteredRides.setPredicate(ride -> {
                if (newText == null || newText.isEmpty()) return true;
                return ride.getName().toLowerCase().contains(newText.toLowerCase());
            });
            updateRidesGrid();
        });

        Label sortLabel = new Label("Sort by:");
        sortLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        sortLabel.setTextFill(Color.WHITE);
        sortLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        sortLabel.setEffect(new DropShadow(5, Color.web("#ffccff")));

        ToggleButton alphaButton = new ToggleButton("Aa");
        alphaButton.setToggleGroup(sortGroup);
        alphaButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(255, 102, 204, 0.3), #ffcc66); " + // Semi-transparent pastel gradient
            "-fx-text-fill: white; " +
            "-fx-border-color: #ff66cc; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 5px 10px; " +
            "-fx-stroke: #000000; -fx-stroke-width: 1;"
        );

        ToggleButton popButton = new ToggleButton("★");
        popButton.setToggleGroup(sortGroup);
        popButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(255, 102, 204, 0.3), #ffcc66); " + // Semi-transparent pastel gradient
            "-fx-text-fill: white; " +
            "-fx-border-color: #ff66cc; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 5px 10px; " +
            "-fx-stroke: #000000; -fx-stroke-width: 1;"
        );

        ToggleButton timeButton = new ToggleButton("🕒");
        timeButton.setToggleGroup(sortGroup);
        timeButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, rgba(255, 102, 204, 0.3), #ffcc66); " + // Semi-transparent pastel gradient
            "-fx-text-fill: white; " +
            "-fx-border-color: #ff66cc; " +
            "-fx-border-radius: 5px; " +
            "-fx-padding: 5px 10px; " +
            "-fx-stroke: #000000; -fx-stroke-width: 1;"
        );

        alphaButton.setOnAction(e -> sortRides(1));
        popButton.setOnAction(e -> sortRides(2));
        timeButton.setOnAction(e -> sortRides(3));

        sortGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ((ToggleButton) newToggle).setStyle(
                    "-fx-background-color: #ff66cc; " +
                    "-fx-text-fill: #121212; " +
                    "-fx-border-color: #ffffff; " + // White border when selected
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 5px 10px;"
                );
            }
            if (oldToggle != null) {
                ((ToggleButton) oldToggle).setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(255, 102, 204, 0.3), #ffcc66); " +
                    "-fx-text-fill: white; " +
                    "-fx-border-color: #ff66cc; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-padding: 5px 10px; " +
                    "-fx-stroke: #000000; -fx-stroke-width: 1;"
                );
            }
        });

        Region fixedSpacer = new Region();
        fixedSpacer.setPrefWidth(20);

        HBox searchAndSortBox = new HBox(searchBar, fixedSpacer, sortLabel, alphaButton, popButton, timeButton);
        searchAndSortBox.setAlignment(Pos.CENTER);

        // **Rides Grid**
        ridesGrid = new GridPane();
        ridesGrid.setHgap(10);
        ridesGrid.setVgap(15);
        ridesGrid.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 3);
            ridesGrid.getColumnConstraints().add(colConst);
        }
        updateRidesGrid();
        HBox ridesGridWrapper = new HBox(ridesGrid);
        ridesGridWrapper.setAlignment(Pos.CENTER);

        // **Time Label**
        timeLabel = new Label("Current Time: 0 / " + availableTime + " min");
        timeLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        timeLabel.setEffect(new DropShadow(5, Color.web("#ffccff")));
        HBox timeLabelBox = new HBox(timeLabel);
        timeLabelBox.setAlignment(Pos.CENTER);

        // **Proceed Button**
        Button proceedButton = new Button("Proceed");
        proceedButton.setFont(Font.font("System", FontWeight.BOLD, 20));
        proceedButton.setTextFill(Color.web("#000000"));
        proceedButton.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
            "-fx-border-color: #ffffff; " +
            "-fx-border-width: 2px; -fx-border-radius: 15px; " +
            "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
            "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
        );
        proceedButton.setOnMouseEntered(e -> {
            proceedButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff66cc); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ff99cc, 15, 0, 0, 0);" // Increased glow intensity
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), proceedButton);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        proceedButton.setOnMouseExited(e -> {
            proceedButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                "-fx-border-color: #ffffff; " +
                "-fx-border-width: 2px; -fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), proceedButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        proceedButton.setOnAction(e -> {
            if (totalTime.get() <= availableTime) {
                OptimalPathPage optimalPathPage = new OptimalPathPage(stage, selectedRides);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Time Limit Exceeded");
                alert.setHeaderText(null);
                alert.setContentText("Total time exceeds the limit. Please deselect some rides.");
                alert.showAndWait();
            }
        });
        HBox proceedButtonBox = new HBox(proceedButton);
        proceedButtonBox.setAlignment(Pos.CENTER);

        // Assemble layout
        layout.getChildren().addAll(
            titleBox, suggestedSection, searchAndSortBox,
            ridesGridWrapper, timeLabelBox, proceedButtonBox
        );

        // ========== Floating Glowing Particles ==========
        Random rand = new Random();
        for (int i = 0; i < 40; i++) {
            double size = 2 + rand.nextDouble() * 5; // Varied particle sizes (2 to 7)
            Circle particle = new Circle(size, Color.web("#ff99cc"));
            particle.setOpacity(0.7);
            particle.setTranslateX(rand.nextInt(1000) - 500);
            particle.setTranslateY(rand.nextInt(700) - 350);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(3 + rand.nextDouble() * 2), particle);
            tt.setByY(rand.nextInt(100) - 50);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.setAutoReverse(true);

            RotateTransition rt = new RotateTransition(Duration.seconds(4 + rand.nextDouble() * 2), particle);
            rt.setByAngle(360);
            rt.setCycleCount(RotateTransition.INDEFINITE);

            ParallelTransition particleAnimation = new ParallelTransition(tt, rt);
            particleAnimation.play();

            root.getChildren().add(particle);
        }

        root.getChildren().add(layout);

        // ========== Animations ==========
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        TranslateTransition layoutTranslate = new TranslateTransition(Duration.millis(700), layout);
        layoutTranslate.setFromY(50);
        layoutTranslate.setToY(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, layoutTranslate);
        parallelTransition.play();

        // Scene and Stage
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private VBox createSuggestedRidesBox() {
        List<Ride> suggestedRides = KnapsackCalculator.recommendBestRides(rideList.getRides(), availableTime);
        VBox suggestedBox = new VBox(15);
        int rowIndex = 0;
        int rideIndex = 0;

        while (rideIndex < suggestedRides.size()) {
            int ridesInThisRow = (rowIndex % 2 == 0) ? 3 : 2;
            HBox rowBox = new HBox(20);
            rowBox.setAlignment(Pos.CENTER);

            for (int i = 0; i < ridesInThisRow && rideIndex < suggestedRides.size(); i++) {
                Ride ride = suggestedRides.get(rideIndex);
                VBox rideEntry = createRideEntry(ride);
                rowBox.getChildren().add(rideEntry);
                rideIndex++;
            }

            if (ridesInThisRow == 2) {
                Region leftSpacer = new Region();
                Region rightSpacer = new Region();
                HBox.setHgrow(leftSpacer, Priority.ALWAYS);
                HBox.setHgrow(rightSpacer, Priority.ALWAYS);
                rowBox.getChildren().add(0, leftSpacer);
                rowBox.getChildren().add(rightSpacer);
            }

            suggestedBox.getChildren().add(rowBox);
            rowIndex++;
        }
        return suggestedBox;
    }

    private VBox createRideEntry(Ride ride) {
        Label nameLabel = new Label(ride.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label detailsLabel = new Label(ride.getEstimatedTime() + " min, Rating: " + ride.getRating());
        detailsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        detailsLabel.setTextFill(Color.WHITE);
        detailsLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        detailsLabel.setTextAlignment(TextAlignment.CENTER);
        detailsLabel.setMaxWidth(Double.MAX_VALUE);

        VBox rideInfo = new VBox(2, nameLabel, detailsLabel);
        rideInfo.setAlignment(Pos.CENTER);
        return rideInfo;
    }

    private HBox createSelectableRideEntry(Ride ride) {
        CheckBox checkBox = new CheckBox();
        boolean isSelected = selectedRides.contains(ride);
        checkBox.setSelected(isSelected);
        int remainingTime = availableTime - totalTime.get();
        if (!isSelected && ride.getEstimatedTime() > remainingTime) {
            checkBox.setDisable(true);
        } else {
            checkBox.setDisable(false);
        }

        Label nameLabel = new Label(ride.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label detailsLabel = new Label(ride.getEstimatedTime() + " min, Rating: " + ride.getRating());
        detailsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        detailsLabel.setTextFill(Color.WHITE);
        detailsLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        detailsLabel.setTextAlignment(TextAlignment.CENTER);
        detailsLabel.setMaxWidth(Double.MAX_VALUE);

        VBox rideInfo = new VBox(2, nameLabel, detailsLabel);
        rideInfo.setAlignment(Pos.CENTER);

        HBox rideEntry = new HBox(10, checkBox, rideInfo);
        rideEntry.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(rideInfo, Priority.ALWAYS);

        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && !oldVal) {
                if (totalTime.get() + ride.getEstimatedTime() > availableTime) {
                    checkBox.setSelected(false);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Time Limit Exceeded");
                    alert.setHeaderText(null);
                    alert.setContentText("Cannot select this ride; it would exceed the time limit.");
                    alert.showAndWait();
                } else {
                    selectedRides.add(ride);
                    totalTime.set(totalTime.get() + ride.getEstimatedTime());
                    updateRidesGrid();
                }
            } else if (!newVal && oldVal) {
                selectedRides.remove(ride);
                totalTime.set(totalTime.get() - ride.getEstimatedTime());
                updateRidesGrid();
            }
            updateTimeDisplay();
        });

        return rideEntry;
    }

    private void updateRidesGrid() {
        ridesGrid.getChildren().clear();
        int col = 0;
        int row = 0;
        for (Ride ride : filteredRides) {
            HBox rideEntry = createSelectableRideEntry(ride);
            ridesGrid.add(rideEntry, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private void sortRides(int sortChoice) {
        SelectionSort sorter = new SelectionSort();
        List<Ride> rideListCopy = new ArrayList<>(allRides);
        sorter.sortRides(rideListCopy, sortChoice);
        allRides.setAll(rideListCopy);
        updateRidesGrid();
    }

    private void updateTimeDisplay() {
        timeLabel.setText("Current Time: " + totalTime.get() + " / " + availableTime + " min");
    }
}