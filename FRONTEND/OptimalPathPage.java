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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OptimalPathPage {
   private Stage stage;
   private List<Ride> selectedRides;
   private List<Integer> bestPath;
   private int minDistance;
   private final double entranceX = 700;
   private final double entranceY = 650;
   private final double[][] ridePositions = {
       {600, 600}, {500, 550}, {750, 500}, {465, 450},
       {400, 500}, {700, 400}, {550, 350}, {750, 300}
   };
   private final String[] rideIconPaths = {
       "file:C:\\Users\\Josh\\Downloads\\roller-coaster-icon-.jpg",
       "file:C:\\Users\\Josh\\Downloads\\ferris-wheel-icon-.jpg",
       "file:C:\\Users\\Josh\\Downloads\\bumper-cars--icon-put-like-3-bumper-cars.jpg",
       "file:C:\\Users\\Josh\\Downloads\\haunted-house-icon-make-it-look-creepy.jpg",
       "file:C:\\Users\\Josh\\Downloads\\water-slide-icon-make-it-watery.jpg",
       "file:C:\\Users\\Josh\\Downloads\\swing-ride-icon.jpg",
       "file:C:\\Users\\Josh\\Downloads\\drop-tower-ride-icon-make-it-have-more-seats.jpg",
       "file:C:\\Users\\Josh\\Downloads\\pirate-ship-ride-icon.jpg"
   };
   private final String entranceIconPath = "file:C:\\Users\\Josh\\Downloads\\theme-park-entrance-icon-make-it-have-more-details.jpg";
   private final String[] rideNames = {
       "Roller Coaster", "Ferris Wheel", "Bumper Cars", "Haunted House",
       "Water Slide", "Swing Ride", "Drop Tower", "Pirate Ship"
   };
   
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
	    StackPane root = new StackPane();
	    setupVideoBackground(root);
	    
	    Rectangle overlay = new Rectangle();
	    overlay.widthProperty().bind(root.widthProperty());
	    overlay.heightProperty().bind(root.heightProperty());
	    overlay.setFill(Color.color(0, 0, 0, 0.2));
	    root.getChildren().add(overlay);
	    
	    BorderPane contentPane = new BorderPane();
	    contentPane.setStyle("-fx-background-color: transparent;");
	    contentPane.setPadding(new Insets(0, 0, 0, 250));
	    
	    VBox leftBox = createOptimalRidesText();
	    contentPane.setLeft(leftBox);
	    
	    AnchorPane mapPane = createThemeParkMap();
	    contentPane.setCenter(mapPane);
	    BorderPane.setMargin(mapPane, new Insets(0, 100, 0, -150));
	    
	    root.getChildren().add(contentPane);
	    
	    Scene scene = new Scene(root, 900, 600);
	    stage.setScene(scene);
	    
	    // Set the fullscreen exit hint to empty string to hide it
	    stage.setFullScreenExitHint("");
	    stage.setFullScreen(true);
	    stage.setTitle("Optimal Ride Path");
	    stage.show();
	}
   
   private void setupVideoBackground(StackPane root) {
       if (MediaCache.BACKGROUND_MEDIA != null) {
           MediaPlayer mediaPlayer = new MediaPlayer(MediaCache.BACKGROUND_MEDIA);
           mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
           MediaView mediaView = new MediaView(mediaPlayer);
           
           mediaView.setPreserveRatio(false);
           mediaView.fitWidthProperty().bind(root.widthProperty());
           mediaView.fitHeightProperty().bind(root.heightProperty());
           
           root.getChildren().add(mediaView);
           mediaPlayer.setOnReady(() -> mediaPlayer.play());
       } else {
           root.setStyle("-fx-background-color: #121212;");
       }
   }

   private VBox createOptimalRidesText() {
       VBox leftBox = new VBox(15);
       leftBox.setAlignment(Pos.CENTER_LEFT);
       leftBox.setStyle("-fx-padding: 20px;");
       
       String plannerFontPath = "C:\\Users\\Josh\\Downloads\\font\\HeadingNowTrial-16BoldItalic.ttf";
       Font plannerFont = Font.loadFont(new File(plannerFontPath).toURI().toString(), 120);
       Text titleText = new Text("Optimal Ride Path");
       if (plannerFont != null) {
           titleText.setFont(plannerFont);
       } else {
           titleText.setFont(Font.font("Arial Narrow", FontWeight.BOLD, FontPosture.ITALIC, 60));
       }
       titleText.setFill(Color.web("#ff3131"));
       titleText.setStroke(Color.BLACK);
       titleText.setStrokeWidth(2);
       DropShadow shadow = new DropShadow();
       shadow.setRadius(10);
       shadow.setColor(Color.BLACK);
       titleText.setEffect(shadow);
       
       VBox rideListBox = new VBox(8);
       rideListBox.setAlignment(Pos.CENTER_LEFT);
       rideListBox.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-padding: 15px; -fx-background-radius: 10;");
       
       if (selectedRides.isEmpty()) {
           Label noRidesLabel = new Label("❌ No rides selected.");
           noRidesLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
           rideListBox.getChildren().add(noRidesLabel);
       } else {
           Label startLabel = new Label("1. Entrance");
           startLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
           rideListBox.getChildren().add(startLabel);
           for (int i = 0; i < bestPath.size(); i++) {
               int rideCode = bestPath.get(i);
               Ride ride = getRideByCode(rideCode);
               Label rideLabel = new Label((i + 2) + ". " + ride.getName());
               rideLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
               rideListBox.getChildren().add(rideLabel);
           }
           Label endLabel = new Label((bestPath.size() + 2) + ". Entrance");
           endLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px;");
           rideListBox.getChildren().add(endLabel);
           Label distanceLabel = new Label("Total Distance: " + minDistance + " meters");
           distanceLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-font-weight: bold;");
           rideListBox.getChildren().add(distanceLabel);
           
           Button proceedButton = new Button("Proceed");
           proceedButton.setStyle(
               "-fx-background-color: rgba(76, 175, 80, 0.8); " +
               "-fx-text-fill: white; " +
               "-fx-font-size: 14px; " +
               "-fx-font-weight: bold; " +
               "-fx-border-color: white; " +
               "-fx-border-width: 1px; " +
               "-fx-border-radius: 5px; " +
               "-fx-background-radius: 5px;"
           );
           proceedButton.setOnMouseEntered(e -> {
               proceedButton.setStyle(
                   "-fx-background-color: rgba(76, 175, 80, 1.0); " +
                   "-fx-text-fill: white; " +
                   "-fx-font-size: 14px; " +
                   "-fx-font-weight: bold; " +
                   "-fx-border-color: white; " +
                   "-fx-border-width: 1px; " +
                   "-fx-border-radius: 5px; " +
                   "-fx-background-radius: 5px;"
               );
           });
           proceedButton.setOnMouseExited(e -> {
               proceedButton.setStyle(
                   "-fx-background-color: rgba(76, 175, 80, 0.8); " +
                   "-fx-text-fill: white; " +
                   "-fx-font-size: 14px; " +
                   "-fx-font-weight: bold; " +
                   "-fx-border-color: white; " +
                   "-fx-border-width: 1px; " +
                   "-fx-border-radius: 5px; " +
                   "-fx-background-radius: 5px;"
               );
           });
           proceedButton.setOnAction(e -> new ThankYouPage(stage));
           
           VBox buttonContainer = new VBox(proceedButton);
           buttonContainer.setAlignment(Pos.CENTER);
           rideListBox.getChildren().add(buttonContainer);
           VBox.setMargin(buttonContainer, new Insets(10, 0, 0, 0));
       }
       
       leftBox.getChildren().addAll(titleText, rideListBox);
       return leftBox;
   }

   private AnchorPane createThemeParkMap() {
	    AnchorPane mapPane = new AnchorPane();
	    mapPane.setStyle("-fx-background-color: transparent;");
	    
	    // Create a rectangle for the park area
	    Rectangle parkArea = new Rectangle(250, 200, 750, 550);
	    
	    // Load the background image instead of using a solid fill
	    try {
	        Image bgImage = new Image("file:C:\\Users\\Josh\\Downloads\\tpbg.jpg");
	        parkArea.setFill(new javafx.scene.paint.ImagePattern(bgImage));
	    } catch (Exception e) {
	        // Fallback to white background if image loading fails
	        parkArea.setFill(Color.rgb(255, 255, 255, 0.7));
	        System.err.println("Failed to load background image: " + e.getMessage());
	    }
	    
	    parkArea.setStroke(Color.BLACK);
	    parkArea.setArcWidth(20);
	    parkArea.setArcHeight(20);
	    mapPane.getChildren().add(parkArea);
	    
	    ImageView entranceIcon = createIcon(entranceIconPath, entranceX, entranceY);
	    mapPane.getChildren().add(entranceIcon);
	    Label entranceLabel = createLabel("Entrance", entranceX, entranceY + 30);
	    entranceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-background-color: rgba(255,255,255,0.7); -fx-padding: 2px 5px; -fx-background-radius: 3px;");
	    mapPane.getChildren().add(entranceLabel);
	    
	    List<ImageView> rideIcons = new ArrayList<>();
	    for (int i = 0; i < 8; i++) {
	        double x = ridePositions[i][0];
	        double y = ridePositions[i][1];
	        String path = rideIconPaths[i];
	        ImageView rideIcon = createIcon(path, x, y);
	        mapPane.getChildren().add(rideIcon);
	        rideIcons.add(rideIcon);
	        Label rideLabel = createLabel(rideNames[i], x, y + 30);
	        rideLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-font-weight: bold; -fx-background-color: rgba(255,255,255,0.7); -fx-padding: 2px 5px; -fx-background-radius: 3px;");
	        mapPane.getChildren().add(rideLabel);
	    }
	    
	    if (!selectedRides.isEmpty()) {
	        List<double[]> pathPoints = new ArrayList<>();
	        pathPoints.add(new double[]{entranceX, entranceY});
	        for (int code : bestPath) {
	            int rideIdx = code - 1;
	            double x = ridePositions[rideIdx][0];
	            double y = ridePositions[rideIdx][1];
	            pathPoints.add(new double[]{x, y});
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
       
       DropShadow dropShadow = new DropShadow();
       dropShadow.setRadius(5.0);
       dropShadow.setOffsetX(3.0);
       dropShadow.setOffsetY(3.0);
       dropShadow.setColor(Color.BLACK);
       icon.setEffect(dropShadow);
       
       return icon;
   }

   private Label createLabel(String text, double x, double y) {
       Label label = new Label(text);
       label.setStyle("-fx-font-size: 12px; -fx-text-fill: #000000; -fx-font-weight: bold;");
       AnchorPane.setLeftAnchor(label, x - 35);
       AnchorPane.setTopAnchor(label, y);
       return label;
   }

   private void animatePath(AnchorPane mapPane, List<double[]> pathPoints, ImageView entranceIcon, List<ImageView> rideIcons) {
	    SequentialTransition seqTransition = new SequentialTransition();
	    
	    // Entrance animation
	    DropShadow entranceGlow = new DropShadow();
	    entranceGlow.setRadius(0);
	    entranceIcon.setEffect(entranceGlow);
	    
	    ScaleTransition scaleEntrance = new ScaleTransition(Duration.seconds(1), entranceIcon);
	    scaleEntrance.setFromX(1);
	    scaleEntrance.setFromY(1);
	    scaleEntrance.setToX(1.5);
	    scaleEntrance.setToY(1.5);
	    scaleEntrance.setAutoReverse(true);
	    scaleEntrance.setCycleCount(1);
	    
	    Timeline entranceGlowTimeline = new Timeline(
	        new KeyFrame(Duration.ZERO, new KeyValue(entranceGlow.radiusProperty(), 0), new KeyValue(entranceGlow.colorProperty(), Color.RED)),
	        new KeyFrame(Duration.seconds(0.25), new KeyValue(entranceGlow.radiusProperty(), 7.5), new KeyValue(entranceGlow.colorProperty(), Color.GREEN)),
	        new KeyFrame(Duration.seconds(0.5), new KeyValue(entranceGlow.radiusProperty(), 15), new KeyValue(entranceGlow.colorProperty(), Color.BLUE)),
	        new KeyFrame(Duration.seconds(0.75), new KeyValue(entranceGlow.radiusProperty(), 7.5), new KeyValue(entranceGlow.colorProperty(), Color.YELLOW)),
	        new KeyFrame(Duration.seconds(1), new KeyValue(entranceGlow.radiusProperty(), 5), new KeyValue(entranceGlow.colorProperty(), Color.LIGHTBLUE))
	    );
	    
	    ParallelTransition entranceParallel = new ParallelTransition(scaleEntrance, entranceGlowTimeline);
	    seqTransition.getChildren().add(entranceParallel);
	    
	    // Path animation with arrows
	    for (int i = 0; i < pathPoints.size() - 1; i++) {
	        double startX = pathPoints.get(i)[0];
	        double startY = pathPoints.get(i)[1];
	        double endX = pathPoints.get(i + 1)[0];
	        double endY = pathPoints.get(i + 1)[1];
	        
	        // Calculate the direction vector and make it the right size for the arrowhead
	        double arrowSize = 15;
	        double dx = endX - startX;
	        double dy = endY - startY;
	        double length = Math.sqrt(dx * dx + dy * dy);
	        double unitDx = dx / length;
	        double unitDy = dy / length;
	        
	        // Create the main line
	        Line mainLine = new Line(startX, startY, startX, startY); // Will animate to endX, endY
	        mainLine.setStroke(Color.RED);
	        mainLine.setStrokeWidth(4);
	        mainLine.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
	        
	        // Create the arrowhead (triangle)
	        javafx.scene.shape.Polygon arrowHead = new javafx.scene.shape.Polygon();
	        
	        // Calculate arrow points (relative to the arrow's tip)
	        // The tip will be at (0,0) and we calculate the other two points based on the angle
	        double angle = Math.atan2(dy, dx);
	        double backAngle1 = angle + Math.toRadians(150);  // Angle for one side
	        double backAngle2 = angle - Math.toRadians(150);  // Angle for the other side
	        
	        arrowHead.getPoints().addAll(
	            0.0, 0.0,  // Tip of the arrow
	            arrowSize * Math.cos(backAngle1), arrowSize * Math.sin(backAngle1),  // First back point
	            arrowSize * Math.cos(backAngle2), arrowSize * Math.sin(backAngle2)   // Second back point
	        );
	        
	        arrowHead.setFill(Color.RED);
	        
	        // Position the arrowhead at the start (it will be moved to the end)
	        arrowHead.setLayoutX(startX);
	        arrowHead.setLayoutY(startY);
	        
	        // Create a group to hold the arrow components
	        javafx.scene.Group arrow = new javafx.scene.Group(mainLine, arrowHead);
	        
	        // Add shadow to the arrow
	        DropShadow pathShadow = new DropShadow();
	        pathShadow.setRadius(3.0);
	        pathShadow.setColor(Color.BLACK);
	        arrow.setEffect(pathShadow);
	        
	        mapPane.getChildren().add(arrow);
	        
	        // Create the animation for the line
	        Timeline lineAnimation = new Timeline(
	            new KeyFrame(Duration.ZERO, 
	                new KeyValue(mainLine.endXProperty(), startX),
	                new KeyValue(mainLine.endYProperty(), startY)),
	            new KeyFrame(Duration.seconds(1), 
	                new KeyValue(mainLine.endXProperty(), endX),
	                new KeyValue(mainLine.endYProperty(), endY))
	        );
	        
	        // Create the animation for the arrowhead
	        Timeline arrowheadAnimation = new Timeline(
	            new KeyFrame(Duration.ZERO, 
	                new KeyValue(arrowHead.layoutXProperty(), startX),
	                new KeyValue(arrowHead.layoutYProperty(), startY)),
	            new KeyFrame(Duration.seconds(1), 
	                new KeyValue(arrowHead.layoutXProperty(), endX),
	                new KeyValue(arrowHead.layoutYProperty(), endY))
	        );
	        
	        // Combine animations
	        ParallelTransition arrowAnimation = new ParallelTransition(lineAnimation, arrowheadAnimation);
	        seqTransition.getChildren().add(arrowAnimation);
	        
	        // Ride icon animation
	        if (i < bestPath.size()) {
	            int rideCode = bestPath.get(i);
	            ImageView icon = rideIcons.get(rideCode - 1);
	            
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
	            
	            Timeline rideGlowTimeline = new Timeline(
	                new KeyFrame(Duration.ZERO, new KeyValue(rideGlow.radiusProperty(), 0), new KeyValue(rideGlow.colorProperty(), Color.RED)),
	                new KeyFrame(Duration.seconds(0.25), new KeyValue(rideGlow.radiusProperty(), 7.5), new KeyValue(rideGlow.colorProperty(), Color.GREEN)),
	                new KeyFrame(Duration.seconds(0.5), new KeyValue(rideGlow.radiusProperty(), 15), new KeyValue(rideGlow.colorProperty(), Color.BLUE)),
	                new KeyFrame(Duration.seconds(0.75), new KeyValue(rideGlow.radiusProperty(), 7.5), new KeyValue(rideGlow.colorProperty(), Color.YELLOW)),
	                new KeyFrame(Duration.seconds(1), new KeyValue(rideGlow.radiusProperty(), 5), new KeyValue(rideGlow.colorProperty(), Color.LIGHTBLUE))
	            );
	            
	            ParallelTransition rideParallel = new ParallelTransition(scale, rideGlowTimeline);
	            seqTransition.getChildren().add(rideParallel);
	        }
	    }
	    
	    seqTransition.play();
	}

   private Ride getRideByCode(int code) {
       for (Ride ride : selectedRides) {
           if (ride.getCode() == code) {
               return ride;
           }
       }
       throw new IllegalStateException("Ride code " + code + " not found in selectedRides");
   }
}
