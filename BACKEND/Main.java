package application;

import javafx.application.Application; 
import javafx.stage.Stage; 

public class Main extends Application {
    @Override // Overrides the start method from the superclass (Application class from javaFX)
    public void start(Stage primaryStage) {
        ThemeParkApp app = new ThemeParkApp(primaryStage); 
        primaryStage.setFullScreen(true); 
    }

    public static void main(String[] args) {
        launch(args); 
    }
}
