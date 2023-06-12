package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.util.enums.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

public interface CatalogService <R> {

    String FIELD_BY_SORT = "price";

    Page<R> realAll(Integer page, Integer size, SortType sortType);

    Set<R> readLeesPrice(BigDecimal price);

    Set<R> readBetweenPrice(BigDecimal min, BigDecimal max);

}
