package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.io.File;
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
        StackPane rootStack = new StackPane();
        setupVideoBackground(rootStack);
        
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(rootStack.widthProperty());
        overlay.heightProperty().bind(rootStack.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        rootStack.getChildren().add(overlay);
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(0, 20, 20, 20));
        layout.setAlignment(Pos.TOP_CENTER);

        Text titleLabel = new Text("SELECT YOUR RIDES");
        String plannerFontPath = "C:\\Users\\Josh\\Downloads\\font\\HeadingNowTrial-16BoldItalic.ttf";
        titleLabel.setFont(Font.loadFont(new File(plannerFontPath).toURI().toString(), 135));
        if (titleLabel.getFont() == null) {
            titleLabel.setFont(Font.font("Arial Narrow", 90));
        }
        titleLabel.setFill(Color.web("#ff3131"));
        titleLabel.setStroke(Color.BLACK);
        titleLabel.setStrokeWidth(2);
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 0, 0));

        VBox contentContainer = new VBox(20);
        contentContainer.setPadding(new Insets(20));
        contentContainer.setStyle(
            "-fx-background-color: rgba(255,255,255,0.5); " +
            "-fx-border-color: black; " +
            "-fx-border-width: 4px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: none;"
        );
        contentContainer.setMaxWidth(600);
        contentContainer.setAlignment(Pos.CENTER);

        Label suggestedLabel = new Label("Suggested Rides:");
        suggestedLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        HBox suggestedLabelBox = new HBox(suggestedLabel);
        suggestedLabelBox.setAlignment(Pos.CENTER);

        VBox suggestedBox = createSuggestedRidesBox();
        HBox suggestedBoxWrapper = new HBox(suggestedBox);
        suggestedBoxWrapper.setAlignment(Pos.CENTER);

        VBox suggestedSection = new VBox(5, suggestedLabelBox, suggestedBoxWrapper);
        suggestedSection.setAlignment(Pos.CENTER);

        AnimatedSearchBar searchBar = new AnimatedSearchBar();
        searchBar.getSearchField().textProperty().addListener((obs, oldText, newText) -> {
            filteredRides.setPredicate(ride -> {
                if (newText == null || newText.isEmpty()) return true;
                return ride.getName().toLowerCase().contains(newText.toLowerCase());
            });
            updateRidesGrid();
        });

        Label sortLabel = new Label("Sort by:");
        sortLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black; -fx-font-weight: bold;");

        ToggleButton alphaButton = new ToggleButton("Aa");
        alphaButton.setToggleGroup(sortGroup);
        alphaButton.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #00cc00; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");

        ToggleButton popButton = new ToggleButton("★");
        popButton.setToggleGroup(sortGroup);
        popButton.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #ffcc00; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");

        ToggleButton timeButton = new ToggleButton("🕒");
        timeButton.setToggleGroup(sortGroup);
        timeButton.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #3399ff; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");

        alphaButton.setOnAction(e -> sortRides(1));
        popButton.setOnAction(e -> sortRides(2));
        timeButton.setOnAction(e -> sortRides(3));

        sortGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                if (newToggle == alphaButton) {
                    ((ToggleButton) newToggle).setStyle("-fx-background-color: black; -fx-text-fill: #00cc00; -fx-padding: 5px 10px; -fx-border-radius: 5px;");
                } else if (newToggle == popButton) {
                    ((ToggleButton) newToggle).setStyle("-fx-background-color: black; -fx-text-fill: #ffcc00; -fx-padding: 5px 10px; -fx-border-radius: 5px;");
                } else if (newToggle == timeButton) {
                    ((ToggleButton) newToggle).setStyle("-fx-background-color: black; -fx-text-fill: #3399ff; -fx-padding: 5px 10px; -fx-border-radius: 5px;");
                }
            }
            if (oldToggle != null) {
                if (oldToggle == alphaButton) {
                    ((ToggleButton) oldToggle).setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #00cc00; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");
                } else if (oldToggle == popButton) {
                    ((ToggleButton) oldToggle).setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #ffcc00; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");
                } else if (oldToggle == timeButton) {
                    ((ToggleButton) oldToggle).setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #3399ff; -fx-border-color: black; -fx-border-radius: 5px; -fx-padding: 5px 10px;");
                }
            }
        });

        HBox sortBox = new HBox(10, sortLabel, alphaButton, popButton, timeButton);
        sortBox.setAlignment(Pos.CENTER);

        Region fixedSpacer = new Region();
        fixedSpacer.setPrefWidth(20);

        HBox searchAndSortBox = new HBox(searchBar, fixedSpacer, sortBox);
        searchAndSortBox.setAlignment(Pos.CENTER);

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

        timeLabel = new Label("Current Time: " + totalTime.get() + " / " + availableTime + " min");
        timeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        HBox timeLabelBox = new HBox(timeLabel);
        timeLabelBox.setAlignment(Pos.CENTER);

        Button proceedButton = new Button("Proceed");
        proceedButton.setStyle(
            "-fx-background-color: rgba(255,255,255,0.7); " +
            "-fx-text-fill: black; " +
            "-fx-font-size: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 10px 30px; " +
            "-fx-border-color: black; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
        );
        proceedButton.setEffect(null);
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

        contentContainer.getChildren().addAll(
            suggestedSection, searchAndSortBox,
            ridesGridWrapper, timeLabelBox, proceedButtonBox
        );
        
        layout.getChildren().addAll(titleBox, contentContainer);
        rootStack.getChildren().add(layout);

        Scene scene = new Scene(rootStack, 1000, 700);
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
            
            // Stretch the video to fill the entire background (may distort the image)
            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());
            
            root.getChildren().add(mediaView);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
        } else {
            root.setStyle("-fx-background-color: #121212;");
        }
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
        nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label detailsLabel = new Label(ride.getEstimatedTime() + " min, Rating: " + ride.getRating());
        detailsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        detailsLabel.setTextAlignment(TextAlignment.CENTER);
        detailsLabel.setMaxWidth(Double.MAX_VALUE);

        VBox rideInfo = new VBox(2, nameLabel, detailsLabel);
        rideInfo.setAlignment(Pos.CENTER);
        return rideInfo;
    }

    private HBox createSelectableRideEntry(Ride ride) {
        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-mark-color: black; -fx-box-border: black; -fx-border-width: 2px;");
        
        boolean isSelected = selectedRides.contains(ride);
        checkBox.setSelected(isSelected);
        int remainingTime = availableTime - totalTime.get();
        if (!isSelected && ride.getEstimatedTime() > remainingTime) {
            checkBox.setDisable(true);
        } else {
            checkBox.setDisable(false);
        }

        Label nameLabel = new Label(ride.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black; -fx-font-weight: bold;");
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        Label detailsLabel = new Label(ride.getEstimatedTime() + " min, Rating: " + ride.getRating());
        detailsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
        detailsLabel.setTextAlignment(TextAlignment.CENTER);
        detailsLabel.setMaxWidth(Double.MAX_VALUE);

        VBox rideInfo = new VBox(2, nameLabel, detailsLabel);
        rideInfo.setAlignment(Pos.CENTER);
        rideInfo.setStyle("-fx-background-color: rgba(255,255,255,0.6); -fx-background-radius: 5;");
        rideInfo.setPadding(new Insets(5));

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
