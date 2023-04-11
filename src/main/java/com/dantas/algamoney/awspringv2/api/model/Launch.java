package com.dantas.algamoney.awspringv2.api.model;

import com.dantas.algamoney.awspringv2.api.enums.LaunchType;
import jakarta.persistence.*;
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

    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "pay_date")
    private LocalDate payDate;

    @Column(name = "amount_value")
    private BigDecimal amountValue;

    @Enumerated(EnumType.STRING)
    private LaunchType launchType;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

}
