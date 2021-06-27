package com.alex.worrall.crudapp.widget;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WidgetRepository extends JpaRepository<WidgetEntity, Long> {
}
