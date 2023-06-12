package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.api.models.request.ReservationRequest;
import com.apptravel.apitravel.api.models.response.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {

    BigDecimal findPrice(Long hotelId, Currency currency);
}
