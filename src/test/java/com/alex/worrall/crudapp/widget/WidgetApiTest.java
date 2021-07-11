package com.alex.worrall.crudapp.widget;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetApiTest {

    private static final String BASE_URL = "http://localhost:";
    private static final String API_PREFIX = "/api/v1/widgets";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCrudApis() {
        //Pre-existing widget created by the data initialiser
        WidgetModel diWidget = new WidgetModel(1L, "Init Widget", "The first of her name", 1L);
        WidgetModel expected1 = new WidgetModel(2L, "Test", "A test widget", 100L);
        WidgetModel expected2 = new WidgetModel(3L, "Test2", "A test widget", 100L);

        //CREATE
        //create 1
        WidgetModel inputModel1 = new WidgetModel(null, "Test", "A test widget", 100L);
        WidgetModel created1 = this.restTemplate.postForObject(getUrlStart() + "/", inputModel1,
                WidgetModel.class);
        assertEquals(expected1, created1);
        //create 2
        WidgetModel inputModel2 = new WidgetModel(null, "Test2", "A test widget", 100L);
        WidgetModel created2 = this.restTemplate.postForObject(getUrlStart() + "/", inputModel2,
                WidgetModel.class);
        assertEquals(expected2, created2);
        //TODO error handling needs sorting
        //shouldn't be allowed to create a new widget with same values as widget 1
//        WidgetModel inputModel3 = new WidgetModel(null, "Test", "A test widget", 100L);
//        assertThrows(Exception.class, () -> {
//            this.restTemplate.postForObject(getUrlStart() + "/", inputModel3,
//                    WidgetModel.class);
//        });

        //RETRIEVE
        //get single
        WidgetModel singleWidget = this.restTemplate.getForObject(getUrlStart() + "/2", WidgetModel.class);
        assertEquals(expected1, singleWidget);

        //get all
        List<WidgetModel> allWidgets = Arrays.asList(this.restTemplate.getForObject(getUrlStart() + "/",
                WidgetModel[].class));
        List<WidgetModel> expectedWidgets = Arrays.asList(diWidget, expected1, expected2);
        assertEquals(allWidgets, expectedWidgets);

        //UPDATE
        WidgetModel updatedWidget = new WidgetModel(1L, "Changed", "This widget was changed", 101L);
        this.restTemplate.put(getUrlStart() + "/", updatedWidget);
        singleWidget = this.restTemplate.getForObject(getUrlStart() + "/1", WidgetModel.class);
        assertNotEquals(expected1, singleWidget);
        assertEquals(updatedWidget, singleWidget);

        //DELETE
        assertEquals(3, allWidgets.size());
        this.restTemplate.delete(getUrlStart() + "/1");
        allWidgets = Arrays.asList(this.restTemplate.getForObject(getUrlStart() + "/",
                WidgetModel[].class));
        assertEquals(2, allWidgets.size());

    }

    private String getUrlStart() {
        return BASE_URL + port + API_PREFIX;
    }
}
