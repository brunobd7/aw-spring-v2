package com.dantas.algamoney.awspringv2.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    @Size(max = 100)
    private String street;

    @Column(name = "house_number")
    private Integer number;

    @Size(max = 100)
    private String complement;

    @Size(max = 100)
    private String disctrictNeighborhood;

    @NotNull
    @Size(max = 7)
    private String postalCode;

    @Size(max = 100)
    private String city;

}
