package org.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Компонент "Надпись" для визуального конструктора.
 * <p>
 * Представляет собой обёртку над стандартной JavaFX-надписью {@link Label}.
 * Наследуется от {@link BaseComponent} и реализует все необходимые методы
 * для работы в конструкторе.
 * </p>
 *
 * @see BaseComponent
 * @see javafx.scene.control.Label
 */
public class LabelComponent extends BaseComponent {

    /** Внутренняя JavaFX-надпись */
    private Label label;

    /**
     * Создаёт новый компонент "Надпись" с текстом по умолчанию.
     */
    public LabelComponent() {
        super("Label");
        setText("Надпись");
        buildUI();
        System.out.println("📝 LabelComponent создан");
    }

    /**
     * Создаёт UI надписи.
     * <p>
     * Очищает контейнер, создаёт новую надпись с текущими настройками
     * (текст, шрифт, цвета) и добавляет её в контейнер.
     * </p>
     */
    @Override
    protected void buildUI() {
        System.out.println("📝 buildUI() вызван для LabelComponent");

        getChildren().clear();

        label = new Label(getText());
        label.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s; -fx-padding: 5px;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));

        label.setPrefWidth(getPrefWidth());
        label.setPrefHeight(getPrefHeight());

        StackPane.setAlignment(label, Pos.CENTER);

        getChildren().add(label);

        makeChildrenTransparentForMouse();
    }

    /**
     * Возвращает "чистую" надпись для отображения в рантайме.
     * <p>
     * В отличие от {@link #buildUI()}, этот метод создаёт надпись
     * без пунктирной рамки и обработчиков конструктора.
     * </p>
     *
     * @return {@link Label} для использования в готовом приложении
     */
    @Override
    public Node getRuntimeNode() {
        Label runtimeLabel = new Label(getText());
        runtimeLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s;",
                getFontSize(), getTextColor()
        ));
        return runtimeLabel;
    }
}