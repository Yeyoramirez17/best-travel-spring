package com.apptravel.apitravel.infraestructure.abstract_services;

import com.apptravel.apitravel.api.models.response.FlyResponse;

import java.util.Set;

public interface IFlyService extends CatalogService <FlyResponse>{

    Set<FlyResponse> readByOriginDestiny(String origin, String destiny);
}
