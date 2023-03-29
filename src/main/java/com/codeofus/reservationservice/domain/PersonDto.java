package com.codeofus.reservationservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonDto {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String registration;

    public PersonDto() {
    }

    public PersonDto(Integer id, String firstName, String lastName, String registration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registration = registration;
    }

    public Integer getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getRegistration() {
        return this.registration;
    }
}
