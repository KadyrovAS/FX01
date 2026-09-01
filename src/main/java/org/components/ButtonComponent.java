package org.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

/**
 * Компонент "Кнопка" для визуального конструктора.
 * <p>
 * Представляет собой обёртку над стандартной JavaFX-кнопкой {@link Button}.
 * Наследуется от {@link BaseComponent} и реализует все необходимые методы
 * для работы в конструкторе.
 * </p>
 *
 * @see BaseComponent
 * @see javafx.scene.control.Button
 */
public class ButtonComponent extends BaseComponent {

    /** Внутренняя JavaFX-кнопка */
    private Button button;

    /**
     * Создаёт новый компонент "Кнопка" с текстом по умолчанию.
     */
    public ButtonComponent() {
        super("Button");
        setText("Кнопка");
        buildUI();
        System.out.println("🔘 ButtonComponent создан");
    }

    /**
     * Создаёт UI кнопки.
     * <p>
     * Очищает контейнер, создаёт новую кнопку с текущими настройками
     * (текст, шрифт, цвета) и добавляет её в контейнер.
     * </p>
     */
    @Override
    protected void buildUI() {
        System.out.println("🔘 buildUI() вызван для ButtonComponent");

        getChildren().clear();

        button = new Button(getText());
        button.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));

        button.setPrefWidth(getPrefWidth());
        button.setPrefHeight(getPrefHeight());

        StackPane.setAlignment(button, Pos.CENTER);

        getChildren().add(button);

        makeChildrenTransparentForMouse();
    }

    /**
     * Возвращает "чистую" кнопку для отображения в рантайме.
     * <p>
     * В отличие от {@link #buildUI()}, этот метод создаёт кнопку
     * без пунктирной рамки и обработчиков конструктора.
     * </p>
     *
     * @return {@link Button} для использования в готовом приложении
     */
    @Override
    public Node getRuntimeNode() {
        Button runtimeButton = new Button(getText());
        runtimeButton.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));
        return runtimeButton;
    }
}