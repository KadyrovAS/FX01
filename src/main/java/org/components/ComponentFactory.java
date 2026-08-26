package org.components;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Фабрика для создания компонентов визуального конструктора.
 *
 * Отвечает за:
 * - Регистрацию всех доступных типов компонентов
 * - Создание экземпляров компонентов по типу
 * - Предоставление отображаемых имен для каждого типа
 */
public class ComponentFactory {

    // ================================================================
    // 1. РЕЕСТРЫ
    // ================================================================

    /**
     * Реестр: тип компонента → способ его создания (Supplier)
     */
    private static final Map<String, Supplier<BaseComponent>> registry = new HashMap<>();

    /**
     * Реестр: тип компонента → его отображаемое имя (для UI)
     */
    private static final Map<String, String> displayNames = new HashMap<>();

    // ================================================================
    // 2. РЕГИСТРАЦИЯ КОМПОНЕНТОВ (выполняется при загрузке класса)
    // ================================================================

    static {
        // Регистрируем все стандартные компоненты
        // Каждый компонент регистрируется с:
        // - техническим идентификатором (тип)
        // - конструктором (как создать)
        // - человекочитаемым именем (для отображения в UI)
        register("Button", ButtonComponent::new, "🔘 Кнопка");
        register("Label", LabelComponent::new, "📝 Надпись");
        register("TextField", TextFieldComponent::new, "📥 Поле ввода");
        register("Chart", ChartComponent::new, "📊 График");
    }

    /**
     * Зарегистрировать новый тип компонента.
     *
     * @param type        технический идентификатор (например, "Button")
     * @param supplier    способ создания компонента (ссылна на конструктор)
     * @param displayName отображаемое имя (например, "🔘 Кнопка")
     */
    public static void register(String type, Supplier<BaseComponent> supplier, String displayName) {
        registry.put(type, supplier);
        displayNames.put(type, displayName);
        System.out.println("📦 Зарегистрирован компонент: " + type + " → " + displayName);
    }

    // ================================================================
    // 3. МЕТОДЫ ДЛЯ РАБОТЫ С КОМПОНЕНТАМИ
    // ================================================================

    /**
     * Создать компонент по его типу.
     *
     * @param type технический идентификатор (например, "Button")
     * @return новый экземпляр компонента
     * @throws IllegalArgumentException если тип не зарегистрирован
     */
    public static BaseComponent create(String type) {
        Supplier<BaseComponent> supplier = registry.get(type);
        if (supplier == null) {
            throw new IllegalArgumentException("Неизвестный тип компонента: " + type);
        }
        return supplier.get();
    }

    /**
     * Получить отображаемое имя для типа компонента.
     *
     * @param type технический идентификатор
     * @return человекочитаемое имя (с иконкой), или сам type, если не найден
     */
    public static String getDisplayName(String type) {
        return displayNames.getOrDefault(type, type);
    }

    /**
     * Получить список всех зарегистрированных типов.
     *
     * @return список типов (например, ["Button", "Label", ...])
     */
    public static java.util.List<String> getAvailableTypes() {
        return new java.util.ArrayList<>(registry.keySet());
    }

    /**
     * Проверить, поддерживается ли данный тип.
     */
    public static boolean isTypeSupported(String type) {
        return registry.containsKey(type);
    }

    // ================================================================
    // 4. ОТЛАДОЧНЫЙ МЕТОД
    // ================================================================

    /**
     * Вывести в консоль список всех зарегистрированных компонентов.
     * Полезно для отладки.
     */
    public static void printRegistry() {
        System.out.println("📋 Зарегистрированные компоненты:");
        for (String type : getAvailableTypes()) {
            System.out.println("  - " + type + " → " + getDisplayName(type));
        }
    }
}