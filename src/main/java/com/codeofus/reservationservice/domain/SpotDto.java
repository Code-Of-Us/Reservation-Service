package com.codeofus.reservationservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SpotDto {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String address;

    @JsonProperty
    private LocalDateTime availability;

    @JsonProperty
    private Integer capacity;

    @JsonProperty
    private PersonDto renter;

    @JsonProperty
    private PersonDto parker;

    public SpotDto() {
    }

    public SpotDto(Integer id, String address, LocalDateTime availability, Integer capacity, PersonDto renter, PersonDto parker) {
        this.id = id;
        this.address = address;
        this.availability = availability;
        this.capacity = capacity;
        this.renter = renter;
        this.parker = parker;
    }

    public Integer getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public LocalDateTime getAvailability() {
        return this.availability;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public PersonDto getRenter() {
        return this.renter;
    }

    public PersonDto getParker() {
        return this.parker;
    }
}
