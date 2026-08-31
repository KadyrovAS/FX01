package org.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ComponentFactory {

    private static final Map<String, ComponentInfo> registry = new HashMap<>();

    static {
        register("Button", ButtonComponent::new, "🔘 Кнопка");
        register("Label", LabelComponent::new, "📝 Надпись");
        register("TextField", TextFieldComponent::new, "📥 Поле ввода");
        register("Chart", ChartComponent::new, "📊 График");
    }

    public static void register(String type, Supplier<BaseComponent> supplier, String displayName) {
        registry.put(type, new ComponentInfo(supplier, displayName));
        System.out.println("📦 Зарегистрирован: " + type + " → " + displayName);
    }

    public static BaseComponent create(String type) {
        ComponentInfo info = registry.get(type);
        if (info == null) {
            throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
        return info.create();
    }

    public static String getDisplayName(String type) {
        ComponentInfo info = registry.get(type);
        return info != null ? info.getDisplayName() : type;
    }

    public static Set<String> getAvailableTypes() {
        return registry.keySet();
    }

    public static void printRegistry() {
        System.out.println("📋 Зарегистрированные компоненты:");
        for (String type : getAvailableTypes()) {
            System.out.println("  - " + type + " → " + getDisplayName(type));
        }
    }
}