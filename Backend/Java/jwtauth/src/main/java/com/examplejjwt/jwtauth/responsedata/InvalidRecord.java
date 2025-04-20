package com.examplejjwt.jwtauth.responsedata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidRecord {
    private final int rowNumber;
    private final String data;
    private final String errormessage;
}
