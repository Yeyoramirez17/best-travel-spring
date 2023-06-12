package com.apptravel.apitravel.domain.repositories.jpa;

import com.apptravel.apitravel.domain.entities.jpa.TicketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TicketRepository extends CrudRepository<TicketEntity, UUID> {
}
