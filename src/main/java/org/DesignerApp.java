package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controller.ToolboxController;
import org.service.DesignService;

public class DesignerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Рабочая область
        Pane canvas = new Pane();
        canvas.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
        canvas.setPrefSize(800, 600);

        DesignService designService = new DesignService(canvas);

        // Загружаем тулбокс из FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/view/toolbox-view.fxml"));
        VBox toolbox = loader.load();
        ToolboxController controller = loader.getController();
        controller.setDesignService(designService);

        // Панель свойств (заглушка)
        VBox properties = new VBox(10);
        properties.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");
        properties.setPrefWidth(250);
        properties.getChildren().addAll(
                new javafx.scene.control.Label("📐 Свойства")
        );

        // Разделитель
        SplitPane splitPane = new SplitPane(toolbox, canvas, properties);
        splitPane.setDividerPositions(0.15, 0.75);

        Scene scene = new Scene(splitPane, 1200, 800);
        primaryStage.setTitle("Визуальный конструктор");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}