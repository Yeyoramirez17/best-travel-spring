package com.apptravel.apitravel.api.controllers;

import com.apptravel.apitravel.api.models.request.TourRequest;
import com.apptravel.apitravel.api.models.response.ErrorResponse;
import com.apptravel.apitravel.api.models.response.TourResponse;
import com.apptravel.apitravel.infraestructure.abstract_services.ITourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name =" Tour")
public class TourController {

    private final ITourService tourService;

    @ApiResponse(
            responseCode = "400",
            description = "When the request have a field invalid we response this",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            }
    )
    @Operation(
        summary = "Save in database a new tour based in list of hotels ang flights"
    )
    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest request) {
        System.out.println(tourService.getClass().getSimpleName());
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @Operation(
            summary = "Return a Tour with ad passed"
    )
    @GetMapping("{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @Operation(
            summary = "Delete a tour with ad passed"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.tourService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove ticket from tour"
    )
    @PatchMapping(path = "{tourId}/remove_ticket/{tickedId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourId, @PathVariable UUID tickedId) {
        this.tourService.removeTicket(tourId, tickedId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add a ticket from tour"
    )
    @PatchMapping(path = "{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remove reservation from tour"
    )
    @PatchMapping(path = "{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add a reservation from tour"
    )
    @PatchMapping(path = "{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(
            @PathVariable Long tourId,
            @PathVariable Long hotelId,
            @RequestParam Integer totalDays
    ) {
        var response = Collections.singletonMap("reservation-Id", this.tourService.addReservation(tourId, hotelId, totalDays));
        return ResponseEntity.ok(response);
    }
}
