package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем FXML из resources
        URL fxmlLocation = getClass().getResource("/org/view/main-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);

        BorderPane root = loader.load(); // Загружаем разметку

        Scene scene = new Scene(root, 800, 600);

        // (Опционально) Подключаем CSS позже для красоты
        // scene.getStylesheets().add(getClass().getResource("/com/myapp/dashboard/css/style.css").toExternalForm());

        primaryStage.setTitle("Мой супер бэкенд-фронтенд");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Традиционный main для запуска
    public static void main(String[] args) {
        launch(args);
    }
}