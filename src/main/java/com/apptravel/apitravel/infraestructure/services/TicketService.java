package com.apptravel.apitravel.infraestructure.services;

import com.apptravel.apitravel.api.models.request.TicketRequest;
import com.apptravel.apitravel.api.models.response.FlyResponse;
import com.apptravel.apitravel.api.models.response.TicketResponse;
import com.apptravel.apitravel.domain.entities.jpa.TicketEntity;
import com.apptravel.apitravel.domain.repositories.jpa.CustomerRepository;
import com.apptravel.apitravel.domain.repositories.jpa.FlyRepository;
import com.apptravel.apitravel.domain.repositories.jpa.TicketRepository;
import com.apptravel.apitravel.infraestructure.abstract_services.ITicketService;
import com.apptravel.apitravel.infraestructure.helpers.BlackListHelper;
import com.apptravel.apitravel.infraestructure.helpers.CustomerHelper;
import com.apptravel.apitravel.infraestructure.helpers.EmailHelper;
import com.apptravel.apitravel.util.BestTravelUtil;
import com.apptravel.apitravel.util.enums.Tables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    // 25 %
    public static  final BigDecimal changer_price_percentage = BigDecimal.valueOf(0.25);

    @Override
    public TicketResponse create(TicketRequest request) {
        this.blackListHelper.isInBlackListCustomer(request.getIdClient());

        var fly = this.flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow();

        var ticketToPersist = TicketEntity
                .builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(changer_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .build();

        var ticketPersisted = this.ticketRepository.save(ticketToPersist);

        if(Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.ticket.name());

        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        this.customerHelper.incrase(customer.getDni(), TicketService.class);

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        var ticketFromDb = this.ticketRepository.findById(uuid).orElseThrow();
        return this.entityToResponse(ticketFromDb);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        var fly = this.flyRepository.findById(request.getIdFly()).orElseThrow();
        var ticketToUpdate = this.ticketRepository.findById(uuid).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(changer_price_percentage)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatter());

        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated with id: {}", ticketUpdated.getId());

        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID uuid) {
        var ticketToDelete = this.ticketRepository.findById(uuid).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);

        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);

        response.setFly(flyResponse);

        return response;
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow();
        return fly.getPrice().add(fly.getPrice().multiply(changer_price_percentage));
    }
}
