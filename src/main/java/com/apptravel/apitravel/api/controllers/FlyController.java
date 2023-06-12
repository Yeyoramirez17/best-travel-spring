package com.apptravel.apitravel.api.controllers;

import com.apptravel.apitravel.api.models.response.FlyResponse;
import com.apptravel.apitravel.infraestructure.abstract_services.IFlyService;
import com.apptravel.apitravel.util.annotations.Notify;
import com.apptravel.apitravel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
@Tag(name =" Fly")
public class FlyController {

    private final IFlyService flyService;

    @Operation( summary = "Return a page with flights can be sorted or no" )
    @GetMapping
    @Notify( value = "GET Fly" )
    public ResponseEntity<Page<FlyResponse> > getAll(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestHeader(required = false) SortType sortType
            ) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());

        if(Objects.isNull(sortType)) sortType = SortType.NONE;

        var response = this.flyService.realAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(@RequestParam BigDecimal price) {
        var response = this.flyService.readLeesPrice(price);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        var response = this.flyService.readBetweenPrice(min, max);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(@RequestParam String origin, @RequestParam String destiny) {
        var response = this.flyService.readByOriginDestiny(origin, destiny);

        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
