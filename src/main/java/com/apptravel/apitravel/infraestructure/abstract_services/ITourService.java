package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.api.models.request.TourRequest;
import com.apptravel.apitravel.api.models.response.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCrudService<TourRequest, TourResponse, Long> {

    void removeTicket(Long tourId, UUID ticketId);

    UUID addTicket(Long tourId, Long flyId);

    void removeReservation(Long tourId, UUID reservationId);

    UUID addReservation(Long hotelId, Long tourId, Integer totalDays);

}
