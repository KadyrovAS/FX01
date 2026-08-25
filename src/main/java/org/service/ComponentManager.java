package org.service;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.components.BaseComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет компонентами на холсте: добавление, удаление, выделение, перемещение.
 */
public class ComponentManager {

    private final Pane canvas;
    private final List<BaseComponent> components = new ArrayList<>();
    private BaseComponent selectedComponent;

    // Смещение для перетаскивания
    private double dragOffsetX;
    private double dragOffsetY;

    public ComponentManager(Pane canvas) {
        this.canvas = canvas;
    }

    public void addComponent(BaseComponent component) {
        components.add(component);
        canvas.getChildren().add(component);

        // Делаем компонент интерактивным
        setupComponentInteraction(component);

        System.out.println("✅ Добавлен компонент: " + component.getComponentName());
    }

    public void removeComponent(BaseComponent component) {
        components.remove(component);
        canvas.getChildren().remove(component);
        if (selectedComponent == component) {
            selectedComponent = null;
        }
        System.out.println("🗑 Удален компонент: " + component.getComponentName());
    }

    public void clear() {
        components.clear();
        canvas.getChildren().clear();
        selectedComponent = null;
    }

    public List<BaseComponent> getComponents() {
        return new ArrayList<>(components);
    }

    public BaseComponent getSelectedComponent() {
        return selectedComponent;
    }

    public void selectComponent(BaseComponent component) {
        // Снимаем выделение со всех
        components.forEach(c -> c.setStyle(""));

        if (component != null) {
            this.selectedComponent = component;
            // Выделяем компонент (синяя рамка)
            component.setStyle("-fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-style: solid;");
            System.out.println("🔍 Выбран: " + component);
        } else {
            this.selectedComponent = null;
        }
    }

    private void setupComponentInteraction(BaseComponent component) {
        // Клик для выделения
        component.setOnMouseClicked(e -> {
            selectComponent(component);
            e.consume();
        });

        // Перетаскивание
        component.setOnMousePressed(e -> {
            if (component.isDraggable()) {
                dragOffsetX = e.getSceneX() - component.getLayoutX();
                dragOffsetY = e.getSceneY() - component.getLayoutY();
                selectComponent(component);
                component.toFront(); // Поднимаем на передний план
            }
        });

        component.setOnMouseDragged(e -> {
            if (component.isDraggable()) {
                double newX = e.getSceneX() - dragOffsetX;
                double newY = e.getSceneY() - dragOffsetY;

                // Ограничиваем перемещение в пределах холста
                newX = Math.max(0, Math.min(newX, canvas.getWidth() - component.getPrefWidth()));
                newY = Math.max(0, Math.min(newY, canvas.getHeight() - component.getPrefHeight()));

                component.setLayoutX(newX);
                component.setLayoutY(newY);
            }
        });

        // Двойной клик для редактирования текста
        component.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                editComponentText(component);
            }
        });
    }

    private void editComponentText(BaseComponent component) {
        // Простой вариант - показываем диалог
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog(component.getText());
        dialog.setTitle("Редактирование");
        dialog.setHeaderText("Измените текст компонента");
        dialog.setContentText("Текст:");

        dialog.showAndWait().ifPresent(newText -> {
            component.setText(newText);
            System.out.println("✏️ Текст изменен на: " + newText);
        });
    }
}