package com.examplejjwt.jwtauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetDto {
    public Long id;
    public String category;
    public String image;
    public String breed;
    public int age;
    public Long price;
    public String mceretificate;
    public String username;

    public PetDto(Long id, String category, String image, String breed, int age, Long price, String mceretificate) {
        this.id=id;
        this.category=category;
        this.image=image;
        this.breed=breed;
        this.age=age;
        this.price=price;
        this.mceretificate=mceretificate;
    }
}
