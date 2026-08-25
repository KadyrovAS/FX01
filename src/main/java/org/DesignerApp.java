package org;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.components.BaseComponent;
import org.components.ComponentFactory;
import org.service.ComponentManager;

public class DesignerApp extends Application {

    private ComponentManager componentManager;

    @Override
    public void start(Stage primaryStage) {
        // Холст (рабочая область)
        Pane canvas = new Pane();
        canvas.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 2px;");
        canvas.setPrefSize(800, 600);

        componentManager = new ComponentManager(canvas);

        // Левая панель - палитра компонентов
        VBox palette = createPalette();

        // Правая панель - свойства
        VBox propertiesPanel = createPropertiesPanel();

        // Разделитель
        SplitPane mainSplit = new SplitPane(palette, canvas, propertiesPanel);
        mainSplit.setDividerPositions(0.15, 0.70);

        // Меню
        MenuBar menuBar = createMenuBar();

        // Корневой контейнер
        VBox root = new VBox();
        root.getChildren().addAll(menuBar, mainSplit);
        VBox.setVgrow(mainSplit, Priority.ALWAYS);

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("🎨 Визуальный конструктор - Studio");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("🚀 Приложение запущено!");
        System.out.println("📦 Доступные компоненты: " + ComponentFactory.getAvailableTypes());
    }

    private VBox createPalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(10));
        palette.setStyle("-fx-background-color: #e9ecef;");
        palette.setPrefWidth(180);

        Label title = new Label("🧩 Палитра");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        palette.getChildren().add(title);
        palette.getChildren().add(new Separator());

        // Создаем кнопки для каждого типа компонента
        for (String type : ComponentFactory.getAvailableTypes()) {
            Button btn = new Button(translateType(type));
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-cursor: hand;");
            btn.setOnAction(e -> {
                BaseComponent component = ComponentFactory.create(type);
                component.setLayoutX(50 + Math.random() * 100);
                component.setLayoutY(50 + Math.random() * 100);
                componentManager.addComponent(component);
            });
            palette.getChildren().add(btn);
        }

        palette.getChildren().add(new Separator());

        // Кнопка удаления
        Button deleteBtn = new Button("🗑 Удалить выбранное");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            BaseComponent selected = componentManager.getSelectedComponent();
            if (selected != null) {
                componentManager.removeComponent(selected);
            } else {
                showAlert("Выберите компонент для удаления");
            }
        });
        palette.getChildren().add(deleteBtn);

        // Кнопка очистки
        Button clearBtn = new Button("🧹 Очистить холст");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            componentManager.clear();
        });
        palette.getChildren().add(clearBtn);

        return palette;
    }

    private String translateType(String type) {
        return switch (type) {
            case "Button" -> "🔘 Кнопка";
            case "Label" -> "📝 Надпись";
            case "TextField" -> "📥 Поле ввода";
            case "Chart" -> "📊 График";
            default -> type;
        };
    }

    private VBox createPropertiesPanel() {
        VBox properties = new VBox(10);
        properties.setPadding(new Insets(10));
        properties.setStyle("-fx-background-color: #f8f9fa;");
        properties.setPrefWidth(250);

        Label title = new Label("📐 Свойства");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label info = new Label("Выберите компонент\nна холсте");
        info.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");

        properties.getChildren().addAll(title, new Separator(), info);

        // TODO: Реализовать панель свойств с привязкой к выбранному компоненту

        return properties;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Меню "Файл"
        Menu fileMenu = new Menu("Файл");
        MenuItem saveItem = new MenuItem("💾 Сохранить проект");
        saveItem.setOnAction(e -> {
            try {
                // TODO: Сохранение в JSON
                showAlert("Проект сохранен в design.json");
            } catch (Exception ex) {
                showAlert("Ошибка сохранения: " + ex.getMessage());
            }
        });

        MenuItem loadItem = new MenuItem("📂 Загрузить проект");
        loadItem.setOnAction(e -> {
            // TODO: Загрузка из JSON
            showAlert("Загрузка проекта...");
        });

        MenuItem exportItem = new MenuItem("🚀 Экспортировать FXML");
        exportItem.setOnAction(e -> {
            // TODO: Экспорт в FXML
            showAlert("Экспорт в FXML...");
        });

        MenuItem exitItem = new MenuItem("Выход");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(saveItem, loadItem, exportItem, new SeparatorMenuItem(), exitItem);

        // Меню "Правка"
        Menu editMenu = new Menu("Правка");
        MenuItem deleteItem = new MenuItem("Удалить выбранное");
        deleteItem.setOnAction(e -> {
            BaseComponent selected = componentManager.getSelectedComponent();
            if (selected != null) {
                componentManager.removeComponent(selected);
            }
        });
        editMenu.getItems().add(deleteItem);

        menuBar.getMenus().addAll(fileMenu, editMenu);
        return menuBar;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}