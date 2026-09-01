package org.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import org.components.BaseComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Управляет компонентами на холсте.
 * <p>
 * Отвечает за:
 * <ul>
 *   <li>Добавление и удаление компонентов</li>
 *   <li>Выделение компонентов</li>
 *   <li>Перетаскивание компонентов</li>
 *   <li>Изменение размера компонентов (через хэндл)</li>
 *   <li>Уведомление внешних классов о событиях (через колбэки)</li>
 * </ul>
 * </p>
 *
 * <p>Хэндл изменения размера реализован как отдельный визуальный элемент на холсте,
 * а не внутри компонента, что гарантирует его видимость поверх всех элементов.</p>
 *
 * @see org.components.BaseComponent
 * @see org.ui.PropertiesPanel
 */
public class ComponentManager {

    // ================================================================
    // ПОЛЯ
    // ================================================================

    /** Холст (рабочая область) */
    private final Pane canvas;

    /** Список всех компонентов на холсте */
    private final List<BaseComponent> components = new ArrayList<>();

    /** Текущий выбранный компонент */
    private BaseComponent selectedComponent;

    // ================================================================
    // ХЭНДЛ ДЛЯ ИЗМЕНЕНИЯ РАЗМЕРА (на холсте)
    // ================================================================

    /** Слой для хэндла */
    private StackPane handleLayer;

    /** Визуальный прямоугольник хэндла */
    private Rectangle resizeHandle;

    /** Компонент, размер которого сейчас изменяется */
    private BaseComponent currentResizingComponent;

    // ================================================================
    // КОЛБЭКИ
    // ================================================================

    /** Вызывается при выделении компонента */
    private Consumer<BaseComponent> onComponentSelected;

    /** Вызывается при двойном клике по компоненту */
    private Consumer<BaseComponent> onComponentDoubleClick;

    /** Вызывается при изменении компонента (размер, позиция) */
    private Runnable onComponentChanged;

    // ================================================================
    // ПЕРЕТАСКИВАНИЕ
    // ================================================================

    private double dragOffsetX;
    private double dragOffsetY;

    // ================================================================
    // ИЗМЕНЕНИЕ РАЗМЕРА
    // ================================================================

    private boolean resizing = false;
    private double resizeStartX;
    private double resizeStartY;
    private double resizeStartWidth;
    private double resizeStartHeight;

    // ================================================================
    // КОНСТРУКТОР
    // ================================================================

    /**
     * Создаёт менеджер компонентов для указанного холста.
     *
     * @param canvas холст, на котором будут размещаться компоненты
     */
    public ComponentManager(Pane canvas) {
        this.canvas = canvas;
        System.out.println("🗂️ ComponentManager создан");
        createGlobalResizeHandle();
    }

    // ================================================================
    // ХЭНДЛ ДЛЯ ИЗМЕНЕНИЯ РАЗМЕРА
    // ================================================================

    /**
     * Создаёт хэндл изменения размера на холсте.
     * Хэндл размещается поверх всех компонентов и не перекрывается ими.
     */
    private void createGlobalResizeHandle() {
        System.out.println("🟦 createGlobalResizeHandle: создаем хэндл на холсте");

        handleLayer = new StackPane();
        handleLayer.setManaged(false);
        handleLayer.setMouseTransparent(false);
        handleLayer.setVisible(false);

        // Хэндл 7x7
        resizeHandle = new Rectangle(7, 7);
        resizeHandle.setFill(Color.web("#3498db"));
        resizeHandle.setStroke(Color.web("#ffffff"));
        resizeHandle.setStrokeWidth(1);
        resizeHandle.setArcWidth(2);
        resizeHandle.setArcHeight(2);
        resizeHandle.setEffect(new DropShadow(2, Color.rgb(0, 0, 0, 0.3)));

        handleLayer.getChildren().add(resizeHandle);
        handleLayer.setCursor(javafx.scene.Cursor.SE_RESIZE);

        // Обработчики хэндла
        handleLayer.setOnMousePressed(e -> {
            if (currentResizingComponent != null && currentResizingComponent.isResizable()) {
                resizing = true;
                resizeStartX = e.getSceneX();
                resizeStartY = e.getSceneY();
                resizeStartWidth = currentResizingComponent.getPrefWidth();
                resizeStartHeight = currentResizingComponent.getPrefHeight();
                resizeHandle.setFill(Color.web("#e74c3c"));
                System.out.println("🔄 Начало изменения размера: " + resizeStartWidth + "x" + resizeStartHeight);
                e.consume();
            }
        });

        handleLayer.setOnMouseDragged(e -> {
            if (resizing && currentResizingComponent != null) {
                double deltaX = e.getSceneX() - resizeStartX;
                double deltaY = e.getSceneY() - resizeStartY;

                double newWidth = Math.max(30, resizeStartWidth + deltaX);
                double newHeight = Math.max(30, resizeStartHeight + deltaY);

                currentResizingComponent.setPrefSize(newWidth, newHeight);
                currentResizingComponent.refresh();

                updateHandlePosition(currentResizingComponent);

                System.out.println("🔄 Изменение размера: " + newWidth + "x" + newHeight);
                e.consume();
            }
        });

        handleLayer.setOnMouseReleased(e -> {
            if (resizing) {
                resizing = false;
                resizeHandle.setFill(Color.web("#3498db"));
                System.out.println("🔄 Изменение размера завершено");
                if (onComponentChanged != null) {
                    onComponentChanged.run();
                }
                e.consume();
            }
        });

        canvas.getChildren().add(handleLayer);
        System.out.println("🟦 createGlobalResizeHandle: хэндл добавлен на холст");
    }

    /**
     * Обновляет позицию хэндла в соответствии с позицией компонента.
     * Хэндл размещается в правом нижнем углу компонента.
     *
     * @param component компонент, для которого обновляется позиция хэндла
     */
    private void updateHandlePosition(BaseComponent component) {
        if (component == null || handleLayer == null) return;

        double x = component.getLayoutX() + component.getPrefWidth() - 4;
        double y = component.getLayoutY() + component.getPrefHeight() - 4;

        handleLayer.setLayoutX(x);
        handleLayer.setLayoutY(y);
        handleLayer.toFront();
    }

    /**
     * Показывает хэндл для указанного компонента.
     *
     * @param component компонент, для которого показывается хэндл
     */
    private void showResizeHandle(BaseComponent component) {
        if (component != null && component.isResizable()) {
            currentResizingComponent = component;
            updateHandlePosition(component);
            handleLayer.setVisible(true);
            handleLayer.toFront();
            System.out.println("🟦 Хэндл ПОКАЗАН на холсте!");
        }
    }

    /**
     * Скрывает хэндл.
     */
    private void hideResizeHandle() {
        handleLayer.setVisible(false);
        currentResizingComponent = null;
        System.out.println("⬜ Хэндл скрыт");
    }

    // ================================================================
    // КОЛБЭКИ
    // ================================================================

    /**
     * Устанавливает колбэк, вызываемый при выделении компонента.
     *
     * @param callback функция, принимающая {@link BaseComponent}
     */
    public void setOnComponentSelected(Consumer<BaseComponent> callback) {
        this.onComponentSelected = callback;
        System.out.println("📌 onComponentSelected установлен");
    }

    /**
     * Устанавливает колбэк, вызываемый при двойном клике по компоненту.
     *
     * @param callback функция, принимающая {@link BaseComponent}
     */
    public void setOnComponentDoubleClick(Consumer<BaseComponent> callback) {
        this.onComponentDoubleClick = callback;
        System.out.println("📌 onComponentDoubleClick установлен");
    }

    /**
     * Устанавливает колбэк, вызываемый при изменении компонента.
     *
     * @param callback функция {@link Runnable}
     */
    public void setOnComponentChanged(Runnable callback) {
        this.onComponentChanged = callback;
        System.out.println("📌 onComponentChanged установлен");
    }

    /**
     * Уведомляет о выделении компонента.
     */
    private void notifySelectionChanged() {
        if (onComponentSelected != null) {
            onComponentSelected.accept(selectedComponent);
        }
    }

    /**
     * Уведомляет о двойном клике по компоненту.
     *
     * @param component компонент, по которому был совершён двойной клик
     */
    private void notifyDoubleClick(BaseComponent component) {
        if (onComponentDoubleClick != null) {
            onComponentDoubleClick.accept(component);
        }
    }

    // ================================================================
    // ОСНОВНЫЕ МЕТОДЫ
    // ================================================================

    /**
     * Добавляет компонент на холст.
     *
     * @param component компонент для добавления
     */
    public void addComponent(BaseComponent component) {
        components.add(component);
        canvas.getChildren().add(component);
        setupComponentInteraction(component);
        System.out.println("✅ Добавлен: " + component);
    }

    /**
     * Удаляет компонент с холста.
     *
     * @param component компонент для удаления
     */
    public void removeComponent(BaseComponent component) {
        components.remove(component);
        canvas.getChildren().remove(component);
        if (selectedComponent == component) {
            selectedComponent = null;
            hideResizeHandle();
            notifySelectionChanged();
        }
        System.out.println("🗑 Удален: " + component);
    }

    /**
     * Очищает холст — удаляет все компоненты.
     */
    public void clear() {
        components.clear();
        canvas.getChildren().clear();
        selectedComponent = null;
        hideResizeHandle();
        notifySelectionChanged();
        // Восстанавливаем хэндл на холсте
        if (handleLayer != null && !canvas.getChildren().contains(handleLayer)) {
            canvas.getChildren().add(handleLayer);
        }
    }

    /**
     * Выделяет указанный компонент.
     * Снимает выделение со всех остальных компонентов.
     *
     * @param component компонент для выделения, или {@code null} для снятия выделения
     */
    public void selectComponent(BaseComponent component) {
        System.out.println("🔍 selectComponent вызван для: " + component);

        // Снимаем выделение со всех
        components.forEach(c -> {
            c.setStyle("");
        });

        hideResizeHandle();

        if (component != null) {
            this.selectedComponent = component;
            component.setStyle(
                    "-fx-border-color: #3498db; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-style: solid;"
            );
            showResizeHandle(component);
            System.out.println("🔍 Выбран: " + component);
        } else {
            this.selectedComponent = null;
            System.out.println("🔍 Выделение сброшено");
        }

        notifySelectionChanged();
    }

    // ================================================================
    // НАСТРОЙКА ВЗАИМОДЕЙСТВИЯ С КОМПОНЕНТОМ
    // ================================================================

    /**
     * Настраивает взаимодействие с компонентом:
     * <ul>
     *   <li>Выделение по клику</li>
     *   <li>Редактирование по двойному клику</li>
     *   <li>Перетаскивание</li>
     *   <li>Удаление при выходе за пределы холста</li>
     * </ul>
     *
     * @param component компонент для настройки
     */
    private void setupComponentInteraction(BaseComponent component) {
        // ===== ВЫДЕЛЕНИЕ ПО КЛИКУ =====
        component.setOnMouseClicked(e -> {
            selectComponent(component);
            e.consume();
        });

        // ===== ДВОЙНОЙ КЛИК =====
        component.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                selectComponent(component);
                notifyDoubleClick(component);
                e.consume();
            }
        });

        // ===== ПЕРЕТАСКИВАНИЕ =====
        component.setOnMousePressed(e -> {
            if (component.isDraggable()) {
                dragOffsetX = e.getSceneX() - component.getLayoutX();
                dragOffsetY = e.getSceneY() - component.getLayoutY();
                selectComponent(component);
                component.toFront();
                if (selectedComponent == component) {
                    updateHandlePosition(component);
                }
            }
        });

        component.setOnMouseDragged(e -> {
            if (component.isDraggable()) {
                double newX = e.getSceneX() - dragOffsetX;
                double newY = e.getSceneY() - dragOffsetY;
                component.setLayoutX(newX);
                component.setLayoutY(newY);
                if (selectedComponent == component) {
                    updateHandlePosition(component);
                }
                if (onComponentChanged != null) {
                    onComponentChanged.run();
                }
            }
        });

        // ===== УДАЛЕНИЕ ПРИ ВЫХОДЕ ЗА ПРЕДЕЛЫ =====
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
                        if (selectedComponent == component) {
                            updateHandlePosition(component);
                        }
                        if (onComponentChanged != null) {
                            onComponentChanged.run();
                        }
                    }
                } else {
                    double clampedX = Math.max(0, Math.min(x, canvas.getWidth() - component.getPrefWidth()));
                    double clampedY = Math.max(0, Math.min(y, canvas.getHeight() - component.getPrefHeight()));
                    component.setLayoutX(clampedX);
                    component.setLayoutY(clampedY);
                    if (selectedComponent == component) {
                        updateHandlePosition(component);
                    }
                    if (onComponentChanged != null) {
                        onComponentChanged.run();
                    }
                }
            }
        });
    }

    // ================================================================
    // ГЕТТЕРЫ
    // ================================================================

    /**
     * Возвращает список всех компонентов на холсте.
     *
     * @return копия списка компонентов
     */
    public List<BaseComponent> getComponents() {
        return new ArrayList<>(components);
    }

    /**
     * Возвращает текущий выбранный компонент.
     *
     * @return выбранный компонент, или {@code null} если ничего не выбрано
     */
    public BaseComponent getSelectedComponent() {
        return selectedComponent;
    }
}