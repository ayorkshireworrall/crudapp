package com.alex.worrall.crudapp.widget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class WidgetDataInitialiserTest {
    @Mock
    WidgetService service;

    @InjectMocks
    WidgetDataInitialiser initialiser;

    @Test
    public void testInitialiser() {
        initialiser.initialiseData();
        verify(service, times(1)).createWidget(new WidgetModel(null, "Init Widget", "The first of her name", 1L));
    }
}
