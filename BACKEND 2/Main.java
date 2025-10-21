package application;

import javafx.application.Application; // superclass for javaFX	Applications
import javafx.stage.Stage; // represents the window for the javaFX application

public class Main extends Application {
    @Override // Overrides the start method from the superclass (Application class from javaFX)
    public void start(Stage primaryStage) {
        ThemeParkApp app = new ThemeParkApp(primaryStage); // Creates a ThemeParkApp object 
        primaryStage.setFullScreen(true); // Makes the window full screen
    }

    public static void main(String[] args) {
        launch(args); //Initializes javaFX (Basically just calling the javaFX)
    }
}