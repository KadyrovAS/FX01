package org.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Компонент "Поле ввода" для визуального конструктора.
 * <p>
 * Представляет собой обёртку над стандартным JavaFX-полем ввода {@link TextField}.
 * Наследуется от {@link BaseComponent} и реализует все необходимые методы
 * для работы в конструкторе.
 * </p>
 *
 * @see BaseComponent
 * @see javafx.scene.control.TextField
 */
public class TextFieldComponent extends BaseComponent {

    /** Внутреннее JavaFX-поле ввода */
    private TextField textField;

    /**
     * Создаёт новый компонент "Поле ввода" с текстом по умолчанию.
     */
    public TextFieldComponent() {
        super("TextField");
        setText("Введите текст");
        buildUI();
        System.out.println("📥 TextFieldComponent создан");
    }

    /**
     * Создаёт UI поля ввода.
     * <p>
     * Очищает контейнер, создаёт новое поле ввода с текущими настройками
     * (текст, шрифт, цвета) и добавляет его в контейнер.
     * </p>
     */
    @Override
    protected void buildUI() {
        System.out.println("📥 buildUI() вызван для TextFieldComponent");

        getChildren().clear();

        textField = new TextField(getText());
        textField.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));

        textField.setPrefWidth(getPrefWidth());
        textField.setPrefHeight(getPrefHeight());

        StackPane.setAlignment(textField, Pos.CENTER);

        getChildren().add(textField);

        makeChildrenTransparentForMouse();
    }

    /**
     * Возвращает "чистое" поле ввода для отображения в рантайме.
     * <p>
     * В отличие от {@link #buildUI()}, этот метод создаёт поле ввода
     * без пунктирной рамки и обработчиков конструктора.
     * </p>
     *
     * @return {@link TextField} для использования в готовом приложении
     */
    @Override
    public Node getRuntimeNode() {
        TextField runtimeField = new TextField(getText());
        runtimeField.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s;",
                getFontSize(), getTextColor()
        ));
        return runtimeField;
    }
}