package com.codeofus.reservationservice;

import com.codeofus.reservationservice.client.ParkingConsumer;
import com.codeofus.reservationservice.domain.Reservation;
import com.codeofus.reservationservice.dtos.ReservationDto;
import com.codeofus.reservationservice.dtos.SpotDto;
import com.codeofus.reservationservice.mappers.ReservationMapper;
import com.codeofus.reservationservice.repositories.ReservationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTests extends IntegrationTest {

    static final String RESERVATIONS_API = "/api/v1/reservations";
    static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2023, 12, 3, 10, 15);

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
        return ReservationDto.builder().personId(1).spotId(1).createdAt(LOCAL_DATE_TIME).build();
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
        MvcResult result = mockMvc.perform(get(RESERVATIONS_API))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Page<ReservationDto> reservations = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        List<ReservationDto> content = reservations.getContent();

        assertThat(content).containsExactly(reservationMapper.reservationToReservationDTO(reservation));
    }

    @Test
    public void testGetReservation() throws Exception {
        MvcResult result = mockMvc.perform(get(RESERVATIONS_API + "/{id}", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        ReservationDto reservationDto = objectMapper.readValue(jsonResponse, ReservationDto.class);

        assertThat(reservationDto).isEqualTo(reservationMapper.reservationToReservationDTO(reservation));
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
        SpotDto spotDto = new SpotDto(1, "address", "2.1", null);
        Page<SpotDto> spots = new PageImpl<>(List.of(spotDto));

        doReturn(spots).when(parkingConsumer).getAllSpots(any());

        MvcResult result = mockMvc.perform(get(RESERVATIONS_API + "/spots"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Page<SpotDto> spotPage = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        List<SpotDto> content = spotPage.getContent();

        assertThat(content).containsExactly(spotDto);
    }
}
