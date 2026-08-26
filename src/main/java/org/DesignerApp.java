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

/**
 * Главный класс приложения — визуальный конструктор.
 *
 * Отвечает за:
 * - Создание главного окна
 * - Компоновку интерфейса (меню, палитра, холст, свойства)
 * - Инициализацию ComponentManager
 * - Обработку действий пользователя
 */
public class DesignerApp extends Application {

    private ComponentManager componentManager;

    // ================================================================
    // 1. ТОЧКА ВХОДА
    // ================================================================

    public static void main(String[] args) {
        launch(args);
    }

    // ================================================================
    // 2. СОЗДАНИЕ ГЛАВНОГО ОКНА
    // ================================================================

    @Override
    public void start(Stage primaryStage) {
        // ---------- 2.1. ХОЛСТ (рабочая область) ----------
        Pane canvas = new Pane();
        canvas.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 2px;"
        );
        canvas.setPrefSize(800, 600);

        // ---------- 2.2. МЕНЕДЖЕР КОМПОНЕНТОВ ----------
        componentManager = new ComponentManager(canvas);

        // ---------- 2.3. ПАЛИТРА (левая панель) ----------
        VBox palette = createPalette();

        // ---------- 2.4. ПАНЕЛЬ СВОЙСТВ (правая панель) ----------
        VBox properties = createPropertiesPanel();

        // ---------- 2.5. РАЗДЕЛИТЕЛЬ ----------
        SplitPane mainSplit = new SplitPane(palette, canvas, properties);
        mainSplit.setDividerPositions(0.15, 0.70);  // 15% - палитра, 70% - холст, 15% - свойства

        // ---------- 2.6. МЕНЮ ----------
        MenuBar menuBar = createMenuBar();

        // ---------- 2.7. КОРНЕВОЙ КОНТЕЙНЕР ----------
        VBox root = new VBox();
        root.getChildren().addAll(menuBar, mainSplit);
        VBox.setVgrow(mainSplit, Priority.ALWAYS);  // Растягиваем разделитель по вертикали

        // ---------- 2.8. СЦЕНА И ОКНО ----------
        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setTitle("🎨 Визуальный конструктор - Studio");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("🚀 DesignerApp запущен!");
        System.out.println("📦 Доступные компоненты: " + ComponentFactory.getAvailableTypes());
        ComponentFactory.printRegistry();  // Выводим красиво в консоль
    }

    // ================================================================
    // 3. СОЗДАНИЕ ПАЛИТРЫ (ИЗМЕНЕНО!)
    // ================================================================

    private VBox createPalette() {
        VBox palette = new VBox(10);
        palette.setPadding(new Insets(10));
        palette.setStyle("-fx-background-color: #e9ecef;");
        palette.setPrefWidth(200);

        // Заголовок
        Label title = new Label("🧩 Палитра");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        palette.getChildren().addAll(title, new Separator());

        // ================================================================
        // 🔥 ИЗМЕНЕНИЕ: теперь мы берем компоненты из фабрики,
        // а не из жестко закодированного массива!
        // ================================================================
        for (String type : ComponentFactory.getAvailableTypes()) {
            String displayName = ComponentFactory.getDisplayName(type);

            Button btn = new Button(displayName);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> {
                // Создаем компонент через фабрику
                BaseComponent component = ComponentFactory.create(type);

                // Размещаем со случайным смещением
                component.setLayoutX(50 + Math.random() * 100);
                component.setLayoutY(50 + Math.random() * 100);

                // Добавляем на холст
                componentManager.addComponent(component);
            });
            palette.getChildren().add(btn);
        }

        palette.getChildren().add(new Separator());

        // Кнопка удаления выбранного компонента
        Button deleteBtn = new Button("🗑 Удалить выбранное");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            BaseComponent selected = componentManager.getSelectedComponent();
            if (selected != null) {
                componentManager.removeComponent(selected);
            } else {
                showAlert("Выберите компонент на холсте");
            }
        });
        palette.getChildren().add(deleteBtn);

        // Кнопка очистки холста
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
            }
        });
        palette.getChildren().add(clearBtn);

        return palette;
    }

    // ================================================================
    // 4. ПАНЕЛЬ СВОЙСТВ (пока заглушка)
    // ================================================================

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
        return properties;
    }

    // ================================================================
    // 5. МЕНЮ
    // ================================================================

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Меню "Файл"
        Menu fileMenu = new Menu("Файл");

        MenuItem saveItem = new MenuItem("💾 Сохранить");
        saveItem.setOnAction(e -> showAlert("Сохранение... (пока заглушка)"));

        MenuItem loadItem = new MenuItem("📂 Загрузить");
        loadItem.setOnAction(e -> showAlert("Загрузка... (пока заглушка)"));

        MenuItem exitItem = new MenuItem("Выход");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(saveItem, loadItem, new SeparatorMenuItem(), exitItem);

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

    // ================================================================
    // 6. ВСПОМОГАТЕЛЬНЫЙ МЕТОД
    // ================================================================

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}