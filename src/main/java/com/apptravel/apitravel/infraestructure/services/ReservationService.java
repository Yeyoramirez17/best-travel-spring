package com.apptravel.apitravel.infraestructure.services;

import com.apptravel.apitravel.api.models.request.ReservationRequest;
import com.apptravel.apitravel.api.models.response.HotelResponse;
import com.apptravel.apitravel.api.models.response.ReservationResponse;
import com.apptravel.apitravel.domain.entities.jpa.ReservationEntity;
import com.apptravel.apitravel.domain.repositories.jpa.CustomerRepository;
import com.apptravel.apitravel.domain.repositories.jpa.HotelRepository;
import com.apptravel.apitravel.domain.repositories.jpa.ReservationRepository;
import com.apptravel.apitravel.infraestructure.abstract_services.IReservationService;
import com.apptravel.apitravel.infraestructure.helpers.ApiCurrencyConnectorHelper;
import com.apptravel.apitravel.infraestructure.helpers.BlackListHelper;
import com.apptravel.apitravel.infraestructure.helpers.CustomerHelper;
import com.apptravel.apitravel.infraestructure.helpers.EmailHelper;
import com.apptravel.apitravel.util.enums.Tables;
import com.apptravel.apitravel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final ApiCurrencyConnectorHelper currencyConnectorHelper;
    private final EmailHelper emailHelper;

    public static final BigDecimal charges_price_percentages = BigDecimal.valueOf(0.20);

    @Override
    public ReservationResponse create(ReservationRequest request) {
        this.blackListHelper.isInBlackListCustomer(request.getIdClient());

        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        var reservationToPersist = ReservationEntity.
                builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentages)))
                .build();

        var reservationPersisted = this.reservationRepository.save(reservationToPersist);

        this.customerHelper.incrase(customer.getDni(), ReservationService.class);

        if(Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.reservation.name());

        return this.entityToResponse(reservationPersisted);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        var reservationFromDB = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));

        return this.entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID uuid) {
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        var reservationToUpdate = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException(Tables.reservation.name()));

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentages)));

        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);

        log.info("Reservation updated with id: {}", reservationUpdated.getId());

        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID uuid) {
        var reservationToDelete = this.reservationRepository.findById(uuid).orElseThrow(() -> new IdNotFoundException("reservation"));
        this.reservationRepository.delete(reservationToDelete);
    }

    @Override
    public BigDecimal findPrice(Long hotelId, Currency currency) {
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException("hotel"));
        var priceInDollars = hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentages));

        if(currency.equals(Currency.getInstance("USD"))) return priceInDollars;

        var currencyDto = this.currencyConnectorHelper.getCurrency(currency);
        log.info("API currency in {}, response {}", currencyDto.getExchangeDate().toString(), currencyDto.getRates());

        return priceInDollars.multiply(currencyDto.getRates().get(currency));
    }

    private ReservationResponse entityToResponse(ReservationEntity entity) {
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);

        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);

        response.setHotel(hotelResponse);

        return response;
    }
}
