package com.alex.worrall.crudapp.widget;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static com.alex.worrall.crudapp.user.Role.Names.ADMIN;
import static com.alex.worrall.crudapp.user.Role.Names.USER;


@RestController
@RequestMapping("/api/v1/widgets")
public class WidgetApi {

    @Autowired
    WidgetService service;

    @RolesAllowed({USER, ADMIN})
    @GetMapping("/")
    public List<WidgetModel> getAllWidgets() {
        return service.getAllWidgets();
    }


    @RolesAllowed({USER, ADMIN})
    @GetMapping("/{id}")
    public WidgetModel getWidgetById(@PathVariable Long id) throws NotFoundException {
        return service.getWidgetById(id);
    }

    @RolesAllowed({ADMIN})
    @PostMapping("/")
    public WidgetModel createWidget(@RequestBody WidgetModel widget) {
        return service.createWidget(widget);
    }

    @RolesAllowed(ADMIN)
    @PutMapping("/")
    public WidgetModel updateWidget(@RequestBody WidgetModel widgetModel) throws NotFoundException {
        return service.updateWidget(widgetModel);
    }

    @RolesAllowed(ADMIN)
    @DeleteMapping("/{id}")
    public void deleteWidget(@PathVariable Long id) {
        service.deleteWidgetById(id);
    }
}
