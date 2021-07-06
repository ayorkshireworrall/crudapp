package com.alex.worrall.crudapp.widget;

import java.util.Objects;

public class WidgetModel {
    private Long id;
    private String name;
    private String description;
    private Long value;

    public WidgetModel() {
    }

    public WidgetModel(Long id, String name, String description, Long value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof WidgetModel)) {
            return false;
        }
        WidgetModel wm = (WidgetModel) obj;
        return Objects.equals(id, wm.id)
                && Objects.equals(name, wm.name)
                && Objects.equals(description, wm.description)
                && Objects.equals(value, wm.value);
    }
}
