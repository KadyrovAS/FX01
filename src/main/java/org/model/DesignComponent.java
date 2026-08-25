package org.model;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.util.UUID;

public class DesignComponent {
    private String id = UUID.randomUUID().toString();
    private String type;          // "Button", "Label", "TextField", "LineChart"
    private double x;
    private double y;
    private double width = 100;
    private double height = 30;
    private String text = "";

    // ========== Геттеры и сеттеры ==========

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // ========== Фабричный метод ==========

    public Node createNode() {
        Node node = switch (type) {
            case "Button" -> new Button(text);
            case "Label" -> new Label(text);
            case "TextField" -> new TextField(text);
            case "LineChart" -> {
                NumberAxis xAxis = new NumberAxis();
                xAxis.setLabel("X");
                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Y");
                LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
                chart.setTitle(text);
                yield chart;
            }
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };

        // Устанавливаем позицию
        node.setLayoutX(x);
        node.setLayoutY(y);

        // Устанавливаем размер (приводим к Region, т.к. Node не имеет setPrefWidth/setPrefHeight)
        if (node instanceof Region region) {
            region.setPrefWidth(width);
            region.setPrefHeight(height);
        }

        node.setId(id);
        return node;
    }

    @Override
    public String toString() {
        return "DesignComponent{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}