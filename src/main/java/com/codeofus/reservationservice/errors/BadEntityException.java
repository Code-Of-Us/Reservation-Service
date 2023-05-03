package com.codeofus.reservationservice.errors;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BadEntityException extends RuntimeException {

    String message;

    String entityName;

    String errorKey;

}
