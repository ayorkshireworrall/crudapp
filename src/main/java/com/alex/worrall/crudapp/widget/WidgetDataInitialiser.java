package com.alex.worrall.crudapp.widget;

import com.alex.worrall.crudapp.framework.DataInitialiserModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WidgetDataInitialiser implements DataInitialiserModule {

    @Autowired
    WidgetService service;

    @Override
    public void initialiseData() {
        service.createWidget(new WidgetModel(null, "Init Widget", "The first of her name", 1L));
    }
}
