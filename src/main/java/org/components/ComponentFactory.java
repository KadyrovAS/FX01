package org.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Фабрика для создания компонентов визуального конструктора.
 * <p>
 * Отвечает за:
 * <ul>
 *   <li>Регистрацию всех доступных типов компонентов</li>
 *   <li>Создание экземпляров компонентов по типу</li>
 *   <li>Предоставление метаинформации о компонентах</li>
 * </ul>
 * </p>
 *
 * <p>Использует {@link ComponentInfo} для хранения всей информации о компоненте.</p>
 *
 * @see ComponentInfo
 * @see BaseComponent
 */
public class ComponentFactory {

    /** Реестр: тип компонента → информация о компоненте */
    private static final Map<String, ComponentInfo> registry = new HashMap<>();

    // ================================================================
    // РЕГИСТРАЦИЯ КОМПОНЕНТОВ
    // ================================================================

    static {
        // Регистрация стандартных компонентов
        register("Button", ButtonComponent::new, "🔘 Кнопка");
        register("Label", LabelComponent::new, "📝 Надпись");
        register("TextField", TextFieldComponent::new, "📥 Поле ввода");
        register("Chart", ChartComponent::new, "📊 График");
    }

    /**
     * Регистрирует новый тип компонента.
     *
     * @param type        технический идентификатор (например, "Button")
     * @param supplier    способ создания компонента (ссылка на конструктор)
     * @param displayName отображаемое имя (например, "🔘 Кнопка")
     * @throws IllegalArgumentException если тип уже зарегистрирован или параметры невалидны
     */
    public static void register(String type, Supplier<BaseComponent> supplier, String displayName) {
        if (registry.containsKey(type)) {
            throw new IllegalArgumentException("Компонент с типом '" + type + "' уже зарегистрирован");
        }
        registry.put(type, new ComponentInfo(supplier, displayName));
        System.out.println("📦 Зарегистрирован: " + type + " → " + displayName);
    }

    // ================================================================
    // МЕТОДЫ ДЛЯ РАБОТЫ С КОМПОНЕНТАМИ
    // ================================================================

    /**
     * Создаёт компонент по его типу.
     *
     * @param type технический идентификатор (например, "Button")
     * @return новый экземпляр компонента
     * @throws IllegalArgumentException если тип не зарегистрирован
     */
    public static BaseComponent create(String type) {
        ComponentInfo info = registry.get(type);
        if (info == null) {
            throw new IllegalArgumentException("Неизвестный тип: " + type);
        }
        return info.create();
    }

    /**
     * Возвращает отображаемое имя для типа компонента.
     *
     * @param type технический идентификатор
     * @return человекочитаемое имя (с иконкой), или сам {@code type}, если не найден
     */
    public static String getDisplayName(String type) {
        ComponentInfo info = registry.get(type);
        return info != null ? info.getDisplayName() : type;
    }

    /**
     * Возвращает всю информацию о компоненте.
     *
     * @param type технический идентификатор
     * @return {@link ComponentInfo} или {@code null}, если компонент не зарегистрирован
     */
    public static ComponentInfo getComponentInfo(String type) {
        return registry.get(type);
    }

    /**
     * Возвращает все зарегистрированные ComponentInfo.
     *
     * @return коллекция всех {@link ComponentInfo}
     */
    public static Collection<ComponentInfo> getAllComponentInfos() {
        return registry.values();
    }

    /**
     * Возвращает множество всех зарегистрированных типов.
     *
     * @return множество типов (например, ["Button", "Label", "Chart"])
     */
    public static Set<String> getAvailableTypes() {
        return registry.keySet();
    }

    /**
     * Проверяет, поддерживается ли данный тип.
     *
     * @param type технический идентификатор
     * @return {@code true} если тип зарегистрирован
     */
    public static boolean isTypeSupported(String type) {
        return registry.containsKey(type);
    }

    // ================================================================
    // ОТЛАДОЧНЫЙ МЕТОД
    // ================================================================

    /**
     * Выводит в консоль список всех зарегистрированных компонентов.
     * Полезно для отладки.
     */
    public static void printRegistry() {
        System.out.println("📋 Зарегистрированные компоненты:");
        for (String type : getAvailableTypes()) {
            System.out.println("  - " + type + " → " + getDisplayName(type));
        }
    }
}