package org.components;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Фабрика для создания компонентов по типу.
 * Позволяет регистрировать новые компоненты динамически.
 */
public class ComponentFactory {

    private static final Map<String, Supplier<BaseComponent>> registry = new HashMap<>();

    static {
        // Регистрация стандартных компонентов
        register("Button", ButtonComponent::new);
        register("Label", LabelComponent::new);
        register("TextField", TextFieldComponent::new);
        register("Chart", ChartComponent::new);
    }

    public static void register(String type, Supplier<BaseComponent> supplier) {
        registry.put(type, supplier);
    }

    public static BaseComponent create(String type) {
        Supplier<BaseComponent> supplier = registry.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown component type: " + type);
        }
        return supplier.get();
    }

    public static java.util.List<String> getAvailableTypes() {
        return new java.util.ArrayList<>(registry.keySet());
    }

    public static boolean isTypeSupported(String type) {
        return registry.containsKey(type);
    }
}