package com.alex.worrall.crudapp.widget;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class WidgetServiceTest {
    @Mock
    private WidgetRepository repository;
    @Spy
    private WidgetEntity widgetEntity;
    @Spy
    private WidgetModel widgetModel;

    @InjectMocks
    private WidgetService service;

    @BeforeEach
    public void setup() {
        widgetModel = new WidgetModel(1L, "test", "demonstration widget", 100L);
        widgetEntity = new WidgetEntity(1L, widgetModel);
    }

    @Test
    public void testGetAllWidgets() {
        List<WidgetEntity> entities = new ArrayList<>();
        entities.add(widgetEntity);
        when(repository.findAll()).thenReturn(entities);
        List<WidgetModel> allWidgets = service.getAllWidgets();
        for (WidgetModel model : allWidgets) {
            assertEquals(model, widgetModel);
        }
    }

    @Test
    public void testGetWidgetByIdSuccess() throws NotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(widgetEntity));
        WidgetModel actual = service.getWidgetById(1L);
        assertEquals(widgetModel, actual);
    }

    @Test
    public void testGetWidgetByIdNoneFound() {
        assertThrows(NotFoundException.class, () -> service.getWidgetById(2L), "Unable to find " +
                "widget with ID 2");
    }

    @Test
    public void testCreateWidget() {
        when(repository.save(any())).thenReturn(widgetEntity);
        WidgetModel created = service.createWidget(new WidgetModel(null, "test", "demonstration " +
                "widget", 100L));
        assertEquals(created, widgetModel);
        verify(repository, times(1)).save(any());
    }

    @Test
    public void testUpdateWidgetSuccess() throws NotFoundException {
        when(repository.findById(1L)).thenReturn(Optional.of(widgetEntity));
        when(repository.save(widgetEntity)).thenReturn(widgetEntity);
        WidgetModel updatedWidget = new WidgetModel(1L, "Updated Widget", "This has changed", 0L);
        WidgetModel saved = service.updateWidget(updatedWidget);
        assertEquals(updatedWidget, saved);
        verify(repository, times(1)).save(widgetEntity);
    }

    @Test
    public void testUpdateWidgetError() {
        assertThrows(NotFoundException.class, () -> service.updateWidget(new WidgetModel(2L,
                "Updated Widget", "This has changed", 0L)), "Unable to find " +
                "widget with ID 2");
    }

    @Test
    public void testDeleteWidgetById() {
        when(repository.getById(1L)).thenReturn(widgetEntity);
        service.deleteWidgetById(1L);
        verify(repository, times(1)).delete(widgetEntity);
    }
}
