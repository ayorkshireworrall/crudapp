package com.alex.worrall.crudapp.widget;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WidgetService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WidgetRepository repository;

    public List<WidgetModel> getAllWidgets() {
        List<WidgetEntity> widgetEntities = repository.findAll();
        return widgetEntities.stream().map(WidgetEntity::toModel).collect(Collectors.toList());
    }

    public WidgetModel getWidgetById(Long id) throws NotFoundException {
        WidgetEntity widgetEntity = repository.findById(id).orElse(null);
        if (widgetEntity == null){
            throw new NotFoundException(String.format("Unable to find widget with ID %d", id));
        }
        return widgetEntity.toModel();
    }

    public WidgetModel createWidget(WidgetModel widgetModel) {
        WidgetEntity widgetEntity = new WidgetEntity(widgetModel);
        log.info("Creating widget {}", widgetModel.getName());
        return repository.save(widgetEntity).toModel();
    }

    public WidgetModel updateWidget(WidgetModel widgetModel) throws NotFoundException {
        WidgetEntity widgetEntity = repository.findById(widgetModel.getId()).orElse(null);
        if (widgetEntity == null){
            log.warn("No widget found with ID {}", widgetModel.getId());
            throw new NotFoundException(String.format("Unable to find widget with ID %d", 2));
        }
        log.info("Updating widget with ID {}", widgetModel.getId());
        widgetEntity.setName(widgetModel.getName());
        widgetEntity.setDescription(widgetModel.getDescription());
        widgetEntity.setValue(widgetModel.getValue());
        return repository.save(widgetEntity).toModel();
    }

    public void deleteWidgetById(Long id) {
        WidgetEntity widgetEntityById = repository.getById(id);
        log.info("Deleting widget {}", widgetEntityById.toModel().getName());
        repository.delete(widgetEntityById);
    }
}
