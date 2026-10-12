package com.myindexer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.scene.layout.StackPane;
import logs.LogParser;

public class App extends Application {
    
    private static boolean running = true;
    
    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = FXMLLoader.load(getClass().getResource("/forms/MainForm.fxml"));
        Scene scene = new Scene(root);
        
        stage.setTitle("Indexer Calculator");
        //stage.setResizable(false);
        
        stage.setScene(scene);

        stage.show();
        
        stage.setOnCloseRequest(event -> {
            running = false;
        });
    }

    public static void main(String[] args) {
        new Thread(() -> {
            LogParser.initLogs();
        }).start();

        new Thread(() -> {
            filesWork.FileConnector.initFiles();
        }).start();
        
        launch();
    }

    public static boolean isRunning() {
        return running;
    }
}
