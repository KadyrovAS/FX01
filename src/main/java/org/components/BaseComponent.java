package org.components;

import javafx.scene.layout.StackPane;

public abstract class BaseComponent extends StackPane {

    private String componentId = java.util.UUID.randomUUID().toString();
    private String componentName = "Компонент";
    private String componentType;
    private String text = "";
    private double fontSize = 14;
    private String textColor = "#000000";
    private String backgroundColor = "#FFFFFF";
    private boolean resizable = true;
    private boolean draggable = true;

    public BaseComponent(String type) {
        this.componentType = type;
        this.setPrefSize(150, 50);
        this.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-style: dashed;");
    }

    /**
     * Создает UI компонента. Вызывается при создании и при обновлении свойств.
     * Должен быть переопределен в каждом конкретном компоненте.
     */
    protected abstract void buildUI();

    /**
     * Возвращает JavaFX-узел для отображения в рантайме
     */
    public abstract javafx.scene.Node getRuntimeNode();

    /**
     * Обновляет отображение после изменения свойств
     */
    public void refresh() {
        buildUI();
    }

    // ========== Геттеры и сеттеры ==========

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
    public void setResizable(boolean resizable) { this.resizable = resizable; }

    public boolean isDraggable() { return draggable; }
    public void setDraggable(boolean draggable) { this.draggable = draggable; }

    @Override
    public String toString() {
        return String.format("%s [%s]: '%s'", componentType, componentId.substring(0, 8), text);
    }
}