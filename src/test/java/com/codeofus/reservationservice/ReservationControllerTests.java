package com.codeofus.reservationservice;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.ReservationDto;
import com.codeofus.reservationservice.dtos.SpotDto;
import com.codeofus.reservationservice.mappers.ReservationMapper;
import com.codeofus.reservationservice.repositories.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTests extends IntegrationTest {

    static final String RESERVATIONS_API = "/api/v1/reservations";
    static final ZonedDateTime ZONED_DATE_TIME = ZonedDateTime.of(2023, 12, 3, 10, 15, 30, 0, ZoneId.systemDefault());
    static final String ZONED_DATE_TIME_STRING = ZONED_DATE_TIME.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationMapper reservationMapper;

    @MockBean
    ParkingConsumer parkingConsumer;

    Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservationRepository.deleteAll();
        ReservationDto reservationDto = createReservationDto();
        reservation = reservationMapper.reservationDTOtoReservation(reservationDto);
        reservationRepository.save(reservation);
    }

    @AfterAll
    void cleanUp() {
        reservationRepository.deleteAll();
    }

    public ReservationDto createReservationDto() {
        return ReservationDto.builder().personId(1).spotId(1).createdAt(ZONED_DATE_TIME).build();
    }

    @Test
    public void testCreateReservation() throws Exception {
        int sizeBeforeAdding = reservationRepository.findAll().size();
        ReservationDto reservationDto = createReservationDto();

        mockMvc.perform(post(RESERVATIONS_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reservationDto)))
                .andExpect(status().isOk());

        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(sizeBeforeAdding + 1);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(RESERVATIONS_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(ZONED_DATE_TIME_STRING)));
    }

    @Test
    public void testGetReservation() throws Exception {
        mockMvc.perform(get(RESERVATIONS_API + "/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.createdAt").value(ZONED_DATE_TIME_STRING));
    }

    @Test
    public void testDeleteReservation() throws Exception {
        int sizeBeforeDeleting = reservationRepository.findAll().size();

        mockMvc.perform(delete(RESERVATIONS_API + "/{id}", reservation.getId()))
                .andExpect(status().isOk());

        assertEquals(sizeBeforeDeleting - 1, reservationRepository.findAll().size());
    }

    @Test
    public void testUpdateReservation() throws Exception {
        ReservationDto updatedReservationDto = ReservationDto.builder().id(reservation.getId()).personId(2).build();

        mockMvc.perform(put(RESERVATIONS_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(updatedReservationDto)))
                .andExpect(status().isOk());

        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        assertEquals(2, updatedReservation.getPersonId());
    }

    @Test
    public void testGetSpots() throws Exception {
        List<SpotDto> spots = List.of(new SpotDto(1, "address", LocalDateTime.now(), 1, null, null));
        doReturn(spots).when(parkingConsumer).getSpots();

        mockMvc.perform(get(RESERVATIONS_API + "/spots"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(1))
                .andExpect(jsonPath("$.[*].address").value("address"));
    }
}
