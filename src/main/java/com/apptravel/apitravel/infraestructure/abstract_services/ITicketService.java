package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.api.models.request.TicketRequest;
import com.apptravel.apitravel.api.models.response.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID>{

    BigDecimal findPrice(Long flyId);
}
