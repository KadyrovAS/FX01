package org.service;

import org.components.BaseComponent;
import org.components.ComponentFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ProjectService {

    public void saveProject(String filePath, List<BaseComponent> components) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < components.size(); i++) {
            BaseComponent comp = components.get(i);
            sb.append("  {\n");
            sb.append("    \"type\": \"").append(comp.getComponentType()).append("\",\n");
            sb.append("    \"id\": \"").append(comp.getComponentId()).append("\",\n");
            sb.append("    \"name\": \"").append(comp.getComponentName()).append("\",\n");
            sb.append("    \"text\": \"").append(escapeJson(comp.getText())).append("\",\n");
            sb.append("    \"x\": ").append(comp.getLayoutX()).append(",\n");
            sb.append("    \"y\": ").append(comp.getLayoutY()).append(",\n");
            sb.append("    \"width\": ").append(comp.getPrefWidth()).append(",\n");
            sb.append("    \"height\": ").append(comp.getPrefHeight()).append(",\n");
            sb.append("    \"fontSize\": ").append(comp.getFontSize()).append(",\n");
            sb.append("    \"textColor\": \"").append(comp.getTextColor()).append("\",\n");
            sb.append("    \"backgroundColor\": \"").append(comp.getBackgroundColor()).append("\"\n");
            sb.append("  }");
            if (i < components.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]");

        Files.writeString(
                Paths.get(filePath),
                sb.toString(),
                StandardCharsets.UTF_8
        );

        System.out.println("💾 Проект сохранен: " + filePath + " (" + components.size() + " компонентов)");
    }

    public List<BaseComponent> loadProject(String filePath, ComponentManager manager) throws Exception {
        String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        List<BaseComponent> components = new ArrayList<>();

        // Парсим вручную (упрощенный вариант)
        String[] lines = json.split("\n");
        String currentType = null;
        String currentId = null;
        String currentName = null;
        String currentText = null;
        double currentX = 0;
        double currentY = 0;
        double currentWidth = 150;
        double currentHeight = 50;
        double currentFontSize = 14;
        String currentTextColor = "#000000";
        String currentBackgroundColor = "#FFFFFF";

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("\"type\"")) {
                currentType = extractValue(trimmed);
            } else if (trimmed.startsWith("\"id\"")) {
                currentId = extractValue(trimmed);
            } else if (trimmed.startsWith("\"name\"")) {
                currentName = extractValue(trimmed);
            } else if (trimmed.startsWith("\"text\"")) {
                currentText = extractValue(trimmed);
            } else if (trimmed.startsWith("\"x\"")) {
                currentX = Double.parseDouble(extractNumber(trimmed));
            } else if (trimmed.startsWith("\"y\"")) {
                currentY = Double.parseDouble(extractNumber(trimmed));
            } else if (trimmed.startsWith("\"width\"")) {
                currentWidth = Double.parseDouble(extractNumber(trimmed));
            } else if (trimmed.startsWith("\"height\"")) {
                currentHeight = Double.parseDouble(extractNumber(trimmed));
            } else if (trimmed.startsWith("\"fontSize\"")) {
                currentFontSize = Double.parseDouble(extractNumber(trimmed));
            } else if (trimmed.startsWith("\"textColor\"")) {
                currentTextColor = extractValue(trimmed);
            } else if (trimmed.startsWith("\"backgroundColor\"")) {
                currentBackgroundColor = extractValue(trimmed);
            } else if (trimmed.equals("},") || trimmed.equals("}")) {
                // Создаем компонент
                if (currentType != null) {
                    BaseComponent comp = ComponentFactory.create(currentType);
                    if (currentId != null) comp.setComponentId(currentId);
                    if (currentName != null) comp.setComponentName(currentName);
                    if (currentText != null) comp.setText(currentText);
                    comp.setLayoutX(currentX);
                    comp.setLayoutY(currentY);
                    comp.setPrefWidth(currentWidth);
                    comp.setPrefHeight(currentHeight);
                    comp.setFontSize(currentFontSize);
                    comp.setTextColor(currentTextColor);
                    comp.setBackgroundColor(currentBackgroundColor);
                    components.add(comp);

                    // Сбрасываем для следующего компонента
                    currentType = null;
                    currentId = null;
                    currentName = null;
                    currentText = null;
                }
            }
        }

        System.out.println("📂 Загружено компонентов: " + components.size());
        return components;
    }

    private String extractValue(String line) {
        int start = line.indexOf('"');
        int end = line.lastIndexOf('"');
        if (start == -1 || end == -1 || start == end) return "";
        return line.substring(start + 1, end);
    }

    private String extractNumber(String line) {
        String num = line.replaceAll("[^0-9.\\-]", "");
        return num.isEmpty() ? "0" : num;
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}