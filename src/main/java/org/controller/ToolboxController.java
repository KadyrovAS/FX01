package org.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.model.DesignComponent;
import org.service.DesignService;

public class ToolboxController {

    private DesignService designService;

    public void setDesignService(DesignService designService) {
        this.designService = designService;
    }

    @FXML
    private void addButton(ActionEvent event) {
        if (designService == null) {
            System.err.println("DesignService не установлен!");
            return;
        }
        DesignComponent comp = new DesignComponent();
        comp.setType("Button");
        comp.setText("Кнопка");
        comp.setX(50);
        comp.setY(50);
        comp.setWidth(120);
        comp.setHeight(30);
        designService.addComponent(comp);
    }

    @FXML
    private void addLabel(ActionEvent event) {
        if (designService == null) return;
        DesignComponent comp = new DesignComponent();
        comp.setType("Label");
        comp.setText("Текст");
        comp.setX(50);
        comp.setY(100);
        comp.setWidth(100);
        comp.setHeight(25);
        designService.addComponent(comp);
    }

    @FXML
    private void addTextField(ActionEvent event) {
        if (designService == null) return;
        DesignComponent comp = new DesignComponent();
        comp.setType("TextField");
        comp.setText("Введите текст");
        comp.setX(50);
        comp.setY(150);
        comp.setWidth(150);
        comp.setHeight(30);
        designService.addComponent(comp);
    }

    @FXML
    private void addChart(ActionEvent event) {
        if (designService == null) return;
        DesignComponent comp = new DesignComponent();
        comp.setType("LineChart");
        comp.setText("График");
        comp.setX(50);
        comp.setY(200);
        comp.setWidth(400);
        comp.setHeight(300);
        designService.addComponent(comp);
    }

    @FXML
    private void clearCanvas(ActionEvent event) {
        if (designService == null) return;
        designService.clear();
    }
}