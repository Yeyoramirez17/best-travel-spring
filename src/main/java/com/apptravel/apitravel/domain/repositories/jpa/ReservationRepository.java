package com.apptravel.apitravel.domain.repositories.jpa;

import com.apptravel.apitravel.domain.entities.jpa.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {
}
