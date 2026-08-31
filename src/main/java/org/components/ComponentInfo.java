package org.components;

import java.util.function.Supplier;

public class ComponentInfo {

    private final Supplier<BaseComponent> supplier;
    private final String displayName;

    public ComponentInfo(Supplier<BaseComponent> supplier, String displayName) {
        this.supplier = supplier;
        this.displayName = displayName;
    }

    public Supplier<BaseComponent> getSupplier() {
        return supplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BaseComponent create() {
        return supplier.get();
    }
}