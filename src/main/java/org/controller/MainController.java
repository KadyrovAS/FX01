package org.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.util.Random;

public class MainController {

    // Это точно как @Autowired, но связано с XML разметкой
    @FXML
    private Button generateButton;

    @FXML
    private LineChart<Number, Number> lineChart;

    private final Random random = new Random();

    // Этот метод вызывается автоматически после загрузки FXML (аналог @PostConstruct)
    @FXML
    public void initialize() {
        // Добавляем начальные данные, чтобы график не был пустым
        generateData();

        // Вешаем слушатель на кнопку
        generateButton.setOnAction(this::handleGenerate);
    }

    // Обработчик нажатия кнопки
    private void handleGenerate(ActionEvent event) {
        generateData();
    }

    private void generateData() {
        // Очищаем график
        lineChart.getData().clear();

        // Создаем серию данных (линию)
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Случайные значения");

        ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();

        for (int i = 0; i < 20; i++) {
            // Твоя бэкенд-логика здесь
            int x = i;
            int y = random.nextInt(100);
            data.add(new XYChart.Data<>(x, y));
        }

        series.setData(data);
        lineChart.getData().add(series);
    }
}