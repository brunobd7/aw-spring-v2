package com.dantas.algamoney.awspringv2.api.model;

import com.dantas.algamoney.awspringv2.api.enums.LaunchType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "launch")
public class Launch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String description;

    @Column(name = "due_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dueDate;

    @Column(name = "pay_date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate payDate;

    @Column(name = "amount_value")
    private BigDecimal amountValue;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LaunchType launchType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @NotNull
    private Person person;

}
