package org.components;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabelComponent extends BaseComponent {

    private Label label;

    public LabelComponent() {
        super("Label");
        setText("Надпись");
        buildUI();
    }

    @Override
    protected void buildUI() {
        getChildren().clear();

        label = new Label(getText());
        label.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-text-fill: %s; -fx-background-color: %s; -fx-padding: 5px;",
                getFontSize(), getTextColor(), getBackgroundColor()
        ));
        label.setPrefWidth(getPrefWidth());
        label.setPrefHeight(getPrefHeight());

        getChildren().add(label);

        makeChildrenTransparentForMouse();

        if (resizeHandle != null) {
            resizeHandle.toFront();
        }
    }

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