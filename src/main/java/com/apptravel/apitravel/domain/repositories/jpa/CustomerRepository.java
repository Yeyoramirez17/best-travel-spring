package com.apptravel.apitravel.domain.repositories.jpa;

import com.apptravel.apitravel.domain.entities.jpa.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
