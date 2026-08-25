package org.service;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.model.DesignComponent;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DesignService {

    private final Pane canvas;
    private final List<DesignComponent> components = new ArrayList<>();
    private DesignComponent selectedComponent;

    public DesignService(Pane canvas) {
        this.canvas = canvas;
    }

    // ========== Публичные методы ==========

    public void addComponent(DesignComponent component) {
        components.add(component);
        Node node = component.createNode();
        makeDraggable(node);
        makeSelectable(node);
        canvas.getChildren().add(node);
    }

    public void clear() {
        components.clear();
        canvas.getChildren().clear();
        selectedComponent = null;
    }

    public List<DesignComponent> getComponents() {
        return new ArrayList<>(components);
    }

    public DesignComponent getSelectedComponent() {
        return selectedComponent;
    }

    // ========== Сохранение в FXML ==========

    public void saveToFXML(String filePath) throws Exception {
        StringBuilder fxml = new StringBuilder();

        // Заголовок FXML
        fxml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        fxml.append("<?import javafx.scene.control.*?>\n");
        fxml.append("<?import javafx.scene.layout.*?>\n");
        fxml.append("<?import javafx.scene.chart.*?>\n\n");

        // Корневой элемент
        fxml.append("<AnchorPane xmlns=\"http://javafx.com/javafx/21\" \n");
        fxml.append("            xmlns:fx=\"http://javafx.com/fxml/1\"\n");
        fxml.append("            prefWidth=\"").append(canvas.getWidth()).append("\" \n");
        fxml.append("            prefHeight=\"").append(canvas.getHeight()).append("\">\n");

        // Сохраняем каждый компонент
        for (DesignComponent comp : components) {
            String fxmlElement = convertToFXML(comp);
            if (fxmlElement != null) {
                fxml.append("    ").append(fxmlElement).append("\n");
            }
        }

        fxml.append("</AnchorPane>");

        // Записываем в файл
        Files.writeString(
                Paths.get(filePath),
                fxml.toString(),
                StandardCharsets.UTF_8
        );

        System.out.println("✅ Дизайн сохранен в: " + filePath);
    }

    // ========== Приватные методы ==========

    private String convertToFXML(DesignComponent comp) {
        String type = comp.getType();
        String layout = String.format(
                "layoutX=\"%.0f\" layoutY=\"%.0f\" prefWidth=\"%.0f\" prefHeight=\"%.0f\"",
                comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight()
        );

        return switch (type) {
            case "Button" -> String.format(
                    "<Button %s text=\"%s\" />",
                    layout, escapeXml(comp.getText())
            );
            case "Label" -> String.format(
                    "<Label %s text=\"%s\" />",
                    layout, escapeXml(comp.getText())
            );
            case "TextField" -> String.format(
                    "<TextField %s text=\"%s\" />",
                    layout, escapeXml(comp.getText())
            );
            case "LineChart" -> String.format(
                    "<LineChart %s title=\"%s\">\n" +
                            "    <xAxis><NumberAxis label=\"X\" /></xAxis>\n" +
                            "    <yAxis><NumberAxis label=\"Y\" /></yAxis>\n" +
                            "</LineChart>",
                    layout, escapeXml(comp.getText())
            );
            default -> null;
        };
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    // ========== Drag-and-Drop с обновлением координат ==========

    private void makeDraggable(Node node) {
        node.setOnMousePressed(e -> {
            node.setUserData(new double[]{e.getSceneX() - node.getLayoutX(),
                    e.getSceneY() - node.getLayoutY()});
        });

        node.setOnMouseDragged(e -> {
            double[] offset = (double[]) node.getUserData();
            if (offset != null) {
                double newX = e.getSceneX() - offset[0];
                double newY = e.getSceneY() - offset[1];
                node.setLayoutX(newX);
                node.setLayoutY(newY);

                // ОБНОВЛЯЕМ КООРДИНАТЫ В МОДЕЛИ
                String id = node.getId();
                components.stream()
                        .filter(c -> id.equals(c.getId()))
                        .findFirst()
                        .ifPresent(c -> {
                            c.setX(newX);
                            c.setY(newY);
                        });
            }
        });
    }

    private void makeSelectable(Node node) {
        node.setOnMouseClicked(e -> {
            // Снимаем выделение со всех
            canvas.getChildren().forEach(n -> n.setStyle(""));
            // Выделяем текущий
            node.setStyle("-fx-border-color: #3498db; -fx-border-width: 2px; -fx-border-style: solid;");

            // Находим компонент в модели и сохраняем как selected
            String id = node.getId();
            selectedComponent = components.stream()
                    .filter(c -> id.equals(c.getId()))
                    .findFirst()
                    .orElse(null);

            System.out.println("Выбран компонент: " + selectedComponent);
        });
    }
}