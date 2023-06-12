package com.apptravel.apitravel.domain.repositories.jpa;

import com.apptravel.apitravel.domain.entities.jpa.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<TourEntity, Long> {
}
