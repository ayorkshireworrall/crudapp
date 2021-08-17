package com.alex.worrall.crudapp.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataInitialiser {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private List<DataInitialiserModule> dataInitialisers;

    @Value("${init.test.data}")
    private boolean initTestData;

    @PostConstruct
    public void performInitialisation() {
        List<DataInitialiserModule> sortedDIs = dataInitialisers.stream()
                .sorted((m1, m2) -> m1.getExecutionOrder() - m2.getExecutionOrder())
                .collect(Collectors.toList());
        for (DataInitialiserModule dataInitialiser : sortedDIs) {
            if (initTestData || dataInitialiser.runInAllEnvs()) {
                log.info("Running initialisation for {}", dataInitialiser.getName());
                dataInitialiser.initialiseData();
            }
        }
    }
}
