package org.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.StackPane;

/**
 * Компонент "График" для визуального конструктора.
 * <p>
 * Представляет собой обёртку над стандартным JavaFX-графиком {@link LineChart}.
 * Наследуется от {@link BaseComponent} и реализует все необходимые методы
 * для работы в конструкторе.
 * </p>
 *
 * @see BaseComponent
 * @see javafx.scene.chart.LineChart
 */
public class ChartComponent extends BaseComponent {

    /** Внутренний JavaFX-график */
    private LineChart<Number, Number> chart;

    /**
     * Создаёт новый компонент "График" с заголовком по умолчанию.
     */
    public ChartComponent() {
        super("Chart");
        setText("График");
        setPrefSize(400, 300);
        buildUI();
        System.out.println("📊 ChartComponent создан");
    }

    /**
     * Создаёт UI графика.
     * <p>
     * Очищает контейнер, создаёт новый график с текущими настройками
     * и добавляет его в контейнер.
     * </p>
     */
    @Override
    protected void buildUI() {
        System.out.println("📊 buildUI() вызван для ChartComponent");

        getChildren().clear();

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Y");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(getText());
        chart.setPrefWidth(getPrefWidth());
        chart.setPrefHeight(getPrefHeight());

        StackPane.setAlignment(chart, Pos.CENTER);

        getChildren().add(chart);

        makeChildrenTransparentForMouse();
    }

    /**
     * Возвращает "чистый" график для отображения в рантайме.
     * <p>
     * В отличие от {@link #buildUI()}, этот метод создаёт график
     * без пунктирной рамки и обработчиков конструктора.
     * </p>
     *
     * @return {@link LineChart} для использования в готовом приложении
     */
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