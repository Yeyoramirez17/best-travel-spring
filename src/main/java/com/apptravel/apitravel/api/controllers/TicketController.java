package com.apptravel.apitravel.api.controllers;

import com.apptravel.apitravel.api.models.request.TicketRequest;
import com.apptravel.apitravel.api.models.response.ErrorResponse;
import com.apptravel.apitravel.api.models.response.TicketResponse;
import com.apptravel.apitravel.infraestructure.abstract_services.ITicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name =" Ticket")
public class TicketController {

    private final ITicketService ticketService;

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
            summary = "Save a ticket on the database"
    )
    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok(ticketService.create(request));
    }

    @Operation(
            summary = "Return a Ticket"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable(name = "id") UUID uuid) {
        return ResponseEntity.ok(this.ticketService.read(uuid));
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> put(@PathVariable UUID id, @RequestBody TicketRequest request) {
        return ResponseEntity.ok(this.ticketService.update(request, id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(flyId)));
    }
}
