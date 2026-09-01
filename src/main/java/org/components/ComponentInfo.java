package org.components;

import java.util.function.Supplier;

/**
 * Информация о зарегистрированном компоненте.
 * <p>
 * Содержит способ создания (Supplier) и отображаемое имя компонента.
 * Используется в {@link ComponentFactory} для регистрации компонентов.
 * </p>
 *
 * <p>В будущем можно расширить:</p>
 * <ul>
 *   <li>категория компонента</li>
 *   <li>путь к иконке</li>
 *   <li>краткое описание</li>
 *   <li>версия компонента</li>
 *   <li>теги для поиска</li>
 * </ul>
 *
 * @see ComponentFactory
 * @see BaseComponent
 */
public class ComponentInfo {

    /** Поставщик экземпляров компонента */
    private final Supplier<BaseComponent> supplier;

    /** Отображаемое имя компонента (с иконкой) */
    private final String displayName;

    /**
     * Создаёт новую информацию о компоненте.
     *
     * @param supplier    способ создания компонента (ссылка на конструктор)
     * @param displayName отображаемое имя (например, "🔘 Кнопка")
     * @throws IllegalArgumentException если supplier или displayName равны null
     */
    public ComponentInfo(Supplier<BaseComponent> supplier, String displayName) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier не может быть null");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Отображаемое имя не может быть пустым");
        }
        this.supplier = supplier;
        this.displayName = displayName;
    }

    /**
     * Возвращает поставщик экземпляров компонента.
     *
     * @return Supplier для создания компонента
     */
    public Supplier<BaseComponent> getSupplier() {
        return supplier;
    }

    /**
     * Возвращает отображаемое имя компонента.
     *
     * @return отображаемое имя (с иконкой)
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Создаёт новый экземпляр компонента.
     *
     * @return новый экземпляр {@link BaseComponent}
     */
    public BaseComponent create() {
        return supplier.get();
    }

    /**
     * Возвращает строковое представление информации о компоненте.
     *
     * @return строка вида "ComponentInfo{displayName='🔘 Кнопка'}"
     */
    @Override
    public String toString() {
        return "ComponentInfo{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}