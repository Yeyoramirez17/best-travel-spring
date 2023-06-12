package com.apptravel.apitravel.domain.repositories.jpa;

import com.apptravel.apitravel.domain.entities.jpa.FlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface FlyRepository extends JpaRepository<FlyEntity, Long> {

    @Query("SELECT f FROM fly f WHERE f.price < :price")
    Set<FlyEntity> selectLessPrice(BigDecimal price);

    @Query("SELECT f FROM fly f WHERE f.price BETWEEN :min AND :max")
    Set<FlyEntity> selectBetweenPrice(BigDecimal min, BigDecimal max);

    @Query("SELECT f FROM fly f WHERE f.originName = :origin AND f.destinyName = :destiny")
    Set<FlyEntity> selectOriginDestiny(String origin, String destiny);

    @Query("SELECT f FROM fly f JOIN f.tickets t WHERE t.id = :id")
    Optional<FlyEntity> findByTicketId(UUID id);
}
