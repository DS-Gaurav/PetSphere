package com.examplejjwt.jwtauth.service;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileCsv {
    @CsvBindByName(column = "mobile")
    private String umobile;
    @CsvBindByName(column = "email")
    private String uemail;
    @CsvBindByName(column = "age")
    private int uage;
    @CsvBindByName(column = "user_id")
    private Long uid;
}
