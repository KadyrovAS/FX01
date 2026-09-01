package org.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.components.BaseComponent;

/**
 * Панель свойств для редактирования параметров выбранного компонента.
 * <p>
 * Отображает все доступные свойства компонента:
 * <ul>
 *   <li><b>Текст</b> — {@link TextField}</li>
 *   <li><b>Размер шрифта</b> — {@link Spinner}&lt;Double&gt;</li>
 *   <li><b>Цвет текста</b> — {@link ColorPicker}</li>
 *   <li><b>Цвет фона</b> — {@link ColorPicker}</li>
 *   <li><b>Ширина</b> — {@link Spinner}&lt;Double&gt;</li>
 *   <li><b>Высота</b> — {@link Spinner}&lt;Double&gt;</li>
 *   <li><b>X позиция</b> — {@link Spinner}&lt;Double&gt;</li>
 *   <li><b>Y позиция</b> — {@link Spinner}&lt;Double&gt;</li>
 *   <li><b>Перетаскивание</b> — {@link CheckBox}</li>
 *   <li><b>Изменение размера</b> — {@link CheckBox}</li>
 * </ul>
 * </p>
 *
 * <p>Дополнительно отображает ID и тип компонента (только для информации).</p>
 *
 * @see org.components.BaseComponent
 * @see org.service.ComponentManager
 */
public class PropertiesPanel extends VBox {

    // ================================================================
    // ПОЛЯ
    // ================================================================

    /** Текущий редактируемый компонент */
    private BaseComponent currentComponent;

    // Информационные поля
    private Label idLabel;
    private Label typeLabel;

    // Редактируемые поля
    private TextField textField;
    private Spinner<Double> fontSizeSpinner;
    private ColorPicker textColorPicker;
    private ColorPicker bgColorPicker;
    private Spinner<Double> widthSpinner;
    private Spinner<Double> heightSpinner;
    private Spinner<Double> xSpinner;
    private Spinner<Double> ySpinner;
    private CheckBox draggableCheckBox;
    private CheckBox resizableCheckBox;

    // Контейнеры
    private GridPane infoGrid;
    private GridPane formGrid;
    private Label placeholderLabel;

    /** Флаг для предотвращения рекурсивных обновлений */
    private boolean updating = false;

    // ================================================================
    // КОНСТРУКТОР
    // ================================================================

    /**
     * Создаёт панель свойств.
     * Изначально показывает заглушку "Выберите компонент на холсте".
     */
    public PropertiesPanel() {
        setPadding(new Insets(10));
        setSpacing(10);
        setStyle("-fx-background-color: #f8f9fa;");
        setPrefWidth(300);

        System.out.println("📐 PropertiesPanel создан");

        Label title = new Label("📐 Свойства");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        getChildren().add(title);
        getChildren().add(new Separator());

        infoGrid = createInfoSection();
        getChildren().add(infoGrid);

        formGrid = createPropertyForm();
        getChildren().add(formGrid);

        showPlaceholder();
    }

    // ================================================================
    // СОЗДАНИЕ UI
    // ================================================================

    /**
     * Создаёт информационную секцию с ID и типом компонента.
     *
     * @return {@link GridPane} с информацией
     */
    private GridPane createInfoSection() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 0, 5, 0));

        String labelStyle = "-fx-text-fill: #6c757d; -fx-font-size: 11px;";
        String valueStyle = "-fx-font-size: 11px;";

        Label idTitle = new Label("ID:");
        idTitle.setStyle(labelStyle);
        idLabel = new Label("-");
        idLabel.setStyle(valueStyle);

        Label typeTitle = new Label("Тип:");
        typeTitle.setStyle(labelStyle);
        typeLabel = new Label("-");
        typeLabel.setStyle(valueStyle);

        grid.add(idTitle, 0, 0);
        grid.add(idLabel, 1, 0);
        grid.add(typeTitle, 0, 1);
        grid.add(typeLabel, 1, 1);

        GridPane.setHgrow(idLabel, javafx.scene.layout.Priority.ALWAYS);
        GridPane.setHgrow(typeLabel, javafx.scene.layout.Priority.ALWAYS);

        return grid;
    }

    /**
     * Создаёт форму для редактирования свойств компонента.
     *
     * @return {@link GridPane} с элементами управления
     */
    private GridPane createPropertyForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(5, 0, 10, 0));

        int row = 0;

        // ========== ТЕКСТ ==========
        Separator textSep = new Separator();
        textSep.setStyle("-fx-background-color: #dee2e6;");
        grid.add(textSep, 0, row, 2, 1);
        row++;

        Label textLabel = new Label("Текст:");
        textLabel.setStyle("-fx-font-weight: bold;");
        textField = new TextField();
        textField.setPromptText("Введите текст");

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("✏️ textField изменен: \"" + newVal + "\"");
            if (!updating && currentComponent != null) {
                updateProperty("text", newVal);
            }
        });

        grid.add(textLabel, 0, row);
        grid.add(textField, 1, row);
        row++;

        // ========== ШРИФТ И ЦВЕТА ==========
        Separator fontSep = new Separator();
        fontSep.setStyle("-fx-background-color: #dee2e6;");
        grid.add(fontSep, 0, row, 2, 1);
        row++;

        Label fontSizeLabel = new Label("Размер шрифта:");
        fontSizeLabel.setStyle("-fx-font-weight: bold;");
        fontSizeSpinner = new Spinner<>();
        fontSizeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(8, 72, 14, 1));
        fontSizeSpinner.setEditable(true);
        fontSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !updating) {
                updateProperty("fontSize", newVal.doubleValue());
            }
        });
        grid.add(fontSizeLabel, 0, row);
        grid.add(fontSizeSpinner, 1, row);
        row++;

        Label textColorLabel = new Label("Цвет текста:");
        textColorLabel.setStyle("-fx-font-weight: bold;");
        textColorPicker = new ColorPicker();
        textColorPicker.setOnAction(e -> {
            Color color = textColorPicker.getValue();
            if (color != null && !updating) {
                updateProperty("textColor", toHex(color));
            }
        });
        grid.add(textColorLabel, 0, row);
        grid.add(textColorPicker, 1, row);
        row++;

        Label bgColorLabel = new Label("Цвет фона:");
        bgColorLabel.setStyle("-fx-font-weight: bold;");
        bgColorPicker = new ColorPicker();
        bgColorPicker.setOnAction(e -> {
            Color color = bgColorPicker.getValue();
            if (color != null && !updating) {
                updateProperty("backgroundColor", toHex(color));
            }
        });
        grid.add(bgColorLabel, 0, row);
        grid.add(bgColorPicker, 1, row);
        row++;

        // ========== РАЗМЕРЫ ==========
        Separator sizeSep = new Separator();
        sizeSep.setStyle("-fx-background-color: #dee2e6;");
        grid.add(sizeSep, 0, row, 2, 1);
        row++;

        Label widthLabel = new Label("Ширина:");
        widthLabel.setStyle("-fx-font-weight: bold;");
        widthSpinner = new Spinner<>();
        widthSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(20, 1000, 150, 5));
        widthSpinner.setEditable(true);
        widthSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !updating) {
                updateProperty("width", newVal.doubleValue());
            }
        });
        grid.add(widthLabel, 0, row);
        grid.add(widthSpinner, 1, row);
        row++;

        Label heightLabel = new Label("Высота:");
        heightLabel.setStyle("-fx-font-weight: bold;");
        heightSpinner = new Spinner<>();
        heightSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(20, 1000, 50, 5));
        heightSpinner.setEditable(true);
        heightSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !updating) {
                updateProperty("height", newVal.doubleValue());
            }
        });
        grid.add(heightLabel, 0, row);
        grid.add(heightSpinner, 1, row);
        row++;

        // ========== ПОЗИЦИЯ ==========
        Separator posSep = new Separator();
        posSep.setStyle("-fx-background-color: #dee2e6;");
        grid.add(posSep, 0, row, 2, 1);
        row++;

        Label xLabel = new Label("X (позиция):");
        xLabel.setStyle("-fx-font-weight: bold;");
        xSpinner = new Spinner<>();
        xSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 2000, 0, 5));
        xSpinner.setEditable(true);
        xSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !updating) {
                updateProperty("x", newVal.doubleValue());
            }
        });
        grid.add(xLabel, 0, row);
        grid.add(xSpinner, 1, row);
        row++;

        Label yLabel = new Label("Y (позиция):");
        yLabel.setStyle("-fx-font-weight: bold;");
        ySpinner = new Spinner<>();
        ySpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 2000, 0, 5));
        ySpinner.setEditable(true);
        ySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !updating) {
                updateProperty("y", newVal.doubleValue());
            }
        });
        grid.add(yLabel, 0, row);
        grid.add(ySpinner, 1, row);
        row++;

        // ========== ПОВЕДЕНИЕ ==========
        Separator behaviorSep = new Separator();
        behaviorSep.setStyle("-fx-background-color: #dee2e6;");
        grid.add(behaviorSep, 0, row, 2, 1);
        row++;

        draggableCheckBox = new CheckBox("Разрешено перетаскивание");
        draggableCheckBox.setOnAction(e -> {
            if (!updating && currentComponent != null) {
                currentComponent.setDraggable(draggableCheckBox.isSelected());
            }
        });
        grid.add(draggableCheckBox, 0, row, 2, 1);
        row++;

        resizableCheckBox = new CheckBox("Разрешено изменение размера");
        resizableCheckBox.setOnAction(e -> {
            if (!updating && currentComponent != null) {
                currentComponent.setResizable(resizableCheckBox.isSelected());
                boolean enabled = resizableCheckBox.isSelected();
                widthSpinner.setDisable(!enabled);
                heightSpinner.setDisable(!enabled);
            }
        });
        grid.add(resizableCheckBox, 0, row, 2, 1);
        row++;

        return grid;
    }

    // ================================================================
    // ОБНОВЛЕНИЕ СВОЙСТВА
    // ================================================================

    /**
     * Обновляет свойство компонента.
     *
     * @param property название свойства ("text", "fontSize", "width" и т.д.)
     * @param value    новое значение
     */
    private void updateProperty(String property, Object value) {
        if (updating || currentComponent == null) return;

        System.out.println("🔄 updateProperty: " + property + " = " + value);

        try {
            switch (property) {
                case "text" -> {
                    String newText = (String) value;
                    if (!currentComponent.getText().equals(newText)) {
                        currentComponent.setText(newText);
                    }
                }
                case "fontSize" -> {
                    double newVal = (Double) value;
                    if (Math.abs(currentComponent.getFontSize() - newVal) > 0.01) {
                        currentComponent.setFontSize(newVal);
                    }
                }
                case "textColor" -> {
                    String newColor = (String) value;
                    if (!currentComponent.getTextColor().equals(newColor)) {
                        currentComponent.setTextColor(newColor);
                    }
                }
                case "backgroundColor" -> {
                    String newColor = (String) value;
                    if (!currentComponent.getBackgroundColor().equals(newColor)) {
                        currentComponent.setBackgroundColor(newColor);
                    }
                }
                case "width" -> {
                    double w = (Double) value;
                    currentComponent.setPrefSize(w, currentComponent.getPrefHeight());
                }
                case "height" -> {
                    double h = (Double) value;
                    currentComponent.setPrefSize(currentComponent.getPrefWidth(), h);
                }
                case "x" -> {
                    double x = (Double) value;
                    currentComponent.setLayoutX(x);
                }
                case "y" -> {
                    double y = (Double) value;
                    currentComponent.setLayoutY(y);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка обновления '" + property + "': " + e.getMessage());
        }
    }

    // ================================================================
    // ЗАПОЛНЕНИЕ ПАНЕЛИ СВОЙСТВ
    // ================================================================

    /**
     * Показывает свойства указанного компонента.
     *
     * @param component компонент, чьи свойства нужно отобразить,
     *                  или {@code null} для отображения заглушки
     */
    public void showProperties(BaseComponent component) {
        System.out.println("📐 showProperties вызван с: " + component);
        this.currentComponent = component;
        updating = true;

        try {
            if (component == null) {
                System.out.println("📐 showProperties: component == null, показываем заглушку");
                showPlaceholder();
                return;
            }

            System.out.println("📐 showProperties: отображаем свойства для " + component.getComponentType());

            // Удаляем заглушку
            if (placeholderLabel != null) {
                getChildren().remove(placeholderLabel);
                placeholderLabel = null;
            }

            // Показываем форму
            if (!getChildren().contains(infoGrid)) {
                getChildren().add(2, infoGrid);
            }
            if (!getChildren().contains(formGrid)) {
                getChildren().add(formGrid);
            }
            infoGrid.setVisible(true);
            infoGrid.setManaged(true);
            formGrid.setVisible(true);
            formGrid.setManaged(true);

            setTitle("📐 " + component.getComponentType());

            // Заполняем данными
            idLabel.setText(component.getComponentId().substring(0, 8) + "...");
            typeLabel.setText(component.getComponentType());

            textField.setText(component.getText());
            fontSizeSpinner.getValueFactory().setValue(component.getFontSize());
            textColorPicker.setValue(Color.web(component.getTextColor()));
            bgColorPicker.setValue(Color.web(component.getBackgroundColor()));
            widthSpinner.getValueFactory().setValue(component.getPrefWidth());
            heightSpinner.getValueFactory().setValue(component.getPrefHeight());
            xSpinner.getValueFactory().setValue(component.getLayoutX());
            ySpinner.getValueFactory().setValue(component.getLayoutY());

            draggableCheckBox.setSelected(component.isDraggable());
            resizableCheckBox.setSelected(component.isResizable());

            boolean enabled = component.isResizable();
            widthSpinner.setDisable(!enabled);
            heightSpinner.setDisable(!enabled);

            System.out.println("📐 showProperties: свойства отображены успешно");

        } finally {
            updating = false;
        }
    }

    /**
     * Обновляет значения в панели свойств без перестроения формы.
     * Используется при изменении размера/позиции мышкой.
     */
    public void refreshValues() {
        if (currentComponent == null) return;
        System.out.println("🔄 refreshValues() вызван");
        updating = true;
        try {
            widthSpinner.getValueFactory().setValue(currentComponent.getPrefWidth());
            heightSpinner.getValueFactory().setValue(currentComponent.getPrefHeight());
            xSpinner.getValueFactory().setValue(currentComponent.getLayoutX());
            ySpinner.getValueFactory().setValue(currentComponent.getLayoutY());
        } finally {
            updating = false;
        }
    }

    /**
     * Устанавливает фокус на поле "Текст" для быстрого редактирования.
     */
    public void focusTextField() {
        if (currentComponent != null) {
            textField.requestFocus();
            textField.selectAll();
        }
    }

    // ================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ================================================================

    /**
     * Показывает заглушку "Выберите компонент на холсте".
     */
    private void showPlaceholder() {
        setTitle("📐 Свойства");

        if (placeholderLabel != null) {
            getChildren().remove(placeholderLabel);
            placeholderLabel = null;
        }

        if (infoGrid != null) {
            getChildren().remove(infoGrid);
        }
        if (formGrid != null) {
            getChildren().remove(formGrid);
        }

        placeholderLabel = new Label("Выберите компонент\nна холсте");
        placeholderLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");
        getChildren().add(placeholderLabel);
    }

    /**
     * Устанавливает заголовок панели.
     *
     * @param title новый заголовок
     */
    private void setTitle(String title) {
        if (!getChildren().isEmpty()) {
            Label titleLabel = (Label) getChildren().get(0);
            titleLabel.setText(title);
        }
    }

    /**
     * Преобразует {@link Color} в HEX-строку (#RRGGBB).
     *
     * @param color цвет для преобразования
     * @return строка в формате "#RRGGBB"
     */
    private String toHex(Color color) {
        if (color == null) return "#000000";
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }
}