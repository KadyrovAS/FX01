package org.components;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

public class ChartComponent extends BaseComponent {

    private LineChart<Number, Number> chart;

    public ChartComponent() {
        super("Chart");
        setText("График");
        setPrefSize(400, 300);
        buildUI();
    }

    @Override
    protected void buildUI() {
        getChildren().clear();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(getText());
        chart.setPrefWidth(getPrefWidth());
        chart.setPrefHeight(getPrefHeight());
        getChildren().add(chart);
    }

    @Override
    public Node getRuntimeNode() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");
        LineChart<Number, Number> runtimeChart = new LineChart<>(xAxis, yAxis);
        runtimeChart.setTitle(getText());
        return runtimeChart;
    }
}