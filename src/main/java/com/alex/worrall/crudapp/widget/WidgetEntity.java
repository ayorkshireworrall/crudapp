package com.alex.worrall.crudapp.widget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "widget")
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "description", "value"})
})
public class WidgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Long value;

    public WidgetEntity() {
    }

    public WidgetEntity(WidgetModel model) {
        this.name = model.getName();
        this.description = model.getDescription();
        this.value = model.getValue();
    }

    public WidgetModel toModel() {
        return new WidgetModel(this.id, this.name, this.description, this.value);
    }


}
