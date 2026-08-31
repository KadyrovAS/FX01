package org.components;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class ButtonComponent extends BaseComponent {

    private Button button;

    public ButtonComponent() {
        super("Button");
        setText("Кнопка");
        buildUI();
    }

    @Override
    protected void buildUI() {
        getChildren().clear();

        button = new Button(getText());
        button.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));
        button.setPrefWidth(getPrefWidth());
        button.setPrefHeight(getPrefHeight());

        getChildren().add(button);

        makeChildrenTransparentForMouse();

        // ============================================================
        // 🔥 ВАЖНО: хэндл всегда должен быть на переднем плане
        // ============================================================
        if (resizeHandle != null) {
            resizeHandle.toFront();
        }
    }

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