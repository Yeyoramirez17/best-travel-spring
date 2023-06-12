package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.api.models.response.HotelResponse;

import java.util.Set;

public interface IHotelService extends CatalogService <HotelResponse> {

    Set<HotelResponse> readByRating(Integer rating);

}
