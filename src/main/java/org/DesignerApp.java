package org;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.components.BaseComponent;
import org.components.ComponentFactory;
import org.service.ComponentManager;
import org.ui.PropertiesPanel;

public class DesignerApp extends Application {

    private ComponentManager componentManager;
    private PropertiesPanel propertiesPanel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane canvas = new Pane();
        canvas.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 2px;"
        );
        canvas.setPrefSize(800, 600);

        componentManager = new ComponentManager(canvas);

        // ============================================================
        // 🔥 УСТАНОВКА КОЛБЭКОВ
        // ============================================================
        componentManager.setOnComponentSelected(this::onComponentSelected);
        componentManager.setOnComponentDoubleClick(this::onComponentDoubleClick);
        componentManager.setOnComponentChanged(this::onComponentChanged);

        setupCanvasDragAndDrop(canvas);

        VBox palette = createPalette();
        propertiesPanel = new PropertiesPanel();

        SplitPane mainSplit = new SplitPane(palette, canvas, propertiesPanel);
        mainSplit.setDividerPositions(0.15, 0.65);

        MenuBar menuBar = createMenuBar();

        VBox root = new VBox();
        root.getChildren().addAll(menuBar, mainSplit);
        VBox.setVgrow(mainSplit, Priority.ALWAYS);

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("🎨 Визуальный конструктор - Studio");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("🚀 DesignerApp запущен!");
        ComponentFactory.printRegistry();
    }

    private void onComponentSelected(BaseComponent component) {
        propertiesPanel.showProperties(component);
    }

    private void onComponentDoubleClick(BaseComponent component) {
        propertiesPanel.focusTextField();
    }

    // ================================================================
    // 🔥 СИНХРОНИЗАЦИЯ ПРИ ИЗМЕНЕНИИ РАЗМЕРА/ПОЗИЦИИ
    // ================================================================

    private void onComponentChanged() {
        propertiesPanel.refreshValues();
    }

    private void setupCanvasDragAndDrop(Pane canvas) {
        canvas.setOnDragOver(event -> {
            if (event.getGestureSource() != canvas && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        canvas.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                String type = db.getString();
                try {
                    BaseComponent component = ComponentFactory.create(type);

                    double x = event.getX() - component.getPrefWidth() / 2;
                    double y = event.getY() - component.getPrefHeight() / 2;

                    x = Math.max(0, Math.min(x, canvas.getWidth() - component.getPrefWidth()));
                    y = Math.max(0, Math.min(y, canvas.getHeight() - component.getPrefHeight()));

                    component.setLayoutX(x);
                    component.setLayoutY(y);

                    componentManager.addComponent(component);

                    event.setDropCompleted(true);
                    System.out.println("✅ Добавлен (drag-and-drop): " + type);
                } catch (Exception e) {
                    System.err.println("❌ Ошибка: " + e.getMessage());
                    event.setDropCompleted(false);
                }
            } else {
                event.setDropCompleted(false);
            }
            event.consume();
        });
    }

    private VBox createPalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(10));
        palette.setStyle(
                "-fx-background-color: #e9ecef; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 0 2px 0 0;"
        );
        palette.setPrefWidth(200);

        Label title = new Label("🧩 Палитра");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        palette.getChildren().addAll(title, new Separator());

        for (String type : ComponentFactory.getAvailableTypes()) {
            String displayName = ComponentFactory.getDisplayName(type);

            Label sourceLabel = new Label(displayName);
            sourceLabel.setStyle(
                    "-fx-padding: 8px; " +
                            "-fx-background-color: white; " +
                            "-fx-border-color: #ced4da; " +
                            "-fx-border-radius: 4px; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-size: 13px;"
            );
            sourceLabel.setMaxWidth(Double.MAX_VALUE);
            sourceLabel.setUserData(type);

            sourceLabel.setOnDragDetected(event -> {
                Dragboard db = sourceLabel.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putString((String) sourceLabel.getUserData());
                db.setContent(content);
                event.consume();
            });

            palette.getChildren().add(sourceLabel);
        }

        palette.getChildren().add(new Separator());

        Button clearBtn = new Button("🧹 Очистить холст");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setOnAction(e -> {
            if (componentManager.getComponents().isEmpty()) {
                showAlert("Холст уже пуст");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Подтверждение");
            confirm.setHeaderText("Очистить холст?");
            confirm.setContentText("Все компоненты будут удалены безвозвратно.");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                componentManager.clear();
                propertiesPanel.showProperties(null);
            }
        });
        palette.getChildren().add(clearBtn);

        return palette;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Файл");
        MenuItem saveItem = new MenuItem("💾 Сохранить");
        saveItem.setOnAction(e -> showAlert("Сохранение... (пока заглушка)"));
        MenuItem loadItem = new MenuItem("📂 Загрузить");
        loadItem.setOnAction(e -> showAlert("Загрузка... (пока заглушка)"));
        MenuItem exitItem = new MenuItem("Выход");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(saveItem, loadItem, new SeparatorMenuItem(), exitItem);

        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}