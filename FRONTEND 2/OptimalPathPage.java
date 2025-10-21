package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimalPathPage {
    private Stage stage;
    private List<Ride> selectedRides;
    private List<Integer> bestPath;
    private int minDistance;

    // Coordinates for entrance and rides
    private final double entranceX = 800;  // Positioned right
    private final double entranceY = 650;  // Positioned down
    private final double[][] ridePositions = {
            {700, 600}, // Roller Coaster (near entrance)
            {600, 550}, // Ferris Wheel
            {850, 500}, // Bumper Cars
            {550, 450}, // Haunted House
            {500, 500}, // Water Slide
            {800, 400}, // Swing Ride
            {650, 350}, // Drop Tower
            {900, 300}  // Pirate Ship
    };

    // Icon file paths (update these to your local paths)
    private final String[] rideIconPaths = {
            "file:C:\\Users\\USER\\Downloads\\roller-coaster-icon-.jpg",
            "file:C:\\Users\\USER\\Downloads\\ferris-wheel-icon-.jpg",
            "file:C:\\Users\\USER\\Downloads\\bumper-cars--icon-put-like-3-bumper-cars.jpg",
            "file:C:\\Users\\USER\\Downloads\\haunted-house-icon-make-it-look-creepy.jpg",
            "file:C:\\Users\\USER\\Downloads\\water-slide-icon-make-it-watery.jpg",
            "file:C:\\Users\\USER\\Downloads\\swing-ride-icon.jpg",
            "file:C:\\Users\\USER\\Downloads\\drop-tower-ride-icon-make-it-have-more-seats.jpg",
            "file:C:\\Users\\USER\\Downloads\\pirate-ship-ride-icon.jpg"
    };
    private final String entranceIconPath = "file:C:\\Users\\USER\\Downloads\\theme-park-entrance-icon-make-it-have-more-details.jpg";

    // Ride names
    private final String[] rideNames = {
            "Roller Coaster",
            "Ferris Wheel",
            "Bumper Cars",
            "Haunted House",
            "Water Slide",
            "Swing Ride",
            "Drop Tower",
            "Pirate Ship"
    };

    // Arrow appearance constants
    private final double arrowGap = 15; // Gap between arrow end and ride icon
    private final double arrowheadLength = 10; // Length of arrowhead
    private final double arrowheadWidth = 5; // Half-width of arrowhead base
    private final List<Color> arrowColors = Arrays.asList(
            Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.PURPLE, Color.CYAN, Color.MAGENTA
    );

    public OptimalPathPage(Stage stage, List<Ride> selectedRides) {
        this.stage = stage;
        this.selectedRides = selectedRides;

        if (!selectedRides.isEmpty()) {
            List<Integer> rideCodes = new ArrayList<>();
            for (Ride ride : selectedRides) {
                rideCodes.add(ride.getCode());
            }

            DistanceMatrix dm = new DistanceMatrix();
            int[][] distanceMatrix = dm.getMatrix();

            OptimalPathCalculator calculator = new OptimalPathCalculator(rideCodes, distanceMatrix);
            calculator.findOptimalPath();
            this.bestPath = calculator.getBestPath();
            this.minDistance = calculator.getMinDistance();
        } else {
            this.bestPath = new ArrayList<>();
            this.minDistance = 0;
        }

        showOptimalPath();
    }

    private void showOptimalPath() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #121212;");

        // Video background
        String absolutePath = "C:\\Users\\USER\\Downloads\\mylivewallpapers-com-Cute-City.mp4";
        File videoFile = new File(absolutePath);
        if (videoFile.exists()) {
            try {
                Media media = new Media(videoFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnError(() -> System.err.println("Media error: " + mediaPlayer.getError()));
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setPreserveRatio(true);
                mediaView.fitWidthProperty().bind(root.widthProperty());
                mediaView.fitHeightProperty().bind(root.heightProperty());
                mediaView.setEffect(new BoxBlur(5, 5, 3));
                root.getChildren().add(mediaView);
                mediaPlayer.play();
            } catch (Exception e) {
                System.err.println("Video load error: " + e.getMessage());
                root.setStyle("-fx-background-color: #121212;");
            }
        } else {
            System.err.println("Video not found: " + absolutePath);
        }

        // Overlay
        Rectangle overlay = new Rectangle();
        overlay.widthProperty().bind(root.widthProperty());
        overlay.heightProperty().bind(root.heightProperty());
        overlay.setFill(Color.color(0, 0, 0, 0.2));
        root.getChildren().add(overlay);

        VBox leftBox = createOptimalRidesText();
        BorderPane.setAlignment(leftBox, Pos.CENTER_LEFT);
        BorderPane.setMargin(leftBox, new Insets(0, 0, 0, 70));
        root.setLeft(leftBox);

        AnchorPane mapPane = createThemeParkMap();
        root.setCenter(mapPane);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("Optimal Ride Path");
        stage.show();
    }

    private VBox createOptimalRidesText() {
        VBox leftBox = new VBox(5);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15); " +
                        "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                        "-fx-border-width: 4px; -fx-border-radius: 20px; " +
                        "-fx-effect: dropshadow(gaussian, #ff99cc, 10, 0, 0, 0);"
        );
        leftBox.setPadding(new Insets(10));

        Label titleLabel = new Label("🎯 Optimal Ride Path");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
        titleLabel.setEffect(new DropShadow(10, Color.web("#ff99cc")));

        VBox rideListBox = new VBox(3);
        rideListBox.setAlignment(Pos.CENTER);

        if (selectedRides.isEmpty()) {
            Label noRidesLabel = new Label("❌ No rides selected.");
            noRidesLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            noRidesLabel.setTextFill(Color.WHITE);
            noRidesLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
            rideListBox.getChildren().add(noRidesLabel);

            Button proceedButton = createProceedButton();
            rideListBox.getChildren().add(proceedButton);
            VBox.setMargin(proceedButton, new Insets(5, 0, 0, 0));
        } else {
            Label startLabel = new Label("1. Entrance");
            startLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            startLabel.setTextFill(Color.WHITE);
            startLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
            rideListBox.getChildren().add(startLabel);

            for (int i = 0; i < bestPath.size(); i++) {
                int rideCode = bestPath.get(i);
                Ride ride = getRideByCode(rideCode);
                Label rideLabel = new Label((i + 2) + ". " + ride.getName());
                rideLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
                rideLabel.setTextFill(Color.WHITE);
                rideLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
                rideListBox.getChildren().add(rideLabel);
            }

            Label endLabel = new Label((bestPath.size() + 2) + ". Entrance");
            endLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            endLabel.setTextFill(Color.WHITE);
            endLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
            rideListBox.getChildren().add(endLabel);

            Label distanceLabel = new Label("Total Distance: " + minDistance + " meters");
            distanceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            distanceLabel.setTextFill(Color.WHITE);
            distanceLabel.setStyle("-fx-stroke: #000000; -fx-stroke-width: 1;");
            rideListBox.getChildren().add(distanceLabel);

            Button proceedButton = createProceedButton();
            rideListBox.getChildren().add(proceedButton);
            VBox.setMargin(proceedButton, new Insets(5, 0, 0, 0));
        }

        leftBox.getChildren().addAll(titleLabel, rideListBox);
        return leftBox;
    }

    private Button createProceedButton() {
        Button button = new Button("Proceed");
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.BLACK);
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                        "-fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 15px; " +
                        "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                        "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
        );
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ffcc66, #ff66cc); " +
                            "-fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 15px; " +
                            "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                            "-fx-effect: dropshadow(gaussian, #ffccff, 12, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                            "-fx-border-color: #ffffff; -fx-border-width: 2px; -fx-border-radius: 15px; " +
                            "-fx-background-radius: 15px; -fx-padding: 10px 20px; " +
                            "-fx-effect: dropshadow(gaussian, #ff99cc, 8, 0, 0, 0);"
            );
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        button.setOnAction(e -> new ThankYouPage(stage));
        return button;
    }

    private AnchorPane createThemeParkMap() {
        AnchorPane mapPane = new AnchorPane();
        mapPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");

        Rectangle parkArea = new Rectangle(350, 200, 750, 550);
        String parkBackgroundPath = "C:\\Users\\USER\\Pictures\\background ng map.jpg";
        File parkBackgroundFile = new File(parkBackgroundPath);
        if (parkBackgroundFile.exists()) {
            try {
                Image parkImage = new Image(parkBackgroundFile.toURI().toString());
                parkArea.setFill(new ImagePattern(parkImage, 0, 0, 1, 1, true));
            } catch (Exception e) {
                System.err.println("Park image error: " + e.getMessage());
                parkArea.setFill(Color.WHITE);
            }
        } else {
            parkArea.setFill(Color.WHITE);
        }
        parkArea.setStyle(
                "-fx-border-color: linear-gradient(to bottom, #ff66cc, #ffcc66); " +
                        "-fx-border-width: 10px; -fx-border-radius: 20px; " +
                        "-fx-effect: dropshadow(gaussian, #ff99cc, 25, 0, 0, 0);"
        );
        DropShadow dropShadow = new DropShadow(25, Color.web("#ff99cc"));
        InnerShadow innerShadow = new InnerShadow(10, Color.web("#ffccff"));
        dropShadow.setInput(innerShadow);
        parkArea.setEffect(dropShadow);
        mapPane.getChildren().add(parkArea);

        ImageView entranceIcon = createIcon(entranceIconPath, entranceX, entranceY);
        mapPane.getChildren().add(entranceIcon);
        Label entranceLabel = new Label("Entrance");
        entranceLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #000000;");
        AnchorPane.setLeftAnchor(entranceLabel, entranceX - 25);
        AnchorPane.setTopAnchor(entranceLabel, entranceY + 30);
        mapPane.getChildren().add(entranceLabel);

        List<ImageView> rideIcons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            double x = ridePositions[i][0];
            double y = ridePositions[i][1];
            ImageView rideIcon = createIcon(rideIconPaths[i], x, y);
            mapPane.getChildren().add(rideIcon);
            rideIcons.add(rideIcon);
            Label rideLabel = new Label(rideNames[i]);
            rideLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #000000;");
            AnchorPane.setLeftAnchor(rideLabel, x - 25);
            AnchorPane.setTopAnchor(rideLabel, y + 30);
            mapPane.getChildren().add(rideLabel);
        }

        if (!selectedRides.isEmpty()) {
            List<double[]> pathPoints = new ArrayList<>();
            pathPoints.add(new double[]{entranceX, entranceY});
            for (int code : bestPath) {
                int rideIdx = code - 1;
                pathPoints.add(new double[]{ridePositions[rideIdx][0], ridePositions[rideIdx][1]});
            }
            pathPoints.add(new double[]{entranceX, entranceY});
            animatePath(mapPane, pathPoints, entranceIcon, rideIcons);
        }

        return mapPane;
    }

    private ImageView createIcon(String path, double x, double y) {
        Image image = new Image(path);
        ImageView icon = new ImageView(image);
        icon.setFitWidth(50);
        icon.setFitHeight(50);
        AnchorPane.setLeftAnchor(icon, x - 25);
        AnchorPane.setTopAnchor(icon, y - 25);
        return icon;
    }

    private void animatePath(AnchorPane mapPane, List<double[]> pathPoints, ImageView entranceIcon, List<ImageView> rideIcons) {
        SequentialTransition seqTransition = new SequentialTransition();
        seqTransition.getChildren().add(animateRide(entranceIcon));

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            double startX = pathPoints.get(i)[0];
            double startY = pathPoints.get(i)[1];
            double endX = pathPoints.get(i + 1)[0];
            double endY = pathPoints.get(i + 1)[1];

            double[] B = calculateArrowEnd(startX, startY, endX, endY);
            double Bx = B[0];
            double By = B[1];

            double dx = endX - startX;
            double dy = endY - startY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance == 0) continue;
            double Ux = dx / distance;
            double Uy = dy / distance;
            double Wx = -Uy;
            double Wy = Ux;

            double baseLeftX = Bx - arrowheadLength * Ux + arrowheadWidth * Wx;
            double baseLeftY = By - arrowheadLength * Uy + arrowheadWidth * Wy;
            double baseRightX = Bx - arrowheadLength * Ux - arrowheadWidth * Wx;
            double baseRightY = By - arrowheadLength * Uy - arrowheadWidth * Wy;

            Color arrowColor = arrowColors.get(i % arrowColors.size());

            Line shaft = new Line(startX, startY, startX, startY);
            shaft.setStroke(arrowColor);
            shaft.setStrokeWidth(3);
            mapPane.getChildren().add(shaft);

            Polygon arrowhead = new Polygon();
            arrowhead.getPoints().addAll(Bx, By, baseLeftX, baseLeftY, baseRightX, baseRightY);
            arrowhead.setFill(arrowColor);
            arrowhead.setVisible(false);
            mapPane.getChildren().add(arrowhead);

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(shaft.endXProperty(), startX), new KeyValue(shaft.endYProperty(), startY)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(shaft.endXProperty(), Bx), new KeyValue(shaft.endYProperty(), By))
            );
            timeline.setOnFinished(e -> arrowhead.setVisible(true));
            seqTransition.getChildren().add(timeline);

            if (i < bestPath.size()) {
                int rideCode = bestPath.get(i);
                ImageView icon = rideIcons.get(rideCode - 1);
                seqTransition.getChildren().add(animateRide(icon));
            }
        }

        seqTransition.play();
    }

    private ParallelTransition animateRide(ImageView icon) {
        DropShadow rideGlow = new DropShadow();
        rideGlow.setRadius(0);
        icon.setEffect(rideGlow);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(1), icon);
        scale.setFromX(1);
        scale.setFromY(1);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setAutoReverse(true);
        scale.setCycleCount(1);

        Timeline glowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(rideGlow.radiusProperty(), 0), new KeyValue(rideGlow.colorProperty(), Color.RED)),
                new KeyFrame(Duration.seconds(0.25), new KeyValue(rideGlow.radiusProperty(), 7.5), new KeyValue(rideGlow.colorProperty(), Color.GREEN)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(rideGlow.radiusProperty(), 15), new KeyValue(rideGlow.colorProperty(), Color.BLUE)),
                new KeyFrame(Duration.seconds(0.75), new KeyValue(rideGlow.radiusProperty(), 7.5), new KeyValue(rideGlow.colorProperty(), Color.YELLOW)),
                new KeyFrame(Duration.seconds(1), new KeyValue(rideGlow.radiusProperty(), 5), new KeyValue(rideGlow.colorProperty(), Color.LIGHTBLUE))
        );

        return new ParallelTransition(scale, glowTimeline);
    }

    private double[] calculateArrowEnd(double startX, double startY, double endX, double endY) {
        double dx = endX - startX;
        double dy = endY - startY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return new double[]{endX, endY};
        double ratio = (distance - arrowGap) / distance;
        if (ratio < 0) ratio = 0;
        double Bx = startX + ratio * dx;
        double By = startY + ratio * dy;
        return new double[]{Bx, By};
    }

    private Ride getRideByCode(int code) {
        for (Ride ride : selectedRides) {
            if (ride.getCode() == code) return ride;
        }
        throw new IllegalStateException("Ride code " + code + " not found");
    }
}