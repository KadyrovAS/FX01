package org.components;

import javafx.scene.layout.StackPane;

/**
 * Абстрактный базовый класс для всех визуальных компонентов конструктора.
 * <p>
 * Использует {@link StackPane} как контейнер для наложения слоёв:
 * <ul>
 *   <li>Нижний слой — сам компонент (кнопка, надпись, поле ввода, график)</li>
 *   <li>Верхний слой — рамка выделения (добавляется динамически из {@link org.service.ComponentManager})</li>
 * </ul>
 * </p>
 *
 * <p>Каждый конкретный компонент должен:</p>
 * <ul>
 *   <li>Реализовать метод {@link #buildUI()} — создание конкретного UI</li>
 *   <li>Реализовать метод {@link #getRuntimeNode()} — получение "чистого" узла для рантайма</li>
 *   <li>Вызывать {@link #makeChildrenTransparentForMouse()} в {@code buildUI()}</li>
 * </ul>
 *
 * @see org.components.ButtonComponent
 * @see org.components.LabelComponent
 * @see org.components.TextFieldComponent
 * @see org.components.ChartComponent
 * @see org.service.ComponentManager
 */
public abstract class BaseComponent extends StackPane {

    // ================================================================
    // ПОЛЯ (состояние компонента)
    // ================================================================

    /** Уникальный идентификатор компонента (UUID) */
    private String componentId = java.util.UUID.randomUUID().toString();

    /** Человеческое имя компонента */
    private String componentName = "Компонент";

    /** Технический тип компонента ("Button", "Label", "TextField", "Chart") */
    private String componentType;

    /** Текст, отображаемый на компоненте */
    private String text = "";

    /** Размер шрифта в пикселях */
    private double fontSize = 14;

    /** Цвет текста в формате HEX (#000000 — чёрный) */
    private String textColor = "#000000";

    /** Цвет фона в формате HEX (#FFFFFF — белый) */
    private String backgroundColor = "#FFFFFF";

    /** Разрешено ли изменение размера компонента */
    private boolean resizable = true;

    /** Разрешено ли перетаскивание компонента */
    private boolean draggable = true;

    // ================================================================
    // КОНСТРУКТОР
    // ================================================================

    /**
     * Создаёт новый компонент с указанным типом.
     *
     * @param type технический тип ("Button", "Label", "TextField", "Chart")
     */
    public BaseComponent(String type) {
        this.componentType = type;
        this.setPrefSize(150, 50);
        this.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-style: dashed;");
        setPickOnBounds(true);

        System.out.println("🏗️ BaseComponent создан: " + type);

        setOnDragDetected(e -> {
            setStyle("-fx-border-color: #ff6b6b; -fx-border-width: 2px; -fx-border-style: solid;");
        });

        setOnDragDone(e -> {
            setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-style: dashed;");
        });
    }

    // ================================================================
    // АБСТРАКТНЫЕ МЕТОДЫ
    // ================================================================

    /**
     * Создаёт UI компонента.
     * <p>
     * Этот метод вызывается:
     * <ul>
     *   <li>При создании компонента</li>
     *   <li>При изменении свойств (текст, цвет, размер и т.д.)</li>
     * </ul>
     * </p>
     * <p>
     * Каждый наследник должен:
     * <ol>
     *   <li>Очистить {@code getChildren()}</li>
     *   <li>Создать конкретный элемент (Button, Label, TextField, LineChart)</li>
     *   <li>Применить стили (цвет, шрифт, размер)</li>
     *   <li>Добавить элемент в {@code getChildren()}</li>
     *   <li>Вызвать {@link #makeChildrenTransparentForMouse()}</li>
     * </ol>
     * </p>
     */
    protected abstract void buildUI();

    /**
     * Получает "чистый" JavaFX-узел для отображения в рантайме.
     * <p>
     * Этот узел будет использоваться:
     * <ul>
     *   <li>При экспорте проекта в готовое приложение</li>
     *   <li>При предварительном просмотре (preview)</li>
     * </ul>
     * </p>
     * <p>
     * Отличается от {@code buildUI()} тем, что не содержит:
     * <ul>
     *   <li>Пунктирной рамки</li>
     *   <li>Обработчиков выделения и перетаскивания</li>
     *   <li>Дополнительных слоёв</li>
     * </ul>
     * </p>
     *
     * @return "чистый" JavaFX-узел для отображения
     */
    public abstract javafx.scene.Node getRuntimeNode();

    // ================================================================
    // МЕТОДЫ ОБНОВЛЕНИЯ
    // ================================================================

    /**
     * Перерисовывает компонент после изменения свойств.
     * <p>
     * Пользователь изменил текст → вызывается {@code refresh()} →
     * вызывается {@code buildUI()} → компонент обновляется на экране.
     * </p>
     */
    public void refresh() {
        System.out.println("🔄 refresh() вызван");
        buildUI();
    }

    // ================================================================
    // ПРОБРОС СОБЫТИЙ
    // ================================================================

    /**
     * Делает дочерние элементы "прозрачными" для событий мыши.
     * <p>
     * Это нужно, чтобы:
     * <ul>
     *   <li>Клик по кнопке выделял сам {@code BaseComponent}, а не кнопку</li>
     *   <li>Перетаскивание работало через {@code BaseComponent}</li>
     *   <li>Двойной клик по кнопке открывал диалог редактирования</li>
     * </ul>
     * </p>
     * <p>
     * Вызывается в {@code buildUI()} каждого конкретного компонента
     * после добавления дочернего элемента.
     * </p>
     */
    protected void makeChildrenTransparentForMouse() {
        System.out.println("🖱️ makeChildrenTransparentForMouse() вызван");
        getChildren().forEach(node -> {
            if (node instanceof javafx.scene.control.Control ||
                    node instanceof javafx.scene.chart.Chart) {
                node.setMouseTransparent(true);
                System.out.println("  - Установлен mouseTransparent для: " + node.getClass().getSimpleName());
            }
        });
    }

    // ================================================================
    // ГЕТТЕРЫ И СЕТТЕРЫ
    // ================================================================

    /**
     * Возвращает уникальный идентификатор компонента.
     *
     * @return UUID компонента
     */
    public String getComponentId() { return componentId; }

    /**
     * Устанавливает уникальный идентификатор компонента.
     *
     * @param id новый UUID
     */
    public void setComponentId(String id) { this.componentId = id; }

    /**
     * Возвращает человеческое имя компонента.
     *
     * @return имя компонента
     */
    public String getComponentName() { return componentName; }

    /**
     * Устанавливает человеческое имя компонента.
     *
     * @param name новое имя
     */
    public void setComponentName(String name) { this.componentName = name; }

    /**
     * Возвращает технический тип компонента.
     *
     * @return тип ("Button", "Label", "TextField", "Chart")
     */
    public String getComponentType() { return componentType; }

    /**
     * Возвращает текст, отображаемый на компоненте.
     *
     * @return текст компонента
     */
    public String getText() { return text; }

    /**
     * Устанавливает текст, отображаемый на компоненте.
     * После изменения текста компонент автоматически перерисовывается.
     *
     * @param text новый текст
     */
    public void setText(String text) {
        System.out.println("✏️ setText: \"" + text + "\"");
        this.text = text != null ? text : "";
        refresh();
    }

    /**
     * Возвращает размер шрифта.
     *
     * @return размер шрифта в пикселях
     */
    public double getFontSize() { return fontSize; }

    /**
     * Устанавливает размер шрифта.
     * После изменения компонент автоматически перерисовывается.
     *
     * @param size новый размер шрифта
     */
    public void setFontSize(double size) {
        this.fontSize = size;
        refresh();
    }

    /**
     * Возвращает цвет текста в формате HEX.
     *
     * @return цвет текста (например, "#000000")
     */
    public String getTextColor() { return textColor; }

    /**
     * Устанавливает цвет текста в формате HEX.
     * После изменения компонент автоматически перерисовывается.
     *
     * @param color новый цвет (например, "#FF0000")
     */
    public void setTextColor(String color) {
        this.textColor = color != null ? color : "#000000";
        refresh();
    }

    /**
     * Возвращает цвет фона в формате HEX.
     *
     * @return цвет фона (например, "#FFFFFF")
     */
    public String getBackgroundColor() { return backgroundColor; }

    /**
     * Устанавливает цвет фона в формате HEX.
     * После изменения компонент автоматически перерисовывается.
     *
     * @param color новый цвет (например, "#0000FF")
     */
    public void setBackgroundColor(String color) {
        this.backgroundColor = color != null ? color : "#FFFFFF";
        refresh();
    }

    /**
     * Проверяет, разрешено ли изменение размера компонента.
     *
     * @return {@code true} если изменение размера разрешено
     */
    public boolean isResizable() { return resizable; }

    /**
     * Устанавливает разрешение на изменение размера компонента.
     *
     * @param resizable {@code true} если изменение размера разрешено
     */
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * Проверяет, разрешено ли перетаскивание компонента.
     *
     * @return {@code true} если перетаскивание разрешено
     */
    public boolean isDraggable() { return draggable; }

    /**
     * Устанавливает разрешение на перетаскивание компонента.
     *
     * @param draggable {@code true} если перетаскивание разрешено
     */
    public void setDraggable(boolean draggable) { this.draggable = draggable; }

    // ================================================================
    // ВСПОМОГАТЕЛЬНЫЙ МЕТОД
    // ================================================================

    /**
     * Возвращает строковое представление компонента.
     *
     * @return строка вида "Тип [ID]: 'Текст'"
     */
    @Override
    public String toString() {
        return String.format("%s [%s]: '%s'",
                componentType,
                componentId.substring(0, 8),
                text
        );
    }
}