module com.myapp.dashboard {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jfree.jfreechart;

    // Открываем пакеты для JavaFX (рефлексия)
    opens org.controller to javafx.fxml;
    opens org.model to javafx.base;
    opens org.service to javafx.base;
    opens org.components to javafx.base;
    opens org to javafx.graphics;

    // Экспортируем пакеты
    exports org;
    exports org.controller;
    exports org.model;
    exports org.service;
    exports org.components;
}