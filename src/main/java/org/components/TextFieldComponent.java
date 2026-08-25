package org.components;

import javafx.scene.Node;
import javafx.scene.control.TextField;

public class TextFieldComponent extends BaseComponent {

    private TextField textField;

    public TextFieldComponent() {
        super("TextField");
        setText("Введите текст");
        buildUI();
    }

    @Override
    protected void buildUI() {
        getChildren().clear();
        textField = new TextField(getText());  // ← используем getText(), а не text
        textField.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));
        textField.setPrefWidth(getPrefWidth());
        textField.setPrefHeight(getPrefHeight());
        getChildren().add(textField);
    }

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