package com.alex.worrall.crudapp.widget;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WidgetService {

    @Autowired
    private WidgetRepository repository;

    public List<WidgetModel> getAllWidgets() {
        List<WidgetEntity> widgetEntities = repository.findAll();
        return widgetEntities.stream().map(WidgetEntity::toModel).collect(Collectors.toList());
    }

    public WidgetModel getWidgetById(Long id) throws NotFoundException {
        WidgetEntity widgetEntity = repository.findById(id).orElse(null);
        if (widgetEntity == null){
            throw new NotFoundException(String.format("Unable to find widget with ID %d", 2));
        }
        return widgetEntity.toModel();
    }

    public WidgetModel createWidget(WidgetModel widgetModel) {
        WidgetEntity widgetEntity = new WidgetEntity(widgetModel);
        return repository.save(widgetEntity).toModel();
    }

    public void deleteWidgetById(Long id) {
        WidgetEntity widgetEntityById = repository.getById(id);
        repository.delete(widgetEntityById);
    }
}
