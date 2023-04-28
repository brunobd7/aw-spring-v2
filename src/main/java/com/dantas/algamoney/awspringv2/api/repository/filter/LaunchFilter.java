package com.dantas.algamoney.awspringv2.api.repository.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaunchFilter {

    private String description;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate initialDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate finalDate;

}
