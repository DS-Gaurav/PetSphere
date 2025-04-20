package com.examplejjwt.jwtauth.responsedata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UploadResponse {
    private final int validcount;
    private final List<InvalidRecord> invalidRecords;

}
