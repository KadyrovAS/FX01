package org.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import org.components.BaseComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ComponentManager {

    private final Pane canvas;
    private final List<BaseComponent> components = new ArrayList<>();
    private BaseComponent selectedComponent;

    private Consumer<BaseComponent> onComponentSelected;
    private Consumer<BaseComponent> onComponentDoubleClick;
    private Runnable onComponentChanged;

    private double dragOffsetX;
    private double dragOffsetY;

    public ComponentManager(Pane canvas) {
        this.canvas = canvas;
    }

    public void setOnComponentSelected(Consumer<BaseComponent> callback) {
        this.onComponentSelected = callback;
    }

    public void setOnComponentDoubleClick(Consumer<BaseComponent> callback) {
        this.onComponentDoubleClick = callback;
    }

    public void setOnComponentChanged(Runnable callback) {
        this.onComponentChanged = callback;
    }

    private void notifySelectionChanged() {
        if (onComponentSelected != null) {
            onComponentSelected.accept(selectedComponent);
        }
    }

    private void notifyDoubleClick(BaseComponent component) {
        if (onComponentDoubleClick != null) {
            onComponentDoubleClick.accept(component);
        }
    }

    private void notifyComponentChanged() {
        if (onComponentChanged != null) {
            onComponentChanged.run();
        }
    }

    public void addComponent(BaseComponent component) {
        components.add(component);
        canvas.getChildren().add(component);
        setupComponentInteraction(component);
        System.out.println("✅ Добавлен: " + component);
    }

    public void removeComponent(BaseComponent component) {
        components.remove(component);
        canvas.getChildren().remove(component);
        if (selectedComponent == component) {
            selectedComponent = null;
            notifySelectionChanged();
        }
        System.out.println("🗑 Удален: " + component);
    }

    public void clear() {
        components.clear();
        canvas.getChildren().clear();
        selectedComponent = null;
        notifySelectionChanged();
    }

    public void selectComponent(BaseComponent component) {
        components.forEach(c -> {
            c.setStyle("");
            c.hideResizeHandle();
        });

        if (component != null) {
            this.selectedComponent = component;
            component.setStyle(
                    "-fx-border-color: #3498db; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-style: solid;"
            );
            component.showResizeHandle();  // ← показываем хэндл
            System.out.println("🔍 Выбран: " + component);
        } else {
            this.selectedComponent = null;
            System.out.println("🔍 Выделение сброшено");
        }

        notifySelectionChanged();
    }

    private void setupComponentInteraction(BaseComponent component) {
        component.setOnMouseClicked(e -> {
            selectComponent(component);
            e.consume();
        });

        component.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                selectComponent(component);
                notifyDoubleClick(component);
                e.consume();
            }
        });

        component.setOnMousePressed(e -> {
            if (component.isDraggable()) {
                dragOffsetX = e.getSceneX() - component.getLayoutX();
                dragOffsetY = e.getSceneY() - component.getLayoutY();
                selectComponent(component);
                component.toFront();
            }
        });

        component.setOnMouseDragged(e -> {
            if (component.isDraggable()) {
                double newX = e.getSceneX() - dragOffsetX;
                double newY = e.getSceneY() - dragOffsetY;
                component.setLayoutX(newX);
                component.setLayoutY(newY);
                notifyComponentChanged();
            }
        });

        component.setOnMouseReleased(e -> {
            if (component.isDraggable()) {
                double x = component.getLayoutX();
                double y = component.getLayoutY();

                boolean isOutside =
                        x + component.getPrefWidth() < 0 ||
                                x > canvas.getWidth() ||
                                y + component.getPrefHeight() < 0 ||
                                y > canvas.getHeight();

                if (isOutside) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Удаление компонента");
                    confirm.setHeaderText("Удалить компонент?");
                    confirm.setContentText("Вы перетащили компонент за пределы холста. Удалить его?");

                    if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                        removeComponent(component);
                    } else {
                        double newX = Math.max(0, Math.min(x, canvas.getWidth() - component.getPrefWidth()));
                        double newY = Math.max(0, Math.min(y, canvas.getHeight() - component.getPrefHeight()));
                        component.setLayoutX(newX);
                        component.setLayoutY(newY);
                        notifyComponentChanged();
                    }
                } else {
                    double clampedX = Math.max(0, Math.min(x, canvas.getWidth() - component.getPrefWidth()));
                    double clampedY = Math.max(0, Math.min(y, canvas.getHeight() - component.getPrefHeight()));
                    component.setLayoutX(clampedX);
                    component.setLayoutY(clampedY);
                    notifyComponentChanged();
                }
            }
        });
    }

    public List<BaseComponent> getComponents() {
        return new ArrayList<>(components);
    }

    public BaseComponent getSelectedComponent() {
        return selectedComponent;
    }
}