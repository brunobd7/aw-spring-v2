package com.dantas.algamoney.awspringv2.api.repository;

import com.dantas.algamoney.awspringv2.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
