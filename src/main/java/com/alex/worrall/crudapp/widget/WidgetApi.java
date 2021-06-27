package com.alex.worrall.crudapp.widget;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/widgets")
public class WidgetApi {

    @Autowired
    WidgetService service;

    @GetMapping("/")
    public List<WidgetModel> getAllWidgets() {
        return service.getAllWidgets();
    }

    @GetMapping("/{id}")
    public WidgetModel getWidgetById(@PathVariable Long id) throws NotFoundException {
        return service.getWidgetById(id);
    }

    @PostMapping("/")
    public WidgetModel createWidget(@RequestBody WidgetModel widget) {
        return service.createWidget(widget);
    }

    @DeleteMapping("/{id}")
    public void deleteWidget(@PathVariable Long id) {
        service.deleteWidgetById(id);
    }
}
