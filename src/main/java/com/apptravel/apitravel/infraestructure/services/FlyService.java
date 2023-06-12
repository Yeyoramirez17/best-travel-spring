package com.apptravel.apitravel.infraestructure.services;

import com.apptravel.apitravel.api.models.response.FlyResponse;
import com.apptravel.apitravel.domain.entities.jpa.FlyEntity;
import com.apptravel.apitravel.domain.repositories.jpa.FlyRepository;
import com.apptravel.apitravel.infraestructure.abstract_services.IFlyService;
import com.apptravel.apitravel.util.constants.CacheConstants;
import com.apptravel.apitravel.util.enums.SortType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class FlyService implements IFlyService {

    private final FlyRepository flyRepository;
    private final WebClient webClient;

    public FlyService(FlyRepository flyRepository, @Qualifier(value = "base") WebClient webClient) {
        this.flyRepository = flyRepository;
        this.webClient = webClient;
    }

    @Override
    public Page<FlyResponse> realAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;

        switch (sortType) {
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending() );
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }

        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    @Cacheable( value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readLeesPrice(BigDecimal price) {
        return this.flyRepository.selectLessPrice(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable( value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readBetweenPrice(BigDecimal min, BigDecimal max) {
        return this.flyRepository.selectBetweenPrice(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    @Override
    @Cacheable( value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readByOriginDestiny(String origin, String destiny) {
        return this.flyRepository.selectOriginDestiny(origin, destiny)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    private FlyResponse entityToResponse(FlyEntity entity) {
        FlyResponse response = new FlyResponse();

        BeanUtils.copyProperties(entity, response);

        return response;
    }
}
