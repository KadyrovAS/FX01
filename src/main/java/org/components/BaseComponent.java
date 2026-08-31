package org.components;

import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public abstract class BaseComponent extends StackPane {

    // ================================================================
    // 1. ПОЛЯ
    // ================================================================

    private String componentId = java.util.UUID.randomUUID().toString();
    private String componentName = "Компонент";
    private String componentType;
    private String text = "";
    private double fontSize = 14;
    private String textColor = "#000000";
    private String backgroundColor = "#FFFFFF";
    private boolean resizable = true;
    private boolean draggable = true;

    // ================================================================
    // 2. ПОЛЯ ДЛЯ ИЗМЕНЕНИЯ РАЗМЕРА
    // ================================================================

    protected Rectangle resizeHandle;
    private boolean resizing = false;
    private double resizeStartX;
    private double resizeStartY;
    private double resizeStartWidth;
    private double resizeStartHeight;

    // ================================================================
    // 3. КОНСТРУКТОР
    // ================================================================

    public BaseComponent(String type) {
        this.componentType = type;
        this.setPrefSize(150, 50);
        this.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-style: dashed;");
        setPickOnBounds(true);

        setOnDragDetected(e -> {
            setStyle("-fx-border-color: #ff6b6b; -fx-border-width: 2px; -fx-border-style: solid;");
        });

        setOnDragDone(e -> {
            setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-style: dashed;");
        });

        createResizeHandle();
    }

    // ================================================================
    // 4. ХЭНДЛ ДЛЯ ИЗМЕНЕНИЯ РАЗМЕРА
    // ================================================================

    private void createResizeHandle() {
        resizeHandle = new Rectangle(14, 14);  // ← увеличил размер
        resizeHandle.setStyle(
                "-fx-fill: #e74c3c; " +            // ← яркий красный цвет
                        "-fx-stroke: white; " +
                        "-fx-stroke-width: 2px; " +
                        "-fx-arc-width: 4px; " +
                        "-fx-arc-height: 4px;"
        );
        resizeHandle.setVisible(false);
        resizeHandle.setManaged(false);
        resizeHandle.setCursor(Cursor.SE_RESIZE);

        // Позиционирование в правом нижнем углу
        resizeHandle.layoutXProperty().bind(
                widthProperty().subtract(resizeHandle.widthProperty())
        );
        resizeHandle.layoutYProperty().bind(
                heightProperty().subtract(resizeHandle.heightProperty())
        );

        // ===== ОБРАБОТЧИКИ =====
        resizeHandle.setOnMousePressed(e -> {
            if (resizable) {
                resizing = true;
                resizeStartX = e.getSceneX();
                resizeStartY = e.getSceneY();
                resizeStartWidth = getPrefWidth();
                resizeStartHeight = getPrefHeight();
                System.out.println("🔄 Начало изменения размера: " + resizeStartWidth + "x" + resizeStartHeight);
                e.consume();
            }
        });

        resizeHandle.setOnMouseDragged(e -> {
            if (resizing && resizable) {
                double deltaX = e.getSceneX() - resizeStartX;
                double deltaY = e.getSceneY() - resizeStartY;

                double newWidth = Math.max(30, resizeStartWidth + deltaX);
                double newHeight = Math.max(30, resizeStartHeight + deltaY);

                setPrefSize(newWidth, newHeight);
                refresh();

                System.out.println("🔄 Изменение размера: " + newWidth + "x" + newHeight);
                e.consume();
            }
        });

        resizeHandle.setOnMouseReleased(e -> {
            if (resizing) {
                resizing = false;
                System.out.println("🔄 Изменение размера завершено");
                e.consume();
            }
        });

        getChildren().add(resizeHandle);

        // ============================================================
        // 🔥 ВАЖНО: хэндл всегда должен быть на переднем плане
        // ============================================================
        resizeHandle.toFront();
    }

    // ================================================================
    // 5. ПОКАЗ/СКРЫТИЕ ХЭНДЛА
    // ================================================================

    public void showResizeHandle() {
        if (resizable && resizeHandle != null) {
            resizeHandle.setVisible(true);
            resizeHandle.toFront();  // ← поднимаем на передний план
            System.out.println("🟦 Хэндл показан");
        }
    }

    public void hideResizeHandle() {
        if (resizeHandle != null) {
            resizeHandle.setVisible(false);
            System.out.println("⬜ Хэндл скрыт");
        }
    }

    // ================================================================
    // 6. АБСТРАКТНЫЕ МЕТОДЫ
    // ================================================================

    protected abstract void buildUI();
    public abstract javafx.scene.Node getRuntimeNode();

    // ================================================================
    // 7. МЕТОД ОБНОВЛЕНИЯ UI
    // ================================================================

    public void refresh() {
        buildUI();
        if (resizeHandle != null) {
            resizeHandle.toFront();  // ← всегда на переднем плане
        }
    }

    // ================================================================
    // 8. ПРОБРОС СОБЫТИЙ
    // ================================================================

    protected void makeChildrenTransparentForMouse() {
        getChildren().forEach(node -> {
            // ============================================================
            // 🔥 ВАЖНО: НЕ делаем прозрачным хэндл!
            // ============================================================
            if (node == resizeHandle) return;

            if (node instanceof javafx.scene.control.Control ||
                    node instanceof javafx.scene.chart.Chart) {
                node.setMouseTransparent(true);
            }
        });
    }

    // ================================================================
    // 9. ГЕТТЕРЫ И СЕТТЕРЫ
    // ================================================================

    public String getComponentId() { return componentId; }
    public void setComponentId(String id) { this.componentId = id; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String name) { this.componentName = name; }

    public String getComponentType() { return componentType; }

    public String getText() { return text; }
    public void setText(String text) {
        this.text = text != null ? text : "";
        refresh();
    }

    public double getFontSize() { return fontSize; }
    public void setFontSize(double size) {
        this.fontSize = size;
        refresh();
    }

    public String getTextColor() { return textColor; }
    public void setTextColor(String color) {
        this.textColor = color != null ? color : "#000000";
        refresh();
    }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String color) {
        this.backgroundColor = color != null ? color : "#FFFFFF";
        refresh();
    }

    public boolean isResizable() { return resizable; }
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        if (!resizable) {
            hideResizeHandle();
        }
    }

    public boolean isDraggable() { return draggable; }
    public void setDraggable(boolean draggable) { this.draggable = draggable; }

    @Override
    public String toString() {
        return String.format("%s [%s]: '%s'",
                componentType,
                componentId.substring(0, 8),
                text
        );
    }
}